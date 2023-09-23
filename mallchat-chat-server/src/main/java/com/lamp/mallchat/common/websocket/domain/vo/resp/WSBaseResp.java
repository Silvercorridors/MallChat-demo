package com.lamp.mallchat.common.websocket.domain.vo.resp;

/**
 * @author qyxmzg
 * @date 2023/9/23 19:46
 * @description webSocket基本返回类
 */
public class WSBaseResp<T> {

    /**
     * @see com.lamp.mallchat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;

    private T data;



}
