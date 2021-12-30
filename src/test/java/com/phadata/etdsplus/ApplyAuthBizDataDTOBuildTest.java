package com.phadata.etdsplus;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.dto.Address;
import com.phadata.etdsplus.entity.dto.ApplyAuthBizDataDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

/**
 * 构建了授权发送MQ的bizData逻辑测试
 * @author: linx
 * @since 2021-11-26 15:17
 */
public class ApplyAuthBizDataDTOBuildTest {
    @Test
    void test() {
        ApplyAuthBizDataDTO applyAuthBizDataDTO = new ApplyAuthBizDataDTO();
        applyAuthBizDataDTO.setAuthType(1);
        ArrayList<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setEtds("123");
        address.setTdaas("123");
        addresses.add(address);
        applyAuthBizDataDTO.setCc(addresses);
        applyAuthBizDataDTO.setDesc("desc");
        applyAuthBizDataDTO.setSerialNumber("123456");
        applyAuthBizDataDTO.setExpiration(123L);
        applyAuthBizDataDTO.setFrom(address);
        applyAuthBizDataDTO.setTo(address);


        Map map = JSON.parseObject(JSON.toJSONString(applyAuthBizDataDTO), Map.class);
        System.out.println(map);
        System.out.println(map.get("serialNumber") instanceof String);
        System.out.println(map.get("desc") instanceof String);
    }
}
