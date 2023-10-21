package com.lamp.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lamp.mallchat.common.user.domain.entity.UserApply;
import com.lamp.mallchat.common.user.mapper.UserApplyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
