package com.lamp.mallchat.common.common.thread.pool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author silverCorridors
 * @date 2023/10/9 23:04
 * @description mallchat项目共用线程池
 */
@Component
@ConfigurationProperties(prefix = "async-pool.mallchat")
@Data
public class MallChatExecutorPool extends AbstractExecutorPool{
    /**
     * weboscket线程池名字
     */
    public static final String EXECUTOR_NAME = "mallchatExecutor";
    /**
     * mallchat线程池的线程名前缀
     */
    public static final String EXECUTOR_THREAD_NAME_PREFIX = "mallchat-executor-";


}
