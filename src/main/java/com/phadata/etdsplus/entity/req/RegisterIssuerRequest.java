package com.phadata.etdsplus.entity.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * @author tanwei
 * @desc
 * @time 6/2/21 6:41 PM
 * @since 1.0.0
 */
@Data
@Valid
@Accessors(chain = true)
public class RegisterIssuerRequest {

    @ApiModelProperty(value = "签发机构dtid", example = "dtid:dtca:3S6UsrFbozydF5KJRiQmQHcfFSon")
    @NotNull(message = "不能为空")
    private String issuer;
    @ApiModelProperty(value = "签发机构名称", example = "贵州天机信息科技有限公司")
    @NotNull(message = "不能为空")
    private String issuerName;
    @Valid
    private Identity identity;
    @Valid
    private ProvideData provideData;

    @Data
    @Accessors(chain = true)
    public static class Identity {
        @ApiModelProperty(value = "安全码", example = "959893")
        @NotNull(message = "不能为空")
        private String safeCode;
        @ApiModelProperty(value = "私钥", example = "c82337b631d253bfe7b2126db4adebfa5ccaed0568f877108ffa8e37f703fe54415026674bf7092413212147b9c6edd4311a2a331c413dbba1d3fc8fd90c2182d2c0b3fd4fcb86290acec36faf371794")
        @NotNull(message = "不能为空")
        private String priKey;

    }
}
