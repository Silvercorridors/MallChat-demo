package com.lamp.mallchat.common.user.controller;


import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import com.lamp.mallchat.common.common.utils.AssertUtil;
import com.lamp.mallchat.common.common.utils.RequestHolder;
import com.lamp.mallchat.common.user.domain.enums.RoleEnum;
import com.lamp.mallchat.common.user.domain.vo.req.user.BlackReq;
import com.lamp.mallchat.common.user.domain.vo.req.user.ModifyNameReq;
import com.lamp.mallchat.common.user.domain.vo.req.user.WearingBadgeReq;
import com.lamp.mallchat.common.user.domain.vo.resp.user.BadgesResp;
import com.lamp.mallchat.common.user.domain.vo.resp.user.UserInfoResp;
import com.lamp.mallchat.common.user.service.RoleService;
import com.lamp.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lamper
 * @since 2023-09-24
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    /**
     * 获取用户信息，个人信息
     *
     * @return
     */
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息, 个人信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    /**
     * 修改用户名
     *
     * @return
     */
    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq) {
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgesResp>> badges() {
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq wearingBadgeReq) {
        userService.wearingBadge(RequestHolder.get().getUid(), wearingBadgeReq.getItemId());
        return ApiResult.success();
    }

    @PutMapping("/black")
    @ApiOperation("拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody BlackReq req) {
        Long uid = RequestHolder.get().getUid();
        boolean hasPower = roleService.hasPower(uid, RoleEnum.ADMIN);
        AssertUtil.isTrue(hasPower, "没有权限");
        userService.black(req);
        return ApiResult.success();
    }


}

