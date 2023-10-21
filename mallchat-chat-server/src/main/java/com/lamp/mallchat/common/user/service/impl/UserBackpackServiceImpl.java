package com.lamp.mallchat.common.user.service.impl;

import com.lamp.mallchat.common.common.annotation.RedissonLock;
import com.lamp.mallchat.common.common.constants.YesOrNoEnum;
import com.lamp.mallchat.common.common.service.LockService;
import com.lamp.mallchat.common.common.utils.AssertUtil;
import com.lamp.mallchat.common.user.dao.UserBackpackDao;
import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.domain.enums.IdempotentEnum;
import com.lamp.mallchat.common.user.service.UserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author silverCorridors
 * @date 2023/10/6 23:47
 * @description 用户背包 服务实现类
 */
@Service
public class UserBackpackServiceImpl implements UserBackpackService {

    @Resource
    private UserBackpackDao userBackpackDao;
    @Resource
    @Lazy
    private UserBackpackServiceImpl userBackpackService;


    /**
     * 让用户获取一个物品, 使用分布式锁
     * @param uid            用户id
     * @param itemId         物品id
     * @param idempotentEnum 幂等类型
     * @param businessId     上层业务发送的唯一标识
     */
    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        //组装幂等号
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        // 发放物品
        userBackpackService.doAcquireItem(uid, itemId, idempotent);
    }


    /**
     * 发放物品
     * @param uid
     * @param itemId
     * @param idempotent
     */
    @RedissonLock(prefixKey = "doAcquireItem", key = "#idempotent", waitTime = 5, unit = TimeUnit.SECONDS)
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
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
