package com.lamp.mallchat.common.user.service.impl;

import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qyxmzg
 * @date 2023/10/2 15:25
 * @description 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;


    @Override
    public Long registry(User newUser) {
        userDao.save(newUser);
        // todo 用户注册事件
        return newUser.getId();
    }
}
