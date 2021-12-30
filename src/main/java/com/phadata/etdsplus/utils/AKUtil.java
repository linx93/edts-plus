package com.phadata.etdsplus.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Objects;

/**
 * AK的相关方法
 * @author: linx
 * @since 2021-11-17 16:01
 */
@Slf4j
public class AKUtil {
    private static final String raw = "appKey=%s&appSecret=%s&timestamp=%s";

    /**
     * @param appKey
     * @param secret
     * @param timestamp 精确到秒
     * @return
     */
    public static String sign(String appKey, String secret, String timestamp) {
        String plain = String.format(raw, appKey, secret, timestamp);
        String sign = "";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(plain.getBytes());
            sign = byte2HexString(bytes);
            return sign;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * @param appKey
     * @param secret
     * @param timestamp 精确到秒
     * @return
     */
    public static boolean checkSign(String targetSign, String appKey, String secret, String timestamp) {
        String sign = sign(appKey, secret, timestamp);
        return Objects.equals(targetSign, sign);
    }


    /**
     * 校验时间戳是不是前后5分钟
     *
     * @param timestamp
     * @return
     */
    public static boolean checkTimestamp(long timestamp) {
        long epochSecond = Instant.now().getEpochSecond();
        return Math.abs(epochSecond - timestamp) <= 300;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2HexString(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            String temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }



}

