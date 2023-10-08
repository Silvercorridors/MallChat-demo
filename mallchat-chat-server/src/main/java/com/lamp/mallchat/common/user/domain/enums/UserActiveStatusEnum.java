package com.lamp.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户状态枚举类
 * @author: qyxzg
 */
@AllArgsConstructor
@Getter
public enum UserActiveStatusEnum {
    /**
     * 用户状态枚举类：在线
     */
    ONLINE(1, "在线"),
    /**
     * 用户状态枚举类：离线
     */
    OFFLINE(2, "离线"),
    ;

    private final Integer status;
    private final String desc;


}