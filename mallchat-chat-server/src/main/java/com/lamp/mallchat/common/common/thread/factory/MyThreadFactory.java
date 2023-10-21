package com.lamp.mallchat.common.common.thread.factory;

import com.lamp.mallchat.common.common.thread.handler.MyUncaughtExceptionHandler;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * @author silverCorridors
 * @date 2023/10/3 23:24
 * @description 装饰器—线程工厂类
 */
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    // 异常捕获类--单例
    public static final MyUncaughtExceptionHandler MY_UNCAUGHT_EXCEPTION_HANDLER
            = new MyUncaughtExceptionHandler();

    /**
     * 原始类--原始线程工厂
     */
    private ThreadFactory original;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = original.newThread(r);
        thread.setUncaughtExceptionHandler(MY_UNCAUGHT_EXCEPTION_HANDLER);
        return thread;
    }
}
