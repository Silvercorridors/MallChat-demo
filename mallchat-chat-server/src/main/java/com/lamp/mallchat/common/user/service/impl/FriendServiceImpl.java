package com.lamp.mallchat.common.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.lamp.mallchat.common.common.annotation.RedissonLock;
import com.lamp.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.req.PageBaseReq;
import com.lamp.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.dao.UserFriendDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserFriend;
import com.lamp.mallchat.common.user.domain.vo.req.FriendCheckReq;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendCheckResp;
import com.lamp.mallchat.common.user.domain.vo.resp.FriendResp;
import com.lamp.mallchat.common.user.service.FriendService;
import com.lamp.mallchat.common.user.service.adapter.FriendAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author : limeng
 * @description : 好友
 * @date : 2023/07/19
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private UserFriendDao userFriendDao;

    @Resource
    private UserDao userDao;


    @Override
    public FriendCheckResp check(Long uid, FriendCheckReq request) {
        List<UserFriend> friendList = userFriendDao.getByFriends(uid, request.getUidList());
        Set<Long> friendUidSet = friendList.stream()
                .map(UserFriend::getFriendUid).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = request.getUidList().stream().map(friendUid -> {
            FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
            friendCheck.setUid(friendUid);
            friendCheck.setIsFriend(friendUidSet.contains(friendUid));
            return friendCheck;
        }).collect(Collectors.toList());

        return FriendCheckResp.builder()
                .checkedList(friendCheckList)
                .build();
    }
    @Override
    public CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, request);
        if (CollectionUtils.isEmpty(friendPage.getList())) {
            return CursorPageBaseResp.empty();
        }
        List<Long> friendUids = friendPage.getList()
                .stream().map(UserFriend::getFriendUid)
                .collect(Collectors.toList());
        // 根据friendUids查询出用户信息
        List<User> userList = userDao.getFriendList(friendUids);
        return CursorPageBaseResp.init(friendPage, FriendAdapter.buildFriend(friendPage.getList(), userList));
    }
}
