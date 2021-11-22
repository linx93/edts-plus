package com.phadata.etdsplus.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * license激活码
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Etds对象", description="etds相关信息")
public class Etds implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "license")
    private String license;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "企业dtid")
    private String companyDtid;

    @ApiModelProperty(value = "状态[0 : 正常  1:暂停]")
    private Integer state;

    @ApiModelProperty(value = "etds url（ip+port）")
    private String etdsUrl;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "激活码")
    private String activationCode;

    @ApiModelProperty(value = "Etds唯一标识码")
    private String etdsCode;

    @ApiModelProperty(value = "应用key")
    private String appKey;

    @ApiModelProperty(value = "密钥")
    private String appSecret;


}
