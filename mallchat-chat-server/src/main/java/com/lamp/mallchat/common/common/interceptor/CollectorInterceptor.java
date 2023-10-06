package com.lamp.mallchat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.lamp.mallchat.common.common.domain.dto.RequestInfo;
import com.lamp.mallchat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author qyxmzg
 * @date 2023/10/5 21:00
 * @description 收集器(收集所有请求需要的参数, 放到ThreadLocal里存储)
 */
@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(null);
        String ip = ServletUtil.getClientIP(request);
        RequestHolder.set(new RequestInfo().setUid(uid).setIp(ip));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
