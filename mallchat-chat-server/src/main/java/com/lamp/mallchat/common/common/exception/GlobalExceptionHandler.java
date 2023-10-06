package com.lamp.mallchat.common.common.exception;

import cn.hutool.core.text.StrPool;
import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qyxmzg
 * @date 2023/10/5 22:04
 * @description 全局异常获取
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(ex -> {
            errorMsg.append(ex.getField())
                    .append(ex.getDefaultMessage())
                    .append(StrPool.COMMA);
        });
        String message = errorMsg.toString();
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), message.substring(0, message.length() - 1));
    }

    /**
     * 兜底，所有异常的拦截
     * @param e
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ApiResult<?> throwable(Throwable e){
        log.error("system exception! The reason is: {}", e.getMessage());
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }


}
