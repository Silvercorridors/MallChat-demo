package com.lamp.mallchat.common.user.service.impl;

import com.lamp.mallchat.common.common.constants.YesOrNoEnum;
import com.lamp.mallchat.common.common.utils.AssertUtil;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.enums.IdempotentEnum;
import com.lamp.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.lamp.mallchat.common.user.service.UserBackpackService;
import com.lamp.mallchat.common.user.service.cache.ItemCache;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author qyxmzg
 * @date 2023/10/6 23:47
 * @description 用户背包 服务实现类
 */
public class UserBackpackServiceImpl implements UserBackpackService {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private UserBackpackDao userBackpackDao;

    /**
     * 让用户获取一个物品, 使用分布式锁
     * @param uid            用户id
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     上层业务发送的唯一标识
     */
    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) throws InterruptedException {
        //组装幂等号
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        doAcuireItem(uid, itemId, idempotent);
    }

    private void doAcuireItem(Long uid, Long itemId, String idempotent) throws InterruptedException {
        RLock lock = redissonClient.getLock("acquireItem:" + idempotent);
        boolean isLock = lock.tryLock(5, TimeUnit.SECONDS);
        AssertUtil.isTrue(isLock, "请求太频繁了");
        try {
            if (isLock){
                UserBackpack userBackpack = userBackpackDao.getByIdp(idempotent);
                //幂等检查
                if (Objects.nonNull(userBackpack)) {
                    return;
                }
                // 发放物品
                //发物品
                UserBackpack insert = UserBackpack.builder()
                        .uid(uid)
                        .itemId(itemId)
                        .status(YesOrNoEnum.NO.getStatus())
                        .idempotent(idempotent)
                        .build();
                userBackpackDao.save(insert);
            }
        } finally {
            lock.unlock();
        }




    }

    /**
     * 组装幂等号
     * @param itemId
     * @param idempotentEnum
     * @param businessId
     * @return
     */
    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
