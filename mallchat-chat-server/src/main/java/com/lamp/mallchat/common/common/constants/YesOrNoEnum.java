package com.lamp.mallchat.common.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    /**
     * 否
     */
    NO(0, "否"),
    /**
     * 是
     */
    YES(1, "是");

    private final Integer status;
    private final String desc;
}
