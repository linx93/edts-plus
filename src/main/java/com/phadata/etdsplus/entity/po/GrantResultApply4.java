package com.phadata.etdsplus.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 授权结果返回表(请求方) <4>
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GrantResultApply4对象", description="授权结果返回表(请求方) <4>")
@TableName("grant_result_apply_4")
@Accessors(chain = true)
public class GrantResultApply4 implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "授权通知id")
    private Long noticeId;

    @ApiModelProperty(value = "发起方企业etds唯一编号")
    private String applyEtdsUuid;

    @ApiModelProperty(value = "数据授权方企业dtid")
    private String grantDtid;

    @ApiModelProperty(value = "数据授权方企业名称")
    private String grantName;

    @ApiModelProperty(value = "数据提供方企业dtid")
    private String toDtid;

    @ApiModelProperty(value = "数据提供方企业名称")
    private String toName;

    @ApiModelProperty(value = "数据提供方企业etds唯一编号")
    private String toEtdsUuid;

    @ApiModelProperty(value = "授权详情")
    private String grantDetails;

    @ApiModelProperty(value = "授权状态")
    private String grantStatus;

    @ApiModelProperty(value = "授权的凭证")
    private String grantDocument;

    @ApiModelProperty(value = "统一业务标识号")
    private String serialNumber;

    @ApiModelProperty(value = "操作时间")
    private Long operatedTime;

    @ApiModelProperty(value = "创建时间")
    private Long createdTime;

    @ApiModelProperty(value = "授权凭证id")
    private String claimId;
}
