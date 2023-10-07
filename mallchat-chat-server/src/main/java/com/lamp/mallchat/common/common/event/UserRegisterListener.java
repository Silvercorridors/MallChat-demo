package com.lamp.mallchat.common.common.event;

import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.enums.IdempotentEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemEnum;
import com.lamp.mallchat.common.user.service.UserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * @author qyxmzg
 * @date 2023/10/7 17:18
 * @description 用户注册事件监听器
 */
@Component
public class UserRegisterListener {

    public static final int TOP_100 = 100;
    public static final int TOP_10 = 10;
    @Resource
    private UserBackpackService userBackpackService;

    @Resource
    private UserDao userDao;


    /**
     * 订阅者：监听事件，新用户注册发放改名卡
     * 异步执行
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendCard(UserRegisterEvent event) {
        User user = event.getUser();
        // 监听到了用户注册事件，给用户发送改名卡
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(),
                IdempotentEnum.UID, user.getId().toString());
    }


    /**
     * 订阅者：监听事件，新用户注册发放徽章
     *
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendBadge(UserRegisterEvent event) {
        User user = event.getUser();
        int registeredCount = userDao.count();
        Long itemId;
        // 前十名
        if (registeredCount < TOP_10){
            // 监听到了用户注册事件，给用户发送前10名徽章
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(),
                    IdempotentEnum.UID, user.getId().toString());
        } else if (registeredCount < TOP_100){
            // 监听到了用户注册事件，给用户发送前100名徽章
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(),
                    IdempotentEnum.UID, user.getId().toString());
        }
    }



}
