package com.lamp.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author silverCorridors
 * @description : 申请类型枚举
 * @date : 2023/10/22
 */
@Getter
@AllArgsConstructor
public enum ApplyTypeEnum {
    /**
     * 加好友
     */
    ADD_FRIEND(1, "加好友");


    private final Integer code;

    private final String desc;
}