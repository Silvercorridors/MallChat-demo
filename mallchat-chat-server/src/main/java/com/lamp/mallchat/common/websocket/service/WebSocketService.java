package com.lamp.mallchat.common.websocket.service;

import io.netty.channel.Channel;

/**
 * @author qyxmzg
 * @date 2023/10/2 0:00
 * @description WebSocket服务类
 */
public interface WebSocketService {

    void connect(Channel channel);

    void handleLoginReq(Channel channel);

    void remove(Channel channel);

    /**
     * 扫码登录成功消息推送
     * @param code
     * @param id
     */
    void scanLoginSuccess(Integer code, Long id);

    /**
     * 等待授权消息推送
     * @param code
     */
    void waitAuthorize(Integer code);

    /**
     * 解析前端携带的token，进行授权
     * @param channel
     * @param data
     */
    void authorize(Channel channel, String data);
}
