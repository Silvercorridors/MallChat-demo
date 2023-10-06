package com.lamp.mallchat.common.common.utils;

import com.lamp.mallchat.common.common.domain.dto.RequestInfo;

/**
 * @author qyxmzg
 * @date 2023/10/5 21:04
 * @description 请求上下文
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> THREAD_LOCAL = new ThreadLocal<RequestInfo>();

    public static void set(RequestInfo requestInfo){
        THREAD_LOCAL.set(requestInfo);
    }

    public static RequestInfo get(){
        return THREAD_LOCAL.get();
    }

    /**
     * 删除，避免内存泄露
     */
    public static void remove(){
        THREAD_LOCAL.remove();
    }

}
