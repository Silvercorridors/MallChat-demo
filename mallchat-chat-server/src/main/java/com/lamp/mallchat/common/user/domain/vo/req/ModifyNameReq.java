package com.lamp.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author qyxmzg
 * @date 2023/10/5 21:41
 * @description 改名请求VO
 */
@Data
public class ModifyNameReq {
    @NotBlank(message = "用户名不可以为空")
    @Length(max = 6, message = "用户名不可以超过6位")
    @ApiModelProperty("用户名")
    private String name;
}
