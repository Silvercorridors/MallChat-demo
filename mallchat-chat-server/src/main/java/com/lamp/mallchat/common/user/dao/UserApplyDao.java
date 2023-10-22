package com.lamp.mallchat.common.user.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lamp.mallchat.common.user.domain.entity.UserApply;
import com.lamp.mallchat.common.user.domain.enums.ApplyReadStatusEnum;
import com.lamp.mallchat.common.user.domain.enums.ApplyStatusEnum;
import com.lamp.mallchat.common.user.domain.enums.ApplyTypeEnum;
import com.lamp.mallchat.common.user.mapper.UserApplyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-19
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> implements IService<UserApply> {

    public UserApply getFriendApproving(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserApply::getUid, uid)
                .eq(UserApply::getTargetId, targetUid)
                .one();
    }

    /**
     * 同意好友申请
     * @param applyId 申请id
     */
    public void agree(Long applyId) {
        lambdaUpdate().set(UserApply::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(UserApply::getId, applyId)
                .update();
    }

    public Integer getUnReadCount(Long targetId) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, targetId)
                .eq(UserApply::getStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .count();
    }

    public IPage<UserApply> friendApplyPage(Long uid, Page page) {
        return lambdaQuery()
                .eq(UserApply::getTargetId, uid)
                .eq(UserApply::getType, ApplyTypeEnum.ADD_FRIEND)
                .orderByDesc(UserApply::getCreateTime)
                .page(page);
    }

    public void readApples(Long uid, List<Long> applyIds) {
        lambdaUpdate()
                .eq(UserApply::getReadStatus, ApplyReadStatusEnum.UNREAD.getCode())
                .in(UserApply::getId, applyIds)
                .eq(UserApply::getTargetId, uid)
                .set(UserApply::getStatus, ApplyReadStatusEnum.READ.getCode())
                .update();
    }
}
