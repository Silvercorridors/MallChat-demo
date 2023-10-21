package com.lamp.mallchat.common.common.thread.pool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author silverCorridors
 * @date 2023/10/9 23:04
 * @description Websocket模块线程池
 */
@Component
@ConfigurationProperties(prefix = "async-pool.websocket")
@Data
public class WebSocketExecutorPool extends AbstractExecutorPool{
    /**
     * weboscket线程池名字
     */
    public static final String EXECUTOR_NAME = "websocketExecutor";
    /**
     * Websocket线程池的线程名前缀
     */
    public static final String EXECUTOR_THREAD_NAME_PREFIX = "websocket-executor-";


}
