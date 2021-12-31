package com.phadata.etdsplus.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serializable;

/**
 * <p>
 * etds视图对象
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "etds视图对象", description = "etds相关信息")
public class EtdsVO implements Serializable {

    @ApiModelProperty(value = "license过期时间/ETDS的过期时间")
    private Long expirationTime;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

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

    @ApiModelProperty(value = "Etds唯一标识码")
    private String etdsCode;

    @ApiModelProperty(value = "etds名字")
    private String etdsName;
}
