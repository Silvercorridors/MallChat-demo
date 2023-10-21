package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.common.event.UserBlackEvent;
import com.lamp.mallchat.common.common.event.UserRegisterEvent;
import com.lamp.mallchat.common.common.utils.AssertUtil;
import com.lamp.mallchat.common.user.dao.BlackDao;
import com.lamp.mallchat.common.user.dao.ItemConfigDao;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.Black;
import com.lamp.mallchat.common.user.domain.entity.IpInfo;
import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.lamp.mallchat.common.user.domain.vo.req.BlackReq;
import com.lamp.mallchat.common.user.domain.vo.resp.BadgesResp;
import com.lamp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.lamp.mallchat.common.user.service.UserService;
import com.lamp.mallchat.common.user.service.adapter.UserAdapter;
import com.lamp.mallchat.common.user.service.cache.ItemCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author silverCorridors
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
    @Resource
    private ItemCache itemCache;
    @Resource
    private ItemConfigDao itemConfigDao;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private BlackDao blackDao;

    @Override
    @Transactional
    public Long registry(User newUser) {
        userDao.save(newUser);
        // todo 用户注册事件
        // 通过springEvent发送用户注册事件，通过观察者模式监听事件发生，进行发放改名卡
        // source是事件的发布者
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, newUser));
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

    /**
     * 徽章列表
     * @param uid 用户id
     * @return
     */
    @Override
    public List<BadgesResp> badges(Long uid) {
        // 查询所有徽章列表
        List<ItemConfig> badgesList = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        // 判断当前用户有哪些徽章
        List<UserBackpack> backpacks =
                userBackpackDao.getByItemId(badgesList.stream()
                        .map(ItemConfig::getId)
                        .collect(Collectors.toList()), uid);
        // 查询用户佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(badgesList, backpacks, user);
    }

    /**
     *
     * @param uid
     * @param itemId
     */
    @Override
    public void wearingBadge(Long uid, Long itemId) {
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(firstValidItem, "你还未拥有此徽章");
        // 确保这个物品是徽章
        ItemConfig itemConfig = itemConfigDao.getById(itemId);
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "只有徽章才能佩戴");
        userDao.wearingBadge(uid, itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black user = new Black();
        user.setType(BlackTypeEnum.UID.getType());
        user.setTarget(uid.toString());
        blackDao.save(user);
        User blackedUser = userDao.getById(uid);
        blackIp(Optional.ofNullable(blackedUser.getIpInfo()).map(IpInfo::getCreateIp).orElse(null));
        blackIp(Optional.ofNullable(blackedUser.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null));
        applicationEventPublisher.publishEvent(new UserBlackEvent(this, blackedUser));
    }

    private void blackIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return;
        }
        try{
            Black insert = new Black();
            insert.setType(BlackTypeEnum.IP.getType());
            insert.setTarget(ip);
            blackDao.save(insert);
        }catch (Exception e){
            log.error("duplicate black ip:{}", ip);
        }

    }
}
