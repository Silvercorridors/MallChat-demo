package com.lamp.mallchat.common.common.constants;

/**
 * @author silverCorridors
 * @date 2023/10/3 19:23
 * @description Redis常量类
 */
public class RedisKey {
    private static final String BASE_KEY = "mallchat:chat";

    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING="user:token:uid_%d";

    public static String getKey(String key, Object ...o){
        return BASE_KEY + String.format(key, o);
    }

}
