package com.lamp.mallchat.common.common.thread.pool;

import com.lamp.mallchat.common.common.thread.factory.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qyxmzg
 * @date 2023/10/3 22:43
 * @description 线程池配置类
 */
@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfig implements AsyncConfigurer {

    private final MallChatExecutorPool mallChatExecutorPool;
    private final WebSocketExecutorPool webSocketExecutorPool;

    /**
     *  构造器注入
      */
    @Autowired
    public ThreadPoolConfig(MallChatExecutorPool mallChatExecutorPool,
                            WebSocketExecutorPool webSocketExecutorPool) {
        this.mallChatExecutorPool = mallChatExecutorPool;
        this.webSocketExecutorPool = webSocketExecutorPool;
    }


    @Override
    public Executor getAsyncExecutor() {
        return mallchatExecutor();
    }


    /**
     * 初始化线程池方法
     * @param abstractExecutorPool
     * @param threadName
     * @param rejectedExecutionHandler
     * @return
     */
    private ThreadPoolTaskExecutor initThreadPoolTaskExecutor(AbstractExecutorPool abstractExecutorPool,
                                                              String threadName,
                                                              RejectedExecutionHandler rejectedExecutionHandler){
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(abstractExecutorPool.getCoreSize());
        threadPool.setMaxPoolSize(abstractExecutorPool.getMaxSize());
        threadPool.setKeepAliveSeconds(abstractExecutorPool.getKeepAliveSeconds());
        threadPool.setQueueCapacity(abstractExecutorPool.getQueueCapacity());
        threadPool.setThreadNamePrefix(threadName);
        threadPool.setRejectedExecutionHandler(rejectedExecutionHandler);
        // 优雅停机，等待线程池任务都执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.setThreadFactory(new MyThreadFactory(threadPool));
        threadPool.initialize();
        return threadPool;
    }


    @Bean(MallChatExecutorPool.EXECUTOR_NAME)
    @Primary
    public ThreadPoolTaskExecutor mallchatExecutor() {
        // 满了丢给原线程执行
        ThreadPoolExecutor.CallerRunsPolicy policy = new ThreadPoolExecutor.CallerRunsPolicy();
        return initThreadPoolTaskExecutor(mallChatExecutorPool, MallChatExecutorPool.EXECUTOR_NAME, policy);
    }


    @Bean(WebSocketExecutorPool.EXECUTOR_NAME)
    public ThreadPoolTaskExecutor websocketExecutor() {
        // 满了丢弃
        ThreadPoolExecutor.DiscardOldestPolicy policy = new ThreadPoolExecutor.DiscardOldestPolicy();
        return initThreadPoolTaskExecutor(webSocketExecutorPool, WebSocketExecutorPool.EXECUTOR_NAME, policy);
    }


}