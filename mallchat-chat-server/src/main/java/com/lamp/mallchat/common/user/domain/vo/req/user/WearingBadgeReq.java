package com.lamp.mallchat.common.user.domain.vo.req.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author silverCorridors
 * @date 2023/10/5 21:41
 * @description 改名请求VO
 */
@Data
public class WearingBadgeReq {
    @NotBlank(message = "徽章id不可以为空")
    @ApiModelProperty("徽章id")
    private Long itemId;

}
