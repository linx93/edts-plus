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
 * 数据开关表，标志etds能否发送数据
 * </p>
 *
 * @author linx
 * @since 2021-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DataSwitch对象", description="数据开关表，标志etds能否发送数据")
public class DataSwitch implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "0: 开启状态   1：关闭状态")
    private Integer flag;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
