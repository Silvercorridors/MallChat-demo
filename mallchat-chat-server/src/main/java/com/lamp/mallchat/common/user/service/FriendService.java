package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.lamp.mallchat.common.user.domain.vo.req.FriendCheckReq;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendCheckResp;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendResp;

/**
 * <p>
 * 用户联系人表 服务类
 * </p>
 *
 * @author lamper
 * @since 2023-10-19
 */
public interface FriendService {

    /**
     * 检查
     * 检查是否是自己好友
     *
     * @param request 请求
     * @param uid     uid
     * @return {@link FriendCheckResp}
     */
    FriendCheckResp check(Long uid, FriendCheckReq request);

    /**
     * 好友列表
     * @param uid uid
     * @param request 分页请求
     * @return {@link FriendResp}
     */
    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);
}
