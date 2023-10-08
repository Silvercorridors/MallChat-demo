package com.lamp.mallchat.common.websocket.service.handler;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.websocket.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * @author qyxmzg
 * @date 2023/10/4 23:17
 * @description 自定义握手协议处理器
 */
public class MyHeaderCollectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
            Optional<String> tokenOptional = Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k -> k.get("token"))
                    .map(k -> k.toString());
            // 如果token存在
            tokenOptional.ifPresent(s -> NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, s));
            // 移除后面拼接的所有参数
            request.setUri(urlBuilder.getPath().toString());
            // 取用户ip
            String ip = request.headers().get("X-Real-IP");
            // 若ip不存在, 取远端ip
            if (StrUtil.isBlank(ip)){
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            // 保存到channel附件
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
            // 保存到channel, 可以扔掉了, 执行器只用一次
            ctx.pipeline().remove(this);
        }
        ctx.fireChannelRead(msg);
    }
}
