package com.lamp.mallchat.common.common.exception;

import lombok.Data;

/**
 * @author qyxmzg
 * @date 2023/10/6 15:24
 * @description 业务异常
 */
@Data
public class BusinessException extends RuntimeException{

    protected Integer errorCode;

    protected String errorMsg;

    public BusinessException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode,String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


}
