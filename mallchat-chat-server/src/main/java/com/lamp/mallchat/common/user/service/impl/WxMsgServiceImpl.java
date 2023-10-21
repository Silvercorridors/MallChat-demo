package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.service.UserService;
import com.lamp.mallchat.common.user.service.WxMsgService;
import com.lamp.mallchat.common.user.service.adapter.TextBuilder;
import com.lamp.mallchat.common.user.service.adapter.UserAdapter;
import com.lamp.mallchat.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author silverCorridors
 * @date 2023/10/2 15:06
 * @description wx消息服务实现类
 */
@Service
@Slf4j
public class WxMsgServiceImpl implements WxMsgService {
    /**
     * openId 和 登录code的关系map
     */
    private static final ConcurrentHashMap<String, Integer>  WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;

    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=%s&" +
            "redirect_uri=%s&" +
            "response_type=code&" +
            "scope=snsapi_userinfo&" +
            "state=STATE#wechat_redirect";

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private WebSocketService webSocketService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage, WxMpService wxMpService) {
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)){
            return null;
        }
        // 注册用户
        User user = userDao.getByOpenId(openId);
        // 如果用户已经注册并授权，直接登录
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar());
        if (authorized){
            // 登录成功的逻辑，给channel推送消息,
            // 通过code找到channel，给channel推送消息
            webSocketService.scanLoginSuccess(code, user.getId());
        }
        // 未注册, 走注册
        if (!registered){
            User newUser = UserAdapter.buildUserSave(openId);
            userService.registry(newUser);
        }
        // 没有授权，等待授权
        WAIT_AUTHORIZE_MAP.put(openId, code);
        // 将等待授权消息发送给前端
        webSocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(),
                URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        return TextBuilder.build("请点击登录:<a href= \"" + authorizeUrl + "\">登录</a>", wxMpXmlMessage);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenId(openid);
        // 更新用户信息
        if (StrUtil.isBlank(user.getAvatar())){
            fillUserInfo(user.getId(), userInfo);
        }
        // 获取code, 通过登录码找到channel， 进行登录
        Integer code = WAIT_AUTHORIZE_MAP.get(openid);
        webSocketService.scanLoginSuccess(code, user.getId());
    }

    private void fillUserInfo(Long id, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(id, userInfo);
        userDao.updateById(user);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.parseInt(code);
        } catch (Exception e) {
            log.error("getEventKey error eventKey: {}", wxMpXmlMessage.getEventKey());
            return null;
        }
    }
}
