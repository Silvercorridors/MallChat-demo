package com.lamp.mallchat.common.websocket.domain.enums;

import com.lamp.mallchat.common.websocket.domain.vo.resp.WSBlack;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSFriendApply;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSMemberChange;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSMessage;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSMsgMark;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSMsgRecall;
import com.lamp.mallchat.common.websocket.domain.vo.resp.WSOnlineOfflineNotify;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qyxmzg
 * @date 2023/9/23 19:46
 * @description webSocket返回类型枚举类
 */
@AllArgsConstructor
@Getter
public enum WSRespTypeEnum {
    LOGIN_URL(1, "登录二维码返回", WSLoginUrl.class),
    LOGIN_SCAN_SUCCESS(2, "用户扫描成功等待授权", null),
    LOGIN_SUCCESS(3, "用户登录成功返回用户信息", WSLoginSuccess.class),
    MESSAGE(4, "新消息", WSMessage.class),
    ONLINE_OFFLINE_NOTIFY(5, "上下线通知", WSOnlineOfflineNotify.class),
    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),
    BLACK(7, "拉黑用户", WSBlack.class),
    MARK(8, "消息标记", WSMsgMark.class),
    RECALL(9, "消息撤回", WSMsgRecall.class),
    APPLY(10, "好友申请", WSFriendApply.class),
    MEMBER_CHANGE(11, "成员变动", WSMemberChange.class),
    ;

    private final Integer type;
    private final String desc;
    private final Class dataClass;

    private static Map<Integer, WSRespTypeEnum> cache;

    static {
        cache = Arrays.stream(WSRespTypeEnum.values()).collect(Collectors.toMap(WSRespTypeEnum::getType, Function.identity()));
    }

    public static WSRespTypeEnum of(Integer type) {
        return cache.get(type);
    }
}