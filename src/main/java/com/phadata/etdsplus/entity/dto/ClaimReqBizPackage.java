package com.phadata.etdsplus.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author tanwei
 * @desc
 * @time 2/5/21 2:12 PM
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class ClaimReqBizPackage {

    @NotBlank(message = "issuer不能为空")
    private String issuer;
    @NotBlank(message = "holder不能为空")
    private String holder;
    //@NotBlank(message = "unionId不能为空")
    private String unionId;
    //@NotNull(message = "expire不能为空")
    private Long expire;
    //@NotNull(message = "pieces不能为空")
    private Integer pieces = 1;
    @NotNull(message = "bizData不能为空")
    private Map<String, Object> bizData;
    //@NotBlank(message = "type不能为空")
    private String type;
    private Integer times = 0;
    private String targetId;
    private String tdrType;

    public static ClaimReqBizPackage parser(String text) {
        return JSONObject.parseObject(text, new TypeReference<>() {
        });
    }
}
