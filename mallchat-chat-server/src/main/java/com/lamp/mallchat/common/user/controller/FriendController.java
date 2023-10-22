package com.lamp.mallchat.common.user.controller;


import com.lamp.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import com.lamp.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.lamp.mallchat.common.common.utils.RequestHolder;
import com.lamp.mallchat.common.user.domain.vo.req.FriendCheckReq;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendCheckResp;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendResp;
import com.lamp.mallchat.common.user.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/page")
    @ApiOperation("联系人列表")
    public ApiResult<CursorPageBaseResp<FriendResp>> friendList(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.friendList(uid, request));
    }




}

