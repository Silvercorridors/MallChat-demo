package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.user.domain.entity.Black;
import com.lamp.mallchat.common.user.mapper.BlackMapper;
import com.lamp.mallchat.common.user.service.IBlackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-08
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> implements IBlackService {

}
