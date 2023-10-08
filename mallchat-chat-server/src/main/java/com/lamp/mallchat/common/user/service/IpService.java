package com.lamp.mallchat.common.user.service;

public interface IpService {
    /**
     * 解析用户ip
     * @param uid 用户id
     */
    void refreshIpDetailAsync(Long uid);
}
