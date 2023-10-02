package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.mapper.UserMapper;
import com.lamp.mallchat.common.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-09-24
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> implements IUserService {

}