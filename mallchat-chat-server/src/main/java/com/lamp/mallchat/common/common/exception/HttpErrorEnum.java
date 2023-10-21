package com.lamp.mallchat.common.common.exception;

import cn.hutool.http.ContentType;
import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import com.lamp.mallchat.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author silverCorridors
 * @date 2023/10/5 19:45
 * @description HttpError枚举类
 */
@AllArgsConstructor
@Getter
public enum HttpErrorEnum implements ErrorEnum{
    /**
     * 登录失效
     */
    ACCESS_DENIED(401, "登录失效，请重新登录");
    /**
     * http状态码
     */
    private Integer httpCode;
    /**
     * 描述信息
     */
    private String desc;

    @Override
    public Integer getErrorCode() {
        return httpCode;
    }

    @Override
    public String getErrorMsg() {
        return desc;
    }

    /**
     * 返回http异常信息
     * @param response
     * @throws IOException
     */
    public void sendHttpEnum(HttpServletResponse response) throws IOException {
        // 401
        response.setStatus(getErrorCode());
        response.setContentType(ContentType.JSON.toString(StandardCharsets.UTF_8));
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(getErrorCode(), getErrorMsg())));
    }


}
