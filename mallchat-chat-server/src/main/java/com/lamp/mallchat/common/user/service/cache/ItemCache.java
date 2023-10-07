package com.lamp.mallchat.common.user.service.cache;

import com.lamp.mallchat.common.user.dao.ItemConfigDao;
import com.lamp.mallchat.common.user.domain.entity.ItemConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qyxmzg
 * @date 2023/10/6 16:34
 * @description 物品缓存类
 */
@Component
public class ItemCache {


    @Resource
    private ItemConfigDao itemConfigDao;

    /**
     * 根据type缓存物品到本地
     * @param itemType
     * @return
     */
    @Cacheable(cacheNames = {"item"}, key = "'itemsByType:' + #itemType")
    public List<ItemConfig> getByType(Integer itemType){
        return itemConfigDao.getValidByType(itemType);
    }




    /**
     * 清空缓存
     */
    @CacheEvict(cacheNames = "item", key = "'itemsByType:' + #itemType")
    public void evictByType(Integer itemType){
    }
}
