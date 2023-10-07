package com.lamp.mallchat.common.common.event;

import com.lamp.mallchat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author qyxmzg
 * @date 2023/10/7 17:14
 * @description 用户注册事件类
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private User user;

    /**
     * 构造函数
     * @param source 事件的发布者
     * @param user 新用户
     */
    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
