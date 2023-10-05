package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.user.domain.entity.UserBackpack;
import com.lamp.mallchat.common.user.mapper.UserBackpackMapper;
import com.lamp.mallchat.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-05
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> implements IUserBackpackService {

}
