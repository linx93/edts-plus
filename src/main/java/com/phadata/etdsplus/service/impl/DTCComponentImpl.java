package com.phadata.etdsplus.service.impl;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.entity.dto.ClaimReqBizPackage;
import com.phadata.etdsplus.entity.dto.DTCResponse;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.TdaasPrivateKey;
import com.phadata.etdsplus.entity.req.ProvideData;
import com.phadata.etdsplus.entity.req.RegisterIssuerRequest;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.DTCComponent;
import com.phadata.etdsplus.service.DTIDComponent;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.service.TdaasPrivateKeyService;
import com.phadata.etdsplus.utils.AESUtil;
import com.phadata.etdsplus.utils.EtdsUtil;
import com.phadata.etdsplus.utils.result.ResultCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
    private final TdaasPrivateKeyService tdaasPrivateKeyService;
    private final EtdsUtil etdsUtil;
    private final DTIDComponent dtidComponent;

    public DTCComponentImpl(DTCServerConfig dtcServerConfig, TdaasPrivateKeyService tdaasPrivateKeyService, EtdsUtil etdsUtil, DTIDComponent dtidComponent) {
        this.dtcServerConfig = dtcServerConfig;
        this.tdaasPrivateKeyService = tdaasPrivateKeyService;
        this.etdsUtil = etdsUtil;
        this.dtidComponent = dtidComponent;
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

    @Override
    public Map<String, Object> parse(DTCResponse dtcResponse) {
        if (dtcResponse == null) {
            throw new BussinessException("DTCResponse为空");
        }
        List<DTCResponse.ClaimResInfo> claims = dtcResponse.getClaims();
        if (claims.isEmpty()) {
            throw new BussinessException("DTCResponse中的claims为空");
        }
        DTCResponse.ClaimResInfo claimResInfo = claims.get(0);
        if (claimResInfo == null) {
            throw new BussinessException("DTCResponse中的claims.get(0)为空");
        }
        Map<String, Object> claim = claimResInfo.getClaim();
        if (claim == null) {
            throw new BussinessException("DTCResponse中的claims的claim为空");
        }
        return claim;
    }

    @Override
    public boolean registerIssuer(EtdsService etdsService) {
        List<TdaasPrivateKey> list = tdaasPrivateKeyService.list();
        if (list.isEmpty()) {
            try {
                Thread.sleep(2000);
                list = tdaasPrivateKeyService.list();
            } catch (InterruptedException e) {
            }
        }
        if (list.isEmpty()) {
            throw new BussinessException("TDaaS还未同步给ETDS私钥和安全码");
        }
        TdaasPrivateKey tdaasPrivateKey = list.get(0);
        Etds etds = etdsUtil.EtdsInfo(etdsService);
        String companyName;
        try {
            companyName = dtidComponent.getCompanyNameByDtid(etds.getCompanyDtid());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BussinessException(e.getMessage());
        }
        RegisterIssuerRequest registerIssuerRequest = new RegisterIssuerRequest()
                .setIssuer(etds.getCompanyDtid())
                .setIssuerName(companyName + etds.getEtdsName())
                .setIdentity(new RegisterIssuerRequest.Identity().setPriKey(tdaasPrivateKey.getPrivateKey()).setSafeCode(tdaasPrivateKey.getSafeCode()))
                .setProvideData(new ProvideData()
                        .setCompanyName(companyName)
                        .setCorporationName("11")
                        .setLicenceSn("11")
                        .setUniformNo("11"));
        log.info("注册成为发行方的参数:{}", JSON.toJSONString(registerIssuerRequest));
        log.info("注册成为发行方的url:{}", dtcServerConfig.getRegisterIssuer());
        HttpResponse execute = HttpRequest.post(dtcServerConfig.getRegisterIssuer()).body(JSON.toJSONString(registerIssuerRequest)).execute();
        log.info("注册成为发行方的响应:{}", execute.body());
        JSONObject result = JSON.parseObject(execute.body());
        if (!ResultCodeMessage.SUCCESS.getCode().equals(result.getString("code"))) {
            if ("900012".equals(result.getString("code"))) {
                //发行方已注册
                return true;
            }
            throw new BussinessException("注册成为发行方失败:" + result.getString("message"));
        }
        //返回的是注册ID
        String registryId = result.getString("payload");
        //使用注册ID查询是否注册成功

        HttpResponse get = HttpRequest.get(dtcServerConfig.getGetIssuer() + "/" + registryId).execute();
        log.info("使用注册ID查询是否注册成功的响应:{}", get.body());
        JSONObject resultGet = JSON.parseObject(get.body());

        if (!ResultCodeMessage.SUCCESS.getCode().equals(resultGet.getString("code"))) {
            throw new BussinessException("使用注册ID查询是否注册成功失败:" + resultGet.getString("message"));
        }
        JSONObject payload = resultGet.getJSONObject("payload");
        if (payload == null) {
            log.error("注册成为发行方失败");
            throw new BussinessException("注册成为发行方失败");
        }
        log.info("注册成为发行方信息:{}", payload);
        return true;
    }

}

