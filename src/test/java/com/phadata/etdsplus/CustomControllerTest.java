package com.phadata.etdsplus;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.controller.CustomController;
import com.phadata.etdsplus.entity.dto.Address;
import com.phadata.etdsplus.entity.dto.ApplyAuthBizDataDTO;
import com.phadata.etdsplus.entity.dto.ApplyAuthDTO;
import com.phadata.etdsplus.utils.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 定制层请求的测试
 * @author: xionglin
 * @create: 2021-11-26 16:53
 */
@SpringBootTest
public class CustomControllerTest {
    @Autowired
    private CustomController customController;

    @Test
    void applyAuthTest() {
        ApplyAuthDTO applyAuthDTO = new ApplyAuthDTO();
        ArrayList<Address> cc = new ArrayList<>();
        cc.add(new Address().setEtds("数据提供方dtid").setTdaas("数据提供方etdsCode"));
        applyAuthDTO.setCc(cc);
        applyAuthDTO.setDesc("测试");
        applyAuthDTO.setExpiration(Instant.now().getEpochSecond()+10000000);
        applyAuthDTO.setSerializeNumber("123456789");
        applyAuthDTO.setTo(new Address().setTdaas("dtid:dtca:dQAhX8P8MfYbyTtsAnxcDuHri1g"));
        Result<Boolean> booleanResult = customController.applyAuth(applyAuthDTO);
        System.out.println(booleanResult);
    }

}
