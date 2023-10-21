package com.lamp.mallchat.common.websocket.domain.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author silverCorridors
 * @date 2023/9/23 19:46
 * @description webSocket基本返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSBaseResp<T> {

    /**
     * @see com.lamp.mallchat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;

    private T data;



}
