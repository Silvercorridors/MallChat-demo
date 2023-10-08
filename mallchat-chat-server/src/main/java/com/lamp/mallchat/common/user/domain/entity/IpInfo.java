package com.lamp.mallchat.common.user.domain.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author qyxmzg
 * @date 2023/10/7 23:10
 * @description Ip地址信息
 */
@Data
public class IpInfo implements Serializable {

    /**
     * 注册时的ip地址
    */
     private String createIp;

    /**
     * 注册时的ip详情
     */
    private IpDetail createIpDetail;
    /**
     * 最新登录ip
     */
    private String updateIp;
    /**
     * 最新登录时的ip详情
     */
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if (StringUtils.isBlank(ip)){
            return;
        }
        if(StringUtils.isBlank(createIp)){
            createIp = ip;
        }
        updateIp = ip;

    }

    public String needRefreshIp() {
        boolean noNeedRefresh = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIpDetail.getIp()))
                .isPresent();
        return noNeedRefresh ? null : updateIp;

    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if (Objects.equals(createIp, ipDetail.getIp())){
            createIpDetail = ipDetail;
        }
        if (Objects.equals(updateIp, ipDetail.getIp())){
            updateIpDetail = ipDetail;
        }

    }
}
