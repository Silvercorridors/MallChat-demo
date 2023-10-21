package com.lamp.mallchat.common.user.service.cache;

import com.lamp.mallchat.common.user.dao.BlackDao;
import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.dao.UserRoleDao;
import com.lamp.mallchat.common.user.domain.entity.Black;
import com.lamp.mallchat.common.user.domain.entity.UserRole;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

}
