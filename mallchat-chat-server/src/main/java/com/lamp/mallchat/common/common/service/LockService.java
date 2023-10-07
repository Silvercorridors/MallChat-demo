package com.lamp.mallchat.common.common.service;

import com.lamp.mallchat.common.common.exception.BusinessException;
import com.lamp.mallchat.common.common.exception.CommonErrorEnum;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author qyxmzg
 * @date 2023/10/7 10:44
 * @description 分布式锁服务
 */
@Service
public class LockService {

    @Resource
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit timeUnit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(key);
        boolean isSuccess = lock.tryLock(waitTime, timeUnit);
        if (!isSuccess) {
            throw new BusinessException(CommonErrorEnum.SYSTEM_ERROR);
        }
        try {
            // 回调函数
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }


    @SneakyThrows
    public <T> T executeWithLock(String key, Supplier<T> supplier) {
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, supplier);
    }

    public <T> T executeWithLock(String key, Runnable runnable) {
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, () -> {
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface Supplier<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }



}
