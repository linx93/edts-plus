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
import com.phadata.etdsplus.service.DTIDComponent;
import com.phadata.etdsplus.utils.AESUtil;
import com.phadata.etdsplus.utils.result.ResultCodeMessage;
import lombok.extern.slf4j.Slf4j;
import net.phadata.identity.dtid.entity.DTIDDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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
public class DTIDComponentImpl implements DTIDComponent {
    @Value("${dtid-sever.resolve-dtid:}")
    private String resolveDtid;



    @Override
    public String getCompanyNameByDtid(String dtid) {
        if (dtid == null || dtid.trim().equals("")) {
            throw new BussinessException("解析数字身份异常，数字身份不能为空");
        }
        HttpResponse execute = HttpRequest.get(resolveDtid + "/" + dtid).execute();
        log.info("解析数字身份响应:{}", execute.body());
        JSONObject result = JSON.parseObject(execute.body());
        if (!ResultCodeMessage.SUCCESS.getCode().equals(result.getString("code"))) {
            log.info("解析数字身份失败:{}", result.getString("message"));
            throw new BussinessException("解析数字身份失败:" + result.getString("message"));
        }
        JSONObject payload = result.getJSONObject("payload");
        DTIDDocument dtidDocument = JSONObject.toJavaObject(payload, DTIDDocument.class);
        //从数字身份文档中获取企业名称
        if (dtidDocument == null) {
            log.info("解析数字身份失败");
            throw new BussinessException("解析数字身份失败");
        }
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(dtidDocument.getBind().get("samrInfo")), Map.class);
        String name = map.get("name").toString();
        if (name == null || name.trim().equals("")) {
            log.info("此数字身份还未绑定企业");
            throw new BussinessException("此数字身份还未绑定企业");
        }
        return name;
    }


}

