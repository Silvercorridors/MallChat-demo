package com.lamp.mallchat.common.websocket;

import cn.hutool.json.JSONUtil;
import com.lamp.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.lamp.mallchat.common.websocket.domain.vo.req.WSBaseReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 握手事件
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            log.info("握手完成");
        }
        // 心跳事件
        else if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            // 读空闲事件
            if (event.state() == IdleState.READER_IDLE){
                // 读到了空闲，关闭连接, 下线
                System.out.println("读空闲");
                // todo: 用户下线

            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                System.out.println("请求二维码");
                channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("二維碼："));
                break;
            default:
                break;
        }
    }
}
