package com.phadata.etdsplus;

import com.phadata.etdsplus.service.impl.EtdsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 到鉴权中心获取token测试
 *
 * @author: lin
 * @since 2021-11-17 19:57
 */
@SpringBootTest
public class GetTokenTest {

    @Autowired
    private EtdsServiceImpl etdsServiceImpl;

    /**
     * 测试去获取鉴权中心获取token
     */
    @Test
    void reqAuthToken() {
        int num = 0;
        while (true) {
            if (num > 100) break;
            String token = etdsServiceImpl.getToken();
            System.out.println(token);
            num++;
        }
    }
}
