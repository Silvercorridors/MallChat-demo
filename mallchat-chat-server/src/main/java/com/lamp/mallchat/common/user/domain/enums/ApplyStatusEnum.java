package com.lamp.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : silverCorridors
 * @description : 申请状态枚举
 * @date : 2023/10/22
 */
@Getter
@AllArgsConstructor
public enum ApplyStatusEnum {
    /**
     * 待审批
     */
    WAIT_APPROVAL(1, "待审批"),
    /**
     * 同意
     */
    AGREE(2, "同意");

    private final Integer code;

    private final String desc;
}
