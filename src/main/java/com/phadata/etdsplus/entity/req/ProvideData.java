package com.phadata.etdsplus.entity.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author tanwei
 * @desc
 * @time 6/2/21 6:47 PM
 * @since 1.0.0
 */
@Data
@Valid
@Accessors(chain = true)
public class ProvideData {
    @ApiModelProperty(value = "企业名称",example = "贵州远东诚信有限公司")
    @NotNull(message = "不能为空")
    private String companyName;
    @ApiModelProperty(value = "企业统一社会信用代码",example = "91520490MAAKBHW97D")
    @NotNull(message = "不能为空")
    private String uniformNo;
    @ApiModelProperty(value = "法人姓名",example = "刘某")
    @NotNull(message = "不能为空")
    private String corporationName;
    @ApiModelProperty(value = "电子营业执照序列号",example = "360430199706XXXXXX")
    @NotNull(message = "不能为空")
    private String licenceSn;
}
