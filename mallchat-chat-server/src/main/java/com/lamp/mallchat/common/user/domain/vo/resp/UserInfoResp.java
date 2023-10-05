package com.lamp.mallchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qyxmzg
 * @date 2023/10/5 16:44
 * @description 用户信息返回体
 */
@Data
@ApiModel("用户信息返回体")
public class UserInfoResp {
    @ApiModelProperty("uid")
    private Long id;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户头像")
    private String avatar;
    @ApiModelProperty("用户性别")
    private Integer sex;
    @ApiModelProperty("剩余改名次数")
    private Integer modifyNameChance;

}
