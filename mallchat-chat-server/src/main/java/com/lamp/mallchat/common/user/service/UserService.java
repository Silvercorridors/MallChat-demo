package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;

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

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 修改用户名
     * @param uid
     * @param name
     */
    void modifyName(Long uid, String name);
}
