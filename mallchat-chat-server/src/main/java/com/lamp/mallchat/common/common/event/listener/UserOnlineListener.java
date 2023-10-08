package com.lamp.mallchat.common.common.event.listener;

import com.lamp.mallchat.common.common.event.UserOnlineEvent;
import com.lamp.mallchat.common.common.event.UserRegisterEvent;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.enums.IdempotentEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemEnum;
import com.lamp.mallchat.common.user.domain.enums.UserActiveStatusEnum;
import com.lamp.mallchat.common.user.service.IpService;
import com.lamp.mallchat.common.user.service.UserBackpackService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author qyxmzg
 * @date 2023/10/7 17:18
 * @description 用户上线事件监听器
 */
@Component
public class UserOnlineListener {


    @Resource
    private UserDao userDao;
    @Resource
    private IpService ipService;


    /**
     * 订阅者：监听事件，用户上线时更新用户信息
     * 异步执行
     * fallbackExecution：没有事务是否回调此方法
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class,
            phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void saveDb(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        // 用户状态
        update.setStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // 用户ip详情的解析
        ipService.refreshIpDetailAsync(user.getId());

    }






}
