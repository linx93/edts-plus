package com.phadata.etdsplus.service.impl;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.entity.dto.ClaimReqBizPackage;
import com.phadata.etdsplus.entity.dto.DTCResponse;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.DTCComponent;
import com.phadata.etdsplus.utils.AESUtil;
import com.phadata.etdsplus.utils.result.ResultCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 凭证相关服务实现
 *
 * @author linx
 * @since 2021-11-15
 */
@Slf4j
@Component
public class DTCComponentImpl implements DTCComponent {

    private final DTCServerConfig dtcServerConfig;

    public DTCComponentImpl(DTCServerConfig dtcServerConfig) {
        this.dtcServerConfig = dtcServerConfig;
    }


    /**
     * 获取rid
     *
     * @return
     */
    @Override
    public String applyRid() {
        HttpResponse execute = HttpRequest.post(dtcServerConfig.getApplyRid()).execute();
        log.info("获取rid请求的响应:{}", execute);
        JSONObject result = JSON.parseObject(execute.body());
        if (!ResultCodeMessage.SUCCESS.getCode().equals(result.getString("code"))) {
            throw new BussinessException("获取rid请求失败:" + result.getString("message"));
        }
        String rid = result.getJSONObject("payload").getString("rid");
        if (StringUtils.isBlank(rid)) {
            throw new BussinessException("获取rid请求失败,rid为空");
        }
        return rid;
    }

    @Override
    public DTCResponse createDtc(ClaimReqBizPackage claimReqBizPackage) throws Exception {
        if (claimReqBizPackage == null) {
            throw new BussinessException("创建凭证时claimReqBizPackage参数不能为空");
        }
        String rid = applyRid();
        Map<String, String> reqData = new HashMap<>(8);
        String bizSign;
        try {
            bizSign = AESUtil.encrypt(JSON.toJSONString(claimReqBizPackage), dtcServerConfig.getSKey(), dtcServerConfig.getVKey(), AESUtil.PKCS7);
        } catch (Exception e) {
            log.error("创建凭证出错:{}", e.getMessage());
            throw e;
        }
        reqData.put("bizSign", bizSign);
        reqData.put("rid", rid);
        HttpResponse execute = HttpRequest.post(dtcServerConfig.getCreateClaim()).body(JSON.toJSONString(reqData)).execute();
        log.info("创建凭证请求的响应:{}", execute.body());
        JSONObject result = JSON.parseObject(execute.body());
        if (!ResultCodeMessage.SUCCESS.getCode().equals(result.getString("code"))) {
            throw new BussinessException("请求凭证基础服务,创建凭证失败:" + result.getString("message"));
        }
        JSONObject payload = result.getJSONObject("payload");
        DTCResponse dtcResponse = JSONObject.toJavaObject(payload, DTCResponse.class);
        if (dtcResponse == null) {
            throw new BussinessException("创建凭证返回为空");
        }
        return dtcResponse;
    }

}

