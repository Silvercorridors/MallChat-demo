package com.lamp.mallchat.common.user.service;

import com.lamp.mallchat.common.user.domain.dto.ItemInfoDTO;
import com.lamp.mallchat.common.user.domain.dto.SummeryInfoDTO;
import com.lamp.mallchat.common.user.domain.entity.User;
import com.lamp.mallchat.common.user.domain.vo.req.ItemInfoReq;
import com.lamp.mallchat.common.user.domain.vo.req.user.BlackReq;
import com.lamp.mallchat.common.user.domain.vo.req.user.SummeryInfoReq;
import com.lamp.mallchat.common.user.domain.vo.resp.user.BadgesResp;
import com.lamp.mallchat.common.user.domain.vo.resp.user.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lamper
 * @since 2023-09-24
 */
public interface UserService {

    /**
     * 用户注册逻辑
     * @param newUser 新用户
     * @return
     */
    Long registry(User newUser);

    /**
     * 获取用户信息
     * @param uid 用户id
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 修改用户名
     * @param uid 用户id
     * @param name 新名字
     */
    void modifyName(Long uid, String name);

    /**
     * 可选徽章预览
     * @param uid 用户id
     * @return
     */
    List<BadgesResp> badges(Long uid);

    /**
     * 佩戴徽章
     * @param uid 用户id
     * @param itemId 徽章id
     */
    void wearingBadge(Long uid, Long itemId);

    /**
     * 拉黑
     * @param req
     */
    void black(BlackReq req);

    /**
     * 获取用户汇总信息
     * @param req 用户汇总信息请求
     * @return
     */
    List<SummeryInfoDTO> getSummeryUserInfo(SummeryInfoReq req);

    /**
     * 获取徽章信息
     * @param req 徽章信息请求
     * @return
     */
    List<ItemInfoDTO> getItemInfo(ItemInfoReq req);
}
