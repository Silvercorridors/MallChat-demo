package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.common.constants.RedisKey;
import com.lamp.mallchat.common.common.utils.JwtUtils;
import com.lamp.mallchat.common.user.service.LoginService;
import com.lamp.mallchat.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author qyxmzg
 * @date 2023/10/2 17:34
 * @description LoginServiceImpl
 */
@Service
public class LoginServiceImpl implements LoginService {

    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public boolean verify(String token) {
        return false;
    }

    /**
     * 异步刷新token
     *
     * @param token
     */
    @Async
    @Override
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        // 不存在的key
        if (expireDays == -2) {
            return;
        }
        // 续期
        if (expireDays < TOKEN_RENEWAL_DAYS) {
            RedisUtils.expire(userTokenKey, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        // 去中心化管理续期（redis）
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if (StrUtil.isBlank(oldToken)) {
            return null;
        }
        return Objects.equals(oldToken, token) ? uid : null;
    }
}
