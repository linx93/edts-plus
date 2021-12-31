package com.phadata.etdsplus.entity.res;

import com.phadata.etdsplus.entity.vo.EtdsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应etds的内容
 *
 * @author linx
 * @since 2021-12-31 09:33
 */
@Data
@ApiModel(value = "登陆或刷新成功返回的实体", description = "登陆或刷新成功返回的实体")
public class EtdsResponse {
    @ApiModelProperty(value = "etds相关信息")
    private EtdsVO company;

    @ApiModelProperty(value = "激活状态")
    private Boolean active;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "account")
    private String account;
}
