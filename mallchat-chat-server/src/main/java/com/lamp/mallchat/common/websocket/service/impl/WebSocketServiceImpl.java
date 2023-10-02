package com.lamp.mallchat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.service.LoginService;
import com.lamp.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.lamp.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import com.lamp.mallchat.common.websocket.service.WebSocketService;
import com.lamp.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qyxmzg
 * @date 2023/10/2 0:01
 * @description WebSocket服务实现类
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Resource
    private WxMpService wxMpService;

    @Resource
    private UserDao userDao;

    @Resource
    private LoginService loginService;

    /**
     * 管理所有在线用户的连接(登录态 / 游客)
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    public static final Duration DURATION = Duration.ofHours(1);
    public static final int MAXIMUM_SIZE = 10000;
    /**
     * 等待登录用户的连接 (临时保存登录码和channel的映射关系)
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @SneakyThrows
    @Override
    public void handleLoginReq(Channel channel) {
        // 生成随机码
        Integer code = generateLoginCode(channel);
        // 找微信生成带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService()
                .qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        channel.close();
        // todo: 用户下线

    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        // 确认channel链接存在
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)){
            return;
        }
        // 不从外面传user，保证user信息是最新的
        User user = userDao.getById(uid);
        // 从WAIT_LOGIN_MAP移除code
        WAIT_LOGIN_MAP.invalidate(code);
        // todo 调用登录模块获取token
        String token = loginService.login(uid);
        // 用户登录
        sendMsg(channel, WebSocketAdapter.buildResp(user, token));
    }

    @Override
    public void waitAuthorize(Integer code) {
        // 确认channel链接存在
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)){
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizeResp());
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }
}
