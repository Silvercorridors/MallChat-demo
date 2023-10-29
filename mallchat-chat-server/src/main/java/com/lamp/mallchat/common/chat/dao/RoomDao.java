package com.lamp.mallchat.common.chat.dao;

import com.lamp.mallchat.common.chat.domain.entity.Room;
import com.lamp.mallchat.common.chat.mapper.RoomMapper;
import com.lamp.mallchat.common.chat.service.IRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-29
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> implements IRoomService {

}
