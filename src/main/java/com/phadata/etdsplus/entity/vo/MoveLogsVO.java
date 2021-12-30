package com.phadata.etdsplus.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 流转日志vo
 * @author: linx
 * @since 2021-12-07 18:46
 */
@Data
@Accessors(chain = true)
public class MoveLogsVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("数据发起方企业dtid")
    private String apply_dtid;

    @ApiModelProperty("数据发起方企业名称")
    private String apply_name;

    @ApiModelProperty("数据发起方企业etds唯一编号")
    private String apply_etds_uuid;

    @ApiModelProperty("数据授权方企业dtid")
    private String grant_dtid;

    @ApiModelProperty("数据授权方企业名称")
    private String grant_name;

    @ApiModelProperty("数据提供方etds唯一编码")
    private String to_etds_uuid;

    @ApiModelProperty("授权详情")
    private String desc;
    @ApiModelProperty("被拉取的数量")
    private Integer data_amount;

    @ApiModelProperty("被拉取的文件大小")
    private Double data_file_size;

    @ApiModelProperty("统计分析的凭证")
    private String statistics_document;

    @ApiModelProperty("统一业务标识号")
    private String serial_number;
    @ApiModelProperty("操作时间")
    private Long operated_time;

    @ApiModelProperty("创建时间")
    private Long created_time;

    @ApiModelProperty("凭证id")
    private String dtc_id;


}
