package com.lamp.mallchat.common.user.service.adapter;

import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserApply;
import com.lamp.mallchat.common.user.domain.entity.UserFriend;
import com.lamp.mallchat.common.user.domain.enums.ApplyReadStatusEnum;
import com.lamp.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.lamp.mallchat.common.user.domain.enums.ApplyTypeEnum;
import com.lamp.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import com.lamp.mallchat.common.user.domain.vo.resp.friend.FriendResp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author silverCorridors
 * @date 2023/10/22 0:13
 * @description
 */
public class FriendAdapter {
    public static List<FriendResp> buildFriend(List<UserFriend> list, List<User> userList) {
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        return list.stream().map(userFriend -> {
            FriendResp resp = new FriendResp();
            resp.setUid(userFriend.getFriendUid());
            User user = userMap.get(userFriend.getFriendUid());
            if (Objects.nonNull(user)) {
                resp.setActiveStatus(user.getActiveStatus());
            }
            return resp;
        }).collect(Collectors.toList());
    }

    public static UserApply buildFriendApply(Long uid, FriendApplyReq request) {
        UserApply userApplyNew = new UserApply();
        userApplyNew.setUid(uid);
        userApplyNew.setMsg(request.getMsg());
        userApplyNew.setType(ApplyTypeEnum.ADD_FRIEND.getCode());
        userApplyNew.setTargetId(request.getTargetUid());
        userApplyNew.setStatus(ApplyStatusEnum.WAIT_APPROVAL.getCode());
        userApplyNew.setReadStatus(ApplyReadStatusEnum.UNREAD.getCode());
        return userApplyNew;
    }

    public static List<FriendApplyResp> buildFriendApplyList(List<UserApply> records) {
        return records.stream().map(userApply -> {
            FriendApplyResp friendApplyResp = new FriendApplyResp();
            friendApplyResp.setUid(userApply.getUid());
            friendApplyResp.setType(userApply.getType());
            friendApplyResp.setApplyId(userApply.getId());
            friendApplyResp.setMsg(userApply.getMsg());
            friendApplyResp.setStatus(userApply.getStatus());
            return friendApplyResp;
        }).collect(Collectors.toList());
    }
}
