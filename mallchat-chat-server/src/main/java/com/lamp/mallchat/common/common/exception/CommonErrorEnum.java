package com.lamp.mallchat.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qyxmzg
 * @date 2023/10/5 22:12
 * @description 通用异常枚举
 */
@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{
    /**
     * 系统异常
     */
    SYSTEM_ERROR(-1, "系统繁忙，请稍后再试~"),
    /**
     * 参数校验失败
     */
    PARAM_INVALID(-2, "参数校验失败");


    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
