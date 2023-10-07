package com.lamp.mallchat.common.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.common.annotation.RedissonLock;
import com.lamp.mallchat.common.common.service.LockService;
import com.lamp.mallchat.common.common.utils.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author qyxmzg
 * @date 2023/10/7 11:17
 * @description 分布式锁切面类
 */
@Slf4j
@Component
@Aspect
@Order(0) //确保比事务注解先执行，分布式锁在事务外
public class RedissonLockAspect {

    @Autowired
    private LockService lockService;

    @Around("@annotation(com.lamp.mallchat.common.common.annotation.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint)  {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? SpElUtils.getMethodKey(method) : redissonLock.prefixKey();//默认方法限定名+注解排名（可能多个）
        String key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLock(prefix + ":" + key, redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }

}
