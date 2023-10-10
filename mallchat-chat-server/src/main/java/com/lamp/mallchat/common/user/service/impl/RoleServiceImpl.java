package com.lamp.mallchat.common.user.service.impl;

import com.lamp.mallchat.common.user.domain.enums.RoleEnum;
import com.lamp.mallchat.common.user.service.RoleService;
import com.lamp.mallchat.common.user.service.cache.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author qyxmzg
 * @date 2023/10/8 23:28
 * @description 角色服务实现类
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserCache userCache;

    /**
     * 是否拥有权限
     * @param uid 用户id
     * @param roleEnum 角色枚举类
     * @return
     */
    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSet = userCache.getRoleByUid(uid);
        return isAdmin(roleSet) || roleSet.contains(roleEnum.getId());
    }

    /**
     * roleSet中是否含有超级管理员
     * @param roleSet
     * @return
     */
    private boolean isAdmin(Set<Long> roleSet){
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }

}
