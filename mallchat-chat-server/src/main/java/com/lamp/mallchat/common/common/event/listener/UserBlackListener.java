package com.lamp.mallchat.common.common.event.listener;


import com.lamp.mallchat.common.common.event.UserBlackEvent;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.service.cache.UserCache;
import com.lamp.mallchat.common.websocket.service.WebSocketService;
import com.lamp.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * 用户拉黑监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class UserBlackListener {

    @Resource
    private WebSocketService webSocketService;
    @Resource
    private UserCache userCache;
    @Resource
    private UserDao userDao;


    /**
     * 事件一：向所有在线用户发送拉黑消息
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendMsg(UserBlackEvent event) {
        User user = event.getUser();
        webSocketService.sendToAllOnline(WebSocketAdapter.buildBlack(user), user.getId());
    }

    /**
     * 事件二：对被拉黑用户用户进行拉黑状态更新
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeUserStatus(UserBlackEvent event) {
        userDao.invalidByUid(event.getUser().getId());
    }

    /**
     * 事件三：删除本地被拉黑用户的缓存
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void evict(UserBlackEvent event) {
        userCache.evictBlackMap();
    }


}
