package com.phadata.etdsplus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 凭证服务配置类
 * @author: linx
 * @create: 2021-11-25 11:22
 */
@Data
@Component
@ConfigurationProperties(prefix = "dtc-server")
public class DTCServerConfig {

    /**
     * 申请请求接口的rid
     */
    private String applyRid;
    /**
     * 创建凭证
     */
    private String createClaim;

    /**
     * 验证凭证
     */
    private String validateClaim;

    /**
     * 注销凭证
     */
    private String writeOffClaim;

    /**
     * 服务接口密钥
     */
    private String sKey;

    /**
     * 服务接口偏移量
     */
    private String vKey;
}



