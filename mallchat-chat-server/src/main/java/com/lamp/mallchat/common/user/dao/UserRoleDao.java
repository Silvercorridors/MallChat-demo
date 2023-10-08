package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.user.domain.entity.UserRole;
import com.lamp.mallchat.common.user.mapper.UserRoleMapper;
import com.lamp.mallchat.common.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-08
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
