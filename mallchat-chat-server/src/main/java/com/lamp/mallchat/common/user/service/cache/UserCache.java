package com.lamp.mallchat.common.user.service.cache;

import com.lamp.mallchat.common.common.constants.RedisKey;
import com.lamp.mallchat.common.user.dao.BlackDao;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.dao.UserRoleDao;
import com.lamp.mallchat.common.user.domain.entity.Black;
import com.lamp.mallchat.common.user.domain.entity.UserRole;
import com.lamp.mallchat.utils.RedisUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author silverCorridors
 * @date 2023/10/8 23:30
 * @description 用户缓存—缓存uid拥有的角色
 */
@Component
public class UserCache {

    @Resource
    private UserDao userDao;

    @Resource
    private UserSummaryCache userSummaryCache;

    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private BlackDao blackDao;

    @Cacheable(cacheNames = "user", key = "'roles:' + #uid")
    public Set<Long> getRoleByUid(Long uid) {
        List<UserRole> userRoleList = userRoleDao.listByUid(uid);
        return userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    }

    @Cacheable(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackDao.list().stream()
                .collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> result = new HashMap<>();
        collect.forEach((type, list) -> {
            result.put(type, list.stream().map(Black::getTarget).collect(Collectors.toSet()));
        });
        return result;
    }


    @CacheEvict(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> evictBlackMap() {
        return null;
    }

    public List<Long> getUserModifyTime(List<Long> uidList) {
        // 拼接缓存key
        List<String> keys = uidList.stream().map(uid -> RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid)).collect(Collectors.toList());
        // 查询缓存
        return RedisUtils.mget(keys, Long.class);
    }

    /**
     * 更新用户信息上次更新时间
     * @param uid
     */
    public void refreshUserModifyTime(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid);
        RedisUtils.set(key, System.currentTimeMillis());
    }

    public void userInfoChange(Long uid) {
        delUserInfo(uid);
        //删除UserSummaryCache，前端下次懒加载的时候可以获取到最新的数据
        userSummaryCache.delete(uid);
        refreshUserModifyTime(uid);
    }

    private void delUserInfo(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
        RedisUtils.del(key);
    }

    public void online(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        //移除离线表
        RedisUtils.zRemove(offlineKey, uid);
        //更新上线表
        RedisUtils.zAdd(onlineKey, uid, optTime.getTime());
    }
}
