package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.common.constants.YesOrNoEnum;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-09-24
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User>{

    public User getByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId, openId).one();
    }

    public User getByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();
    }

    public boolean modifyName(Long uid, String name) {
        return lambdaUpdate().eq(User::getId, uid)
                .set(User::getName, name)
                .update();

    }

    public boolean wearingBadge(Long uid, Long itemId) {
        return lambdaUpdate().eq(User::getId, uid)
                .set(User::getItemId, itemId)
                .update();
    }

    public void invalidByUid(Long id) {
        lambdaUpdate().eq(User::getId, id)
                .set(User::getStatus, YesOrNoEnum.YES.getStatus()).update();
    }

    public List<User> getFriendList(List<Long> uids) {
        return lambdaQuery()
                .in(User::getId, uids)
                .select(User::getId, User::getActiveStatus, User::getName, User::getAvatar)
                .list();
    }
}
