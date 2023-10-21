package com.lamp.mallchat.common.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.lamp.mallchat.common.common.domain.dto.RequestInfo;
import com.lamp.mallchat.common.common.exception.HttpErrorEnum;
import com.lamp.mallchat.common.common.utils.RequestHolder;
import com.lamp.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.lamp.mallchat.common.user.service.cache.UserCache;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author silverCorridors
 * @date 2023/10/10 21:55
 * @description 拉黑拦截器
 */
@Component
public class BlackInterceptor implements HandlerInterceptor {
    @Resource
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 按照类型获取被拉黑的黑名单
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        // 判断当前用户在不在黑名单中
        RequestInfo requestInfo = RequestHolder.get();
        boolean isBlackUid = inBlack(requestInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()));
        boolean isBlackIp = inBlack(requestInfo.getIp(), blackMap.get(BlackTypeEnum.IP.getType()));
        if (isBlackUid || isBlackIp){
            // 在黑名单中，拦截用户
            HttpErrorEnum.ACCESS_DENIED.sendHttpEnum(response);
            return false;
        }
        return true;
    }

    private boolean inBlack(Object target, Set<String> set) {
        if (Objects.isNull(target) || CollectionUtil.isEmpty(set)){
            return false;
        }
        return set.contains(target.toString());
    }
}
