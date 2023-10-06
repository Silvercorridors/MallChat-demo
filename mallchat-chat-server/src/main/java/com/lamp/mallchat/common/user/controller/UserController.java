package com.lamp.mallchat.common.user.controller;


import com.lamp.mallchat.common.common.domain.dto.RequestInfo;
import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import com.lamp.mallchat.common.common.utils.RequestHolder;
import com.lamp.mallchat.common.user.domain.vo.req.ModifyNameReq;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.lamp.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息，个人信息
     * @return
     */
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息, 个人信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    /**
     * 修改用户名
     * @return
     */
    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq modifyNameReq){
        userService.modifyName(RequestHolder.get().getUid(), modifyNameReq.getName());
        return ApiResult.success();
    }

}

