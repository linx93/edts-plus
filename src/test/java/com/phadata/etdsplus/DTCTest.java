package com.phadata.etdsplus;

import com.phadata.etdsplus.entity.dto.ClaimReqBizPackage;
import com.phadata.etdsplus.entity.dto.DTCResponse;
import com.phadata.etdsplus.service.DTCComponent;
import net.phadata.identity.common.DTCType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description: 关于调用dtc基础服务的测试
 * @author: linx
 * @create: 2021-11-25 15:10
 */
@SpringBootTest
public class DTCTest {
    @Autowired
    private DTCComponent dtcComponent;

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
}
