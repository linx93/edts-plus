package com.phadata.etdsplus.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tdaas对etds暂停/恢复的操作记录表
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EtdsStatusRecord对象", description="tdaas对etds暂停/恢复的操作记录表")
public class EtdsStatusRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "类型[0:启动  1:暂停]")
    private Integer type;

    @ApiModelProperty(value = "tdaas对应的数字身份")
    private String companyDtid;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "etds的唯一code")
    private String etdsCode;

}
