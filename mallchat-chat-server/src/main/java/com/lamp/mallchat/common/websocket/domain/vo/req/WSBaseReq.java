package com.lamp.mallchat.common.websocket.domain.vo.req;

import lombok.Data;

/**
 * @author silverCorridors
 * @date 2023/9/23 19:42
 * @description webSocket基本请求类
 */
@Data
public class WSBaseReq {
    /**
     * @see com.lamp.mallchat.common.websocket.domain.enums.WSReqTypeEnum
     */
    private Integer type;

    private String data;

}
