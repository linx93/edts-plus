package com.phadata.etdsplus.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 拉取数据统计日志(提供方)<12>
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DataStatisticsProvide12对象", description="拉取数据统计日志(提供方)<12>")
public class DataStatisticsProvide12 implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "授权通知id")
    private Long noticeId;

    @ApiModelProperty(value = "拉取数据通知id")
    private Long dataId;

    @ApiModelProperty(value = "发起etds唯一编码")
    private String applyEtdsUuid;

    @ApiModelProperty(value = "数据提供方企业dtid")
    private String toDtid;

    @ApiModelProperty(value = "数据提供方企业名称")
    private String toName;

    @ApiModelProperty(value = "数据提供方企业etds唯一编号")
    private String toEtdsUuid;

    @ApiModelProperty(value = "数据授权方企业dtid")
    private String grantDtid;

    @ApiModelProperty(value = "数据授权方企业名称")
    private String grantName;

    @ApiModelProperty(value = "被授权的数据类型")
    private String dataType;

    @ApiModelProperty(value = "被拉取的数量")
    private Integer dataAmount;

    @ApiModelProperty(value = "统计分析的凭证")
    private String statisticsDocument;

    @ApiModelProperty(value = "统一业务标识号")
    private String serialNumber;

    @ApiModelProperty(value = "操作时间")
    private Long operatedTime;

    @ApiModelProperty(value = "创建时间")
    private Long createdTime;

    @ApiModelProperty(value = "授权凭证id")
    private String claimId;
}
