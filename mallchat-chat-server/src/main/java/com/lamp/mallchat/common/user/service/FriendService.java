package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.user.domain.entity.UserFriend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lamp.mallchat.common.user.domain.vo.req.FriendCheckReq;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendCheckResp;

/**
 * <p>
 * 用户联系人表 服务类
 * </p>
 *
 * @author lamper
 * @since 2023-10-19
 */
public interface FriendService {


    FriendCheckResp check(Long uid, FriendCheckReq request);
}
