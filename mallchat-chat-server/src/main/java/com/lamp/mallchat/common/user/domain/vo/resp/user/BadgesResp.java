package com.lamp.mallchat.common.user.domain.vo.resp.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author silverCorridors
 * @date 2023/10/5 16:44
 * @description 用户信息返回体
 */
@Data
@ApiModel("徽章信息返回体")
public class BadgesResp {
    @ApiModelProperty("徽章")
    private Long id;
    @ApiModelProperty("徽章描述")
    private String describe;
    @ApiModelProperty("徽章图标")
    private String img;
    @ApiModelProperty("是否拥有(0 否 / 1 是)")
    private Integer obtain;
    @ApiModelProperty("是否佩戴(0 否 / 1 是)")
    private Integer wearing;

}
