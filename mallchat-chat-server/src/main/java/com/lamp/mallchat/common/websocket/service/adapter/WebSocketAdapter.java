package com.lamp.mallchat.common.websocket.service.adapter;

import com.lamp.mallchat.common.common.constants.YesOrNoEnum;
import com.lamp.mallchat.common.user.domain.entity.Black;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSBlack;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * @author qyxmzg
 * @date 2023/10/2 0:31
 * @description
 */
public class WebSocketAdapter {


    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token, boolean power) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .power(power ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus())
                .build();
        resp.setData(wsLoginSuccess);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }

    public static WSBaseResp<?> buildInValidTokenResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return resp;
    }

    public static WSBaseResp<?> buildBlack(User user) {
        Long uid = user.getId();
        WSBaseResp<WSBlack> resp = new WSBaseResp<>();
        WSBlack black = WSBlack.builder().uid(uid).build();
        resp.setData(black);
        resp.setType(WSRespTypeEnum.BLACK.getType());
        return resp;
    }
}
