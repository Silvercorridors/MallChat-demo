package com.lamp.mallchat.common.common.constants;

/**
 * @author silverCorridors
 * @date 2023/10/3 19:23
 * @description Redis常量类
 */
public class RedisKey {
    /**
     * 基础key
     */
    private static final String BASE_KEY = "mallchat:chat";

    /**
     * 在线用户列表
     */
    public static final String ONLINE_UID_ZET = "online";
    /**
     * 离线用户列表
     */
    public static final String OFFLINE_UID_ZET = "offline";

    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING="user:token:uid_%d";

    /**
     * 用户的信息更新时间
     */
    public static final String USER_MODIFY_STRING = "userModify:uid_%d";
    /**
     * 用户的信息汇总
     */
    public static final String USER_SUMMARY_STRING = "userSummary:uid_%d";
    /**
     * 用户信息
     */
    public static final String USER_INFO_STRING = "userInfo:uid_%d";
    public static String getKey(String key, Object ...o){
        return BASE_KEY + String.format(key, o);
    }

}
