package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lamp.mallchat.common.common.event.UserBlackEvent;
import com.lamp.mallchat.common.common.event.UserRegisterEvent;
import com.lamp.mallchat.common.common.utils.AssertUtil;
import com.lamp.mallchat.common.user.dao.BlackDao;
import com.lamp.mallchat.common.user.dao.ItemConfigDao;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.dto.ItemInfoDTO;
import com.lamp.mallchat.common.user.domain.dto.SummeryInfoDTO;
import com.lamp.mallchat.common.user.domain.entity.Black;
import com.lamp.mallchat.common.user.domain.entity.IpInfo;
import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.lamp.mallchat.common.user.domain.vo.req.ItemInfoReq;
import com.lamp.mallchat.common.user.domain.vo.req.user.BlackReq;
import com.lamp.mallchat.common.user.domain.vo.req.user.SummeryInfoReq;
import com.lamp.mallchat.common.user.domain.vo.resp.user.BadgesResp;
import com.lamp.mallchat.common.user.domain.vo.resp.user.UserInfoResp;
import com.lamp.mallchat.common.user.service.UserService;
import com.lamp.mallchat.common.user.service.adapter.UserAdapter;
import com.lamp.mallchat.common.user.service.cache.ItemCache;
import com.lamp.mallchat.common.user.service.cache.UserCache;
import com.lamp.mallchat.common.user.service.cache.UserSummaryCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private UserCache userCache;
    @Resource
    private UserSummaryCache userSummaryCache;
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
            //删除缓存
            userCache.userInfoChange(uid);
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
        // 删除缓存
        userDao.wearingBadge(uid, itemId);
        //删除用户缓存
        userCache.userInfoChange(uid);
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

    @Override
    public List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req) {
        // 需要前端同步的uid
        List<Long> needSyncUidList = getNeedSyncUidList(req.getReqList());
        //加载用户信息
        Map<Long, SummeryInfoDTO> batch = userSummaryCache.getBatch(needSyncUidList);
        return req.getReqList()
                .stream()
                .map(a -> batch.containsKey(a.getUid()) ? batch.get(a.getUid()) : SummeryInfoDTO.skip(a.getUid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemInfoDTO> getItemInfo(ItemInfoReq req) {
        return req.getReqList().stream().map(a -> {
            // 本地缓存中获取itemConfig
            ItemConfig itemConfig = itemCache.getByItemId(a.getItemId());
            // 前端传来的最后更新时间 >= 本地缓存中获取的更新时间时，代表不用刷新ItemConfig，直接返回
            if (Objects.nonNull(a.getLastModifyTime()) && a.getLastModifyTime() >= itemConfig.getUpdateTime().getTime()) {
                return ItemInfoDTO.skip(a.getItemId());
            }
            ItemInfoDTO dto = new ItemInfoDTO();
            dto.setItemId(itemConfig.getId());
            dto.setImg(itemConfig.getImg());
            dto.setDescribe(itemConfig.getDescribe());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取需要同步的用户信息
     * @param reqList
     * @return
     */
    private List<Long> getNeedSyncUidList(List<SummeryInfoReq.infoReq> reqList) {
        List<Long> needSyncUidList = new ArrayList<>();
        // 根据uid拿出缓存里的用户信息的上次更新事件
        List<Long> userModifyTime = userCache.getUserModifyTime(reqList.stream().map(SummeryInfoReq.infoReq::getUid).collect(Collectors.toList()));
        for (int i = 0; i < reqList.size(); i++) {
            SummeryInfoReq.infoReq infoReq = reqList.get(i);
            Long modifyTime = userModifyTime.get(i);
            // 判断用户信息是否需要更新, 返回需要更新用户信息的uid
            if (Objects.isNull(infoReq.getLastModifyTime()) || (Objects.nonNull(modifyTime) && modifyTime > infoReq.getLastModifyTime())) {
                needSyncUidList.add(infoReq.getUid());
            }
        }
        return needSyncUidList;
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
