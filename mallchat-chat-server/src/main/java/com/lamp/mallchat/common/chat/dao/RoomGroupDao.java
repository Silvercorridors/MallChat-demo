package com.lamp.mallchat.common.chat.dao;

import com.lamp.mallchat.common.chat.domain.entity.RoomGroup;
import com.lamp.mallchat.common.chat.mapper.RoomGroupMapper;
import com.lamp.mallchat.common.chat.service.IRoomGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊房间表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-29
 */
@Service
public class RoomGroupDao extends ServiceImpl<RoomGroupMapper, RoomGroup> implements IRoomGroupService {

}
