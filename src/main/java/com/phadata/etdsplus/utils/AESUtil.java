package com.phadata.etdsplus.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

/**
 * @author tanwei
 * @desc
 * @time 1/11/21 5:40 PM
 * @since 1.0.0
 */
public class AESUtil {

    public static final String ISO10126 = "AES/CBC/ISO10126Padding";
    public static final String PKCS7 = "AES/CBC/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     * @param sSrc 明文
     * @param sKey 密钥
     * @param vKey 偏移量
     * @param aesType 加密类型
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey, String vKey, String aesType) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(aesType);
            IvParameterSpec iv = new IvParameterSpec(vKey.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 解密
     * @param sSrc 密文
     * @param sKey 密钥
     * @param vKey 偏移量
     * @param aesType 加密类型
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey, String vKey, String aesType) throws Exception {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(aesType);
            IvParameterSpec iv = new IvParameterSpec(vKey.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 生成AES KEY
     * @param length
     * @return
     */
    public static byte[] generateDesKey(int length) {
        try {
            KeyGenerator kGen = KeyGenerator.getInstance("AES");
            kGen.init(length);
            SecretKey sKey = kGen.generateKey();
            return sKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}

