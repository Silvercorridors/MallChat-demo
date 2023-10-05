package com.lamp.mallchat.common.user.controller;


import com.lamp.mallchat.common.common.domain.vo.resp.ApiResult;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 获取用户信息，个人信息
     * @param uid 用户id
     * @return
     */
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息, 个人信息")
    public ApiResult<UserInfoResp> getUserInfo(@RequestParam Long uid){



        return null;
    }

}

