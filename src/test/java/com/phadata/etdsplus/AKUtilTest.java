package com.phadata.etdsplus;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.phadata.etdsplus.utils.AKUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;


public class AKUtilTest {
    private String appKey = "c6b00006n88k61eq7nug";
    private String appSecret = "c6b00006n88k61eq7nv0";

    @Test
    public void generator() {
        long epochSecond = Instant.now().getEpochSecond();
        String sign = AKUtil.sign(appKey, appSecret, String.valueOf(epochSecond));
        System.out.println(sign);
        System.out.println(epochSecond);
    }


    @Test
    public void checkSign() {
        String targetSign = "880674bd635007edcb10c7c6c72bb8682f3986d06c9cb0afec8bc49ce9b6a5d1";
        String epochSecond = "1637139859";
        boolean b = AKUtil.checkSign(targetSign, appKey, appSecret, epochSecond);
        System.out.println(b);
    }


    /**
     * 去鉴权中心获取token测试
     */
    @Test
    void getToken() {
        long epochSecond = Instant.now().getEpochSecond();
        String sign = AKUtil.sign(appKey, appSecret, String.valueOf(epochSecond));
        HttpResponse execute = HttpRequest.post("192.168.1.254:8080/api/v1/auth/token/create")
                .header("x-appKey", appKey)
                .header("x-timestamp", String.valueOf(epochSecond))
                .header("x-signature", sign)
                .execute();
        System.out.println(execute.body());
    }


}
