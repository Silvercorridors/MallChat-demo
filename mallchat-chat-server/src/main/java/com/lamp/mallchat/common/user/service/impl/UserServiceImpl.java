package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.common.exception.BusinessException;
import com.lamp.mallchat.common.common.utils.AssertUtil;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.enums.ItemEnum;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.lamp.mallchat.common.user.service.UserService;
import com.lamp.mallchat.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void modifyName(Long uid, String name) {
        // 是否重名
        User oldUser = userDao.getByName(name);
        // 有重名，抛出业务异常
        AssertUtil.isEmpty(oldUser, "名字已经被抢占了, 请换一个");
        // 改名, 查看用户是否有改名卡
        UserBackpack modifyNameCard = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameCard, "改名卡次数不足, 等待后续活动~");
        // 使用改名卡(乐观锁)
        boolean success = userBackpackDao.useItem(modifyNameCard);
        if (success){
            // 修改名字
            userDao.modifyName(uid, name);
        }


    }
}
