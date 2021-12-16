package com.phadata.etdsplus;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.entity.dto.ClaimReqBizPackage;
import com.phadata.etdsplus.entity.dto.DTCResponse;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.DTCComponent;
import com.phadata.etdsplus.utils.result.ResultCodeMessage;
import lombok.extern.slf4j.Slf4j;
import net.phadata.identity.common.DTCType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description: 关于调用dtc基础服务的测试
 * @author: linx
 * @create: 2021-11-25 15:10
 */
@Slf4j
@SpringBootTest
public class DTCTest {

    @Autowired
    private DTCComponent dtcComponent;

    @Autowired
    DTCServerConfig dtcServerConfig;

    /**
     * 测试获取rid
     */
    @Test
    void applyRid() {
        String s = dtcComponent.applyRid();
        System.out.println(s);
    }

    /**
     * 创建凭证测试
     */
    @Test
    void createDtc() throws Exception {
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage();
        claimReqBizPackage.setExpire(1640418687L);
        claimReqBizPackage.setHolder("dtid:dtca:ANoN5b6R8r926PdXGEttQp5Hnj1");
        claimReqBizPackage.setIssuer("dtid:dtca:9o4NzgSWDsGQvsvRssFwBBJBF8F");
        claimReqBizPackage.setUnionId("1");
        claimReqBizPackage.setTimes(1);
        claimReqBizPackage.setType(DTCType.OAUTH.getType());
        claimReqBizPackage.setTdrType("-1");
        DTCResponse dtc = dtcComponent.createDtc(claimReqBizPackage);
        System.out.println(dtc);
    }


    @Test
    void test() {
        String url = dtcServerConfig.getGetIssuer() + "/" + "92aad30c-b276-46fc-b38f-7d89dd3e2971";
        HttpResponse get = HttpRequest.get(url).execute();
        log.info("使用注册ID查询是否注册成功的响应:{}", get.body());
        JSONObject resultGet = JSON.parseObject(get.body());
        log.info("url:{}", url);
        if (!ResultCodeMessage.SUCCESS.getCode().equals(resultGet.getString("code"))) {
            throw new BussinessException("使用注册ID查询是否注册成功失败:" + resultGet.getString("message"));
        }
        JSONObject payload = resultGet.getJSONObject("payload");
        if (payload == null) {
            log.error("注册成为发行方失败");
            throw new BussinessException("注册成为发行方失败");
        }
        log.info("注册成为发行方信息:{}", payload);
    }
}
