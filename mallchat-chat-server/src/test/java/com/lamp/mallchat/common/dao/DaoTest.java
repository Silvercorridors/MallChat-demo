package com.lamp.mallchat.common.dao;

import com.lamp.mallchat.common.user.dao.UserDao;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author silverCorridors
 * @date 2023/9/24 0:50
 * @description DaoTest
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void testRedis(){
//        redisTemplate.opsForValue().set("name","qyx");
//        String name = (String) redisTemplate.opsForValue().get("name");
//        System.out.println(name);
        RedisUtils.set("name", "hahaha");
        String name = RedisUtils.getStr("name");
        System.out.println(name);
    }

    @Test
    public void testRedisson(){
        RLock lock = redissonClient.getLock("123");
        lock.lock();
        System.out.println();
        lock.unlock();
    }

    /**
     * 线程池异常捕获
     */
    @Test
    public void thread() throws InterruptedException {
        threadPoolTaskExecutor.execute(() -> {
            if (1 == 1){
                log.error("此处有异常");
                throw new RuntimeException("发生了异常");
            }
        });
        Thread.sleep(200);
    }

    @Test
    public void test(){
        User byId = userDao.getById(1L);
        User insert = User.builder().name("11").openId("1234").build();
        boolean save = userDao.save(insert);
        System.out.println(save);
    }

}
