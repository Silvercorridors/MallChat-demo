package com.lamp.mallchat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * @author qyxmzg
 * @date 2023/10/2 15:21
 * @description 用户Adapter
 */
public class UserAdapter {
    public static User buildUserSave(String openId) {
        return User.builder().openId(openId).build();
    }

    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        return User.builder()
                .id(id)
                .name(userInfo.getNickname())
                .avatar(userInfo.getHeadImgUrl()).build();
    }

    public static UserInfoResp buildUserInfo(User user, Integer modifyNameCount) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(user, userInfoResp);
        userInfoResp.setModifyNameChance(modifyNameCount);
        return userInfoResp;
    }
}
