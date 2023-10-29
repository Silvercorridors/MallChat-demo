package com.lamp.mallchat.common.common.event;

import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.enums.ChatActiveStatusEnum;
import com.lamp.mallchat.common.user.service.IpService;
import com.lamp.mallchat.common.user.service.cache.UserCache;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

/**
 * @author silverCorridors
 * @date 2023/10/7 23:00
 * @description 用户上线事件
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {

    private User user;
    @Resource
    private IpService ipService;
    @Resource
    private UserCache userCache;
    @Resource
    private UserDao userDao;
    /**
     * 构造函数
     * @param source 事件的发布者
     * @param user 用户
     */
    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        User user = event.getUser();
        // 上线事件：将用户在缓存中放入online列表，并移除offline列表
        userCache.online(user.getId(), user.getLastOptTime());
        // TODO: 给所有在线用户推送消息，该用户登录成功
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        // 用户上线事件：更新用户信息
        userDao.updateById(update);
        //更新用户ip详情（异步刷新用户ip详情）
        ipService.refreshIpDetailAsync(user.getId());
    }


}
