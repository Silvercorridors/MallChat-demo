package com.lamp.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : silverCorridors
 * @description : 申请阅读状态枚举
 * @date : 2023/10/22
 */
@Getter
@AllArgsConstructor
public enum ApplyReadStatusEnum {
    /**
     * 未读
     */
    UNREAD(1, "未读"),
    /**
     * 已读
     */
    READ(2, "已读");

    private final Integer code;

    private final String desc;
}
