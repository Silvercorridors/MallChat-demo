package com.lamp.mallchat.common.user.dao;

import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import com.lamp.mallchat.common.user.mapper.ItemConfigMapper;
import com.lamp.mallchat.common.user.service.IItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author lamper
 * @since 2023-10-05
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig> {

}
