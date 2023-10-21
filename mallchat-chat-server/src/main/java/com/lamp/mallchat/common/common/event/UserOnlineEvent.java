package com.lamp.mallchat.common.common.event;

import com.lamp.mallchat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author silverCorridors
 * @date 2023/10/7 23:00
 * @description 用户上线事件
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {

    private User user;

    /**
     * 构造函数
     * @param source 事件的发布者
     * @param user 用户
     */
    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

}
