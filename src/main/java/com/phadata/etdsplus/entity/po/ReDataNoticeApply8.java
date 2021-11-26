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
 * 请求拉取数据通知(请求方)<8>
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ReDataNoticeApply8对象", description="请求拉取数据通知(请求方)<8>")
public class ReDataNoticeApply8 implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "授权结果返回主键")
    private Long resultId;

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

    @ApiModelProperty(value = "通知详情")
    private String noticeDetails;

    @ApiModelProperty(value = "凭证详情")
    private String dtcDocument;

    @ApiModelProperty(value = "统一业务标识号")
    private String serialNumber;

    @ApiModelProperty(value = "操作时间")
    private Long operatedTime;

    @ApiModelProperty(value = "创建时间")
    private Long createdTime;


}
