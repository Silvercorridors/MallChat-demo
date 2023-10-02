package com.lamp.mallchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.lamp.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.lamp.mallchat.common.websocket.domain.vo.req.WSBaseReq;
import com.lamp.mallchat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private WebSocketService webSocketService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        webSocketService = SpringUtil.getBean(WebSocketService.class);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
    }

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
                userOffLine(ctx);
                // todo: 用户下线

            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 用户下线
     * @param ctx
     */
    private void userOffLine(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        webSocketService.remove(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        Channel channel = channelHandlerContext.channel();
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                System.out.println("请求二维码");
                webSocketService.handleLoginReq(channel);
                break;
            default:
                break;
        }
    }
}
