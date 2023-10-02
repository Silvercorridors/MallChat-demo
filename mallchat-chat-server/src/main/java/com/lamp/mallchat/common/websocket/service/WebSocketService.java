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
}
