package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.enums.ItemEnum;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.lamp.mallchat.common.user.service.UserService;
import com.lamp.mallchat.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author qyxmzg
 * @date 2023/10/2 15:25
 * @description 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserBackpackDao userBackpackDao;

    @Override
    public Long registry(User newUser) {
        userDao.save(newUser);
        // todo 用户注册事件
        return newUser.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        if (Objects.isNull(user)){
            return null;
        }
        Integer modifyNameCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user, modifyNameCount);
    }

    @Override
    public void modifyName(Long uid, String name) {
    }
}
