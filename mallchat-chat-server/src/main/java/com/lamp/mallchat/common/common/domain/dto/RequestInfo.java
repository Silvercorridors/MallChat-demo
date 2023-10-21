package com.lamp.mallchat.common.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author silverCorridors
 * @date 2023/10/5 21:09
 * @description 请求信息
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RequestInfo {
    private Long uid;

    private String ip;
}
