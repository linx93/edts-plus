package com.phadata.etdsplus.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 流转日志vo
 * @author: linx
 * @create: 2021-12-07 18:46
 */
@Data
@Accessors(chain = true)
public class MoveLogsVO {
    @ApiModelProperty(value = "数据大小，单位kb")
    private Double DataSize;


    @ApiModelProperty(value = "接口")
    private String path;


    @ApiModelProperty(value = "时间")
    private Long reqTime;

}
