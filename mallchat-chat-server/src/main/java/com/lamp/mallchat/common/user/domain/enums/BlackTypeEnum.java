package com.lamp.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 拉黑类型枚举
 * @author qyxmzg
 */
@AllArgsConstructor
@Getter
public enum BlackTypeEnum {
    /**
     * IP封禁
     */
    IP(1),
    /**
     * 用户id封禁
     */
    UID(2),
    ;

    private final Integer type;

}
