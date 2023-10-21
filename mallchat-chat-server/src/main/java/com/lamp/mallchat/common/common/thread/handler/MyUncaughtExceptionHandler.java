package com.lamp.mallchat.common.common.thread.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author silverCorridors
 * @date 2023/10/3 23:05
 * @description 线程异常捕获类, 如果一个线程不设置异常捕获类，那么未捕获的异常会打印到控制台
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String EXCEPTION_DESCRIPTION_PREFIX = "Exception in thread";

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error(EXCEPTION_DESCRIPTION_PREFIX, e);
    }
}
