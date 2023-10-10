package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lamp.mallchat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author lamper
 * @since 2023-10-08
 */
public interface RoleService {
    /**
     * 是否拥有某个权限
     * @param uid 用户id
     * @param roleEnum 角色枚举类
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);

}
