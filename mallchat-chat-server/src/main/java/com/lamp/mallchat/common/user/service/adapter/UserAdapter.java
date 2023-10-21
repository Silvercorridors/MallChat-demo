package com.lamp.mallchat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.lamp.mallchat.common.common.constants.YesOrNoEnum;
import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.vo.resp.BadgesResp;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author silverCorridors
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

    public static List<BadgesResp> buildBadgeResp(List<ItemConfig> badgesList,
                                                  List<UserBackpack> backpacks, User user) {
        // 用户已拥有的徽章id set
        Set<Long> obtainItemSets = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return badgesList.stream().map(a -> {
                    BadgesResp resp = new BadgesResp();
                    BeanUtil.copyProperties(a, resp);
                    resp.setObtain(obtainItemSets.contains(a.getId()) ?
                            YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    resp.setWearing(Objects.equals(a.getId(), user.getItemId()) ?
                            YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
                    return resp;
                }).sorted(Comparator.comparing(BadgesResp::getWearing, Comparator.reverseOrder())
                        .thenComparing(BadgesResp::getObtain, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
