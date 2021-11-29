package com.phadata.etdsplus.entity.po;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 请求授权通知(请求方)<1>
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ReAuthNoticeApply1对象", description="请求授权通知(请求方)<1>")
@TableName("re_auth_notice_apply_1")
@Accessors(chain = true)
public class ReAuthNoticeApply1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发起etds唯一编码")
    private String applyEtdsUuid;

    @ApiModelProperty(value = "发起etds所属的tdaas的数字身份")
    private String applyDtid;

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

    @ApiModelProperty(value = "发起时间")
    private Long applyTime;

    @ApiModelProperty(value = "创建时间")
    private Long createdTime;


}
