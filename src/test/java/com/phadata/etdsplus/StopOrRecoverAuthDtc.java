package com.phadata.etdsplus;

import com.phadata.etdsplus.controller.TdaasController;
import com.phadata.etdsplus.entity.dto.AuthDtcDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description: 测试tdaas暂停/恢复某个授权凭证的使用
 * @author: xionglin
 * @create: 2021-11-26 19:08
 */
@SpringBootTest
public class StopOrRecoverAuthDtc {
    @Autowired
    private TdaasController tdaasController;

    /**
     * 暂停
     */
    @Test
    void stopAuthDtc() {
        AuthDtcDTO authDtcDTO = new AuthDtcDTO();
        authDtcDTO.setClaimId("abc");
        tdaasController.stopAuthDtc(authDtcDTO);
    }

    /**
     * 恢复
     */
    @Test
    void recoverAuthDtc() {
        AuthDtcDTO authDtcDTO = new AuthDtcDTO();
        authDtcDTO.setClaimId("abc");
        tdaasController.recoverAuthDtc(authDtcDTO);
    }

}
