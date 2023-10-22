package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.req.PageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.lamp.mallchat.common.common.domain.vo.resp.PageBaseResp;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendApproveReq;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendCheckReq;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendCheckResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendUnreadResp;

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

    /**
     * 应用
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 同意好友申请
     * @param uid uid
     * @param request 请求
     */
    void applyApprove(Long uid, FriendApproveReq request);

    /**
     * 分页查询好友申请
     * @param uid uid
     * @param request 请求
     * @return
     */
    PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request);
    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    FriendUnreadResp unread(Long uid);

    /**
     * 删除好友
     * @param uid uid
     * @param friendUid 好友id
     */
    void deleteFriend(Long uid, Long friendUid);
}
