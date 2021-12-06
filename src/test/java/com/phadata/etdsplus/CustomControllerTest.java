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
import java.util.UUID;

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
        cc.add(new Address().setEtds("c6bmo306n88ldpmt4r3g").setTdaas("dtid:dtca:9o4NzgSWDsGQvsvRssFwBBJBF8F"));
        applyAuthDTO.setCc(cc);
        applyAuthDTO.setDesc("测试 柯博体验一下🦉");
        applyAuthDTO.setExpiration(Instant.now().getEpochSecond() + 10000000);
        applyAuthDTO.setSerialNumber("单元测试生成的serialNumber" + Math.random());
        applyAuthDTO.setTo(new Address().setTdaas("dtid:dtca:9o4NzgSWDsGQvsvRssFwBBJBF8F"));
        System.out.println(JSON.toJSONString(applyAuthDTO, true));
        Result<Boolean> booleanResult = customController.applyAuth(applyAuthDTO);
        System.out.println(booleanResult);
    }

}
