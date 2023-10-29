package com.lamp.mallchat.common.chat.service;

import com.lamp.mallchat.common.chat.domain.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lamp.mallchat.common.chat.domain.entity.RoomFriend;
import com.lamp.mallchat.common.chat.domain.entity.RoomGroup;

import java.util.List;

/**
 * <p>
 * 房间表 底层管理服务类
 * </p>
 *
 * @author lamper
 * @since 2023-10-29
 */
public interface RoomService {
    /**
     * 创建一个单聊房间
     */
    RoomFriend createFriendRoom(List<Long> uidList);

    RoomFriend getFriendRoom(Long uid1, Long uid2);

    /**
     * 禁用一个单聊房间
     */
    void disableFriendRoom(List<Long> uidList);


    /**
     * 创建一个群聊房间
     */
    RoomGroup createGroupRoom(Long uid);
}
