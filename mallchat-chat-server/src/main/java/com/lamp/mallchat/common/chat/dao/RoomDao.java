package com.lamp.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lamp.mallchat.common.chat.domain.entity.Room;
import com.lamp.mallchat.common.chat.mapper.RoomMapper;
import com.lamp.mallchat.common.chat.service.RoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-29
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> implements IService<Room> {
    public void refreshActiveTime(Long roomId, Long msgId, Date msgTime) {
        lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getLastMsgId, msgId)
                .set(Room::getActiveTime, msgTime)
                .update();
    }
}
