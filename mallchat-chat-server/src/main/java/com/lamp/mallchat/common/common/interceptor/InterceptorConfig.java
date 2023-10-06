package com.lamp.mallchat.common.common.interceptor;

import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qyxmzg
 * @date 2023/10/5 20:35
 * @description Spring拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private CollectorInterceptor collectorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * token解析拦截器
         */
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/capi/**");
        /**
         * 请求信息收集器
         */
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/capi/**");
        ;
    }
}
