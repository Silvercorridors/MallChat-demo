package com.lamp.mallchat.common.websocket.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 记录和前端连接的一些映射信息
 * Author: lamper
 * Date: 2023-03-21
 */
@Data
public class WSChannelExtraDTO {
    /**
     * 前端如果登录了，记录uid
     */
    private Long uid;
}
