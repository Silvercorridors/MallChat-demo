package com.lamp.mallchat.common.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 * @author qyxmzg
 */
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    /**
     * 使用 caffeine 替代spring 缓存管理, 底层是 ConcurrentHashMap
     * @return
     */
    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 方案一(常用)：定制化缓存Cache
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 每五分钟过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 最初大小
                .initialCapacity(100)
                // 最大大小（只缓存两百条）
                .maximumSize(200));
        return cacheManager;
    }

}