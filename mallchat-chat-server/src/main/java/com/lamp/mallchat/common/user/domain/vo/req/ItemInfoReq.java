package com.lamp.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author silverCorridors
 * @date 2023/10/28 19:52
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoReq {
    @ApiModelProperty(value = "徽章信息入参")
    @Size(max = 50)
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @ApiModelProperty(value = "徽章id")
        private Long itemId;
        @ApiModelProperty(value = "最近一次更新徽章信息时间")
        private Long lastModifyTime;
    }
}