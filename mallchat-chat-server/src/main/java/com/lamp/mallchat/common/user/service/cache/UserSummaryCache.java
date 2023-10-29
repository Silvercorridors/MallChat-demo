package com.lamp.mallchat.common.user.service.cache;

import com.lamp.mallchat.common.common.constants.RedisKey;
import com.lamp.mallchat.common.common.service.cache.AbstractRedisStringCache;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.domain.dto.SummeryInfoDTO;
import com.lamp.mallchat.common.user.domain.entity.IpDetail;
import com.lamp.mallchat.common.user.domain.entity.IpInfo;
import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.enums.ItemTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author silverCorridors
 * @date 2023/10/27 23:18
 * @description 用户所有信息的缓存
 */
@Component
public class UserSummaryCache extends AbstractRedisStringCache<Long, SummeryInfoDTO> {
    @Resource
    private UserInfoCache userInfoCache;
    @Resource
    private UserBackpackDao userBackpackDao;
    @Resource
    private ItemCache itemCache;


    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING, uid);
    }

    @Override
    protected Long getExpireSeconds() {
        return 10 * 60L;
    }

    @Override
    protected Map<Long, SummeryInfoDTO> load(List<Long> uidList) {
        //用户基本信息
        Map<Long, User> userMap = userInfoCache.getBatch(uidList);
        //用户徽章信息
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        List<Long> itemIds = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        // 根据uid列表和徽章id列表获取背包信息用于查看每个用户拥有哪些徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uidList, itemIds);
        // 转成map
        Map<Long, List<UserBackpack>> userBadgeMap = backpacks.stream()
                .collect(Collectors.groupingBy(UserBackpack::getUid));

        return uidList.stream().map(uid -> {
            SummeryInfoDTO summeryInfoDTO = new SummeryInfoDTO();
            User user = userMap.get(uid);
            if (Objects.isNull(user)) {
                return null;
            }
            List<UserBackpack> userBackpacks = userBadgeMap.getOrDefault(user.getId(), new ArrayList<>());
            summeryInfoDTO.setUid(user.getId());
            summeryInfoDTO.setName(user.getName());
            summeryInfoDTO.setAvatar(user.getAvatar());
            summeryInfoDTO.setLocPlace(Optional.ofNullable(user.getIpInfo()).map(IpInfo::getUpdateIpDetail).map(IpDetail::getCity).orElse(null));
            summeryInfoDTO.setWearingItemId(user.getItemId());
            summeryInfoDTO.setItemIds(userBackpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toList()));
            return summeryInfoDTO;
        }).filter(Objects::nonNull).collect(Collectors.toMap(SummeryInfoDTO::getUid, Function.identity()));
    }
}
