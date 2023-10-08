package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.user.domain.entity.Role;
import com.lamp.mallchat.common.user.mapper.RoleMapper;
import com.lamp.mallchat.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-08
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
