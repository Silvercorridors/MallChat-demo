package com.lamp.mallchat.common.user.controller;


import com.lamp.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.req.PageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import com.lamp.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.lamp.mallchat.common.common.domain.vo.resp.PageBaseResp;
import com.lamp.mallchat.common.common.utils.RequestHolder;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendApproveReq;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendCheckReq;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendDeleteReq;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendCheckResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendUnreadResp;
import com.lamp.mallchat.common.user.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 用户联系人表 前端控制器
 * </p>
 *
 * @author lamper
 * @since 2023-10-19
 */
@RestController
@RequestMapping("/capi/user/friend")
@Api(tags = "好友相关接口")
@Slf4j
public class FriendController {

    @Resource
    private FriendService friendService;


    @GetMapping("/check")
    @ApiOperation("批量判断是否是自己好友")
    public ApiResult<FriendCheckResp> check(@Valid FriendCheckReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.check(uid, request));
    }

    @PostMapping("/apply")
    @ApiOperation("申请好友")
    public ApiResult<Void> apply(@Valid @RequestBody FriendApplyReq request) {
        Long uid = RequestHolder.get().getUid();
        friendService.apply(uid, request);
        return ApiResult.success();
    }

    @DeleteMapping()
    @ApiOperation("删除好友")
    public ApiResult<Void> delete(@Valid @RequestBody FriendDeleteReq request) {
        Long uid = RequestHolder.get().getUid();
        friendService.deleteFriend(uid, request.getTargetUid());
        return ApiResult.success();
    }

    @GetMapping("/apply/page")
    @ApiOperation("好友申请列表--普通分页")
    public ApiResult<PageBaseResp<FriendApplyResp>> page(@Valid PageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.pageApplyFriend(uid, request));
    }

    @GetMapping("/apply/unread")
    @ApiOperation("申请未读数")
    public ApiResult<FriendUnreadResp> unread() {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.unread(uid));
    }

    @PutMapping("/apply")
    @ApiOperation("审批同意")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApproveReq request) {
        friendService.applyApprove(RequestHolder.get().getUid(), request);
        return ApiResult.success();
    }

    @GetMapping("/page")
    @ApiOperation("联系人列表--游标分页")
    public ApiResult<CursorPageBaseResp<FriendResp>> friendList(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.friendList(uid, request));
    }


}

