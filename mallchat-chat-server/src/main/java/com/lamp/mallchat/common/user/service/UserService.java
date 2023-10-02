package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lamper
 * @since 2023-09-24
 */
public interface UserService {

    /**
     * 用户注册逻辑
     * @param newUser 新用户
     * @return
     */
    Long registry(User newUser);
}
