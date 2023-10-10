package com.lamp.mallchat.common.common.interceptor;

import com.lamp.mallchat.common.common.exception.HttpErrorEnum;
import com.lamp.mallchat.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author qyxmzg
 * @date 2023/10/5 19:29
 * @description token解析拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";
    public static final String UID = "uid";

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        // 用户有登录态
        if (Objects.nonNull(validUid)){
            request.setAttribute(UID, validUid);
        } else {
            // 用户未登录, 拦截, 返回401
            boolean isPublicUri = containPublicUri(request);
            if (!isPublicUri){
                // 401
                HttpErrorEnum.ACCESS_DENIED.sendHttpEnum(response);
                return false;
            }
        }
        return true;
    }

    private boolean containPublicUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String[] split = requestUri.split("/");
        return split.length > 3 && "public".equals(split[3]);
    }

    /**
     * 从 request 从获取token
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIZATION_SCHEMA))
                .map(h -> h.replaceFirst(AUTHORIZATION_SCHEMA, ""))
                .orElse(null);
    }


}
