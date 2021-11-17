package com.phadata.etdsplus;


import com.phadata.etdsplus.utils.AKUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;


public class AKUtilTest {
    private String appKey = "xyz";
    private String appSecret = "123";

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


}
