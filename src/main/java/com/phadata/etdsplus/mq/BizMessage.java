package com.phadata.etdsplus.mq;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发宋mq的参数实体
 *
 * @author linx
 */
@Data
@Accessors(chain = true)
public class BizMessage implements Serializable {

    private static final long serialVersionUID = -4339964820622857991L;

    private String title;

    private String dtid;

    @NotBlank(message = "不能为空")
    private String machineId;

    @Valid
    @NotNull
    private BaseMqInfo baseMqInfo;

    @Data
    @Accessors(chain = true)
    public static class BaseMqInfo implements Serializable {

        private static final long serialVersionUID = -8798510333862923130L;


        @NotBlank(message = "不能为空")
        private String exchange;

        @NotBlank(message = "不能为空")
        private String exchangeType;


        @NotBlank(message = "不能为空")
        private String queue;

        @NotBlank(message = "不能为空")
        private String routingKey;

        @NotBlank(message = "不能为空")
        private String content;
    }

}
