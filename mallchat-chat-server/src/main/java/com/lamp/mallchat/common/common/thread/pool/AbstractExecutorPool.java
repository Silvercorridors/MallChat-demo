package com.lamp.mallchat.common.common.thread.pool;

import lombok.Data;

/**
 * @author silverCorridors
 * @date 2023/10/9 23:02
 * @description 线程池抽象类
 */
@Data
public abstract class AbstractExecutorPool {
    /**
     * 核心线程数量
     */
    private int coreSize;
    /**
     * 最大线程数量
     */
    private int maxSize;
    /**
     * 核心线程外的空闲线程存活时间
     */
    private int keepAliveSeconds;
    /**
     * 任务队列大小
     */
    private int queueCapacity;


}
