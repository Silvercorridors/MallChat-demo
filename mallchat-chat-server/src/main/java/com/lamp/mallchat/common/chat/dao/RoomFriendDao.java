package com.lamp.mallchat.common.chat.dao;

import com.lamp.mallchat.common.chat.domain.entity.RoomFriend;
import com.lamp.mallchat.common.chat.mapper.RoomFriendMapper;
import com.lamp.mallchat.common.chat.service.IRoomFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单聊房间表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-29
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend> implements IRoomFriendService {

}
