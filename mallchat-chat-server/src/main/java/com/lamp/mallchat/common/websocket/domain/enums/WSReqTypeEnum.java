package com.lamp.mallchat.common.websocket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author silverCorridors
 * @date 2023/9/23 19:42
 * @description webSocket请求类型枚举
 */
@AllArgsConstructor
@Getter
public enum WSReqTypeEnum {
    /**
     * 请求登录二维码
     */
    LOGIN(1, "请求登录二维码"),
    /**
     * 心跳检测
     */
    HEARTBEAT(2, "心跳包"),
    /**
     * 登录认证
     */
    AUTHORIZE(3, "登录认证"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WSReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSReqTypeEnum.values()).collect(Collectors.toMap(WSReqTypeEnum::getType, Function.identity()));
    }

    public static WSReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
