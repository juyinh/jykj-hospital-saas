package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA算法加密/解密和签名/验签工具类
 * 生成密钥对（公钥和私钥）
 * 加密内容与签名内容进行Base64加密解密（有利于HTTP协议下传输）
 */
@Slf4j
public class RSAUtils {
    //客户端公钥加密
    private final static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJDwPMM4h+L0ZVr2uOb6a25D38folTwuR4eVozan2fOXAR4dIs7RK4G9ow0Q9J/Hn+L3lirnGRSw7Hwj31tgh6tbaQYIDi/slCRGHUV6ZadXl8RYzXm4/uQb8hJT33Aiw87ukKY1IwCpLmeUqapUM9T6b1x/frIv1h/MUPx8B2SLAgMBAAECgYAp3w1exJUVLs+dIMu4Br5Eg51BjmKWmTQW6lbVclZXtdcf0QD+SJVODO/u1WgmRLBfDHG4gTzQqnAXSPG83z0yB4LPqovbUthaIDoI3MGmVybCbJpI+AFGOsEAzTjWaW2UYcXgpnsfW8YL0lCyPxfQi9KkiCKgjlWf5AzQ4kqckQJBAMbpuXp99FNnvFPIUSDPdNAw+W1J6X2zVTE4yypbtt3v/8Lr/ThgZK8vciwNchds8L7c5oFBmICvTwfRpMFCQFMCQQC6iPbj6tPkvDg8Q7arFimKNm62gCX2mnPHdPXlTdwKzqx4StT7JAT1NDKdbmNfl/a4p9xFj80ZhZH3GS0YYqPpAkBxJXwTGFiFpQl0Ywr67ecVkfYf255MmVkJcbPneVoQ7rb+irx0eChtmkCInHFcr+RVvVQFDGoSMSZ0XSUppWTvAkEAjR1OgW/GKzWE6Xpkd5uFg2zYZrIdhiikTBEccQNs4rQlX79kzjUalxGM9PJstQW2fU1n4yv9gkA9A40FQ3xj+QJBAJSbLoTeEAwV5M6dCi+lqLZBgMVPI59Gdx6F8sCTRh4fXvTuu78tIo9Ze1gIKS8PtgDb6og0jPCcwb2ceJyUZsA=";
    //服务器私钥解密
    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ8DzDOIfi9GVa9rjm+mtuQ9/H6JU8LkeHlaM2p9nzlwEeHSLO0SuBvaMNEPSfx5/i95Yq5xkUsOx8I99bYIerW2kGCA4v7JQkRh1FemWnV5fEWM15uP7kG/ISU99wIsPO7pCmNSMAqS5nlKmqVDPU+m9cf36yL9YfzFD8fAdkiwIDAQAB";
    /**
     * 算法名称
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 签名算法 MD5withRSA 或 SHA1WithRSA 等
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 密钥长度默认是1024位:
     * 加密的明文最大长度 = 密钥长度 - 11（单位是字节，即byte）
     */
    private static final int KEY_SIZE = 1024;
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private RSAUtils() {
    }

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(KEY_SIZE);
        return generator.generateKeyPair();
    }

    /**
     * 私钥字符串转PrivateKey实例
     * @return
     */
    public static PrivateKey getPrivateKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] decodedKey = Base64.getDecoder().decode(privateKey.getBytes("UTF-8"));// 对私钥进行Base64编码解密
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥字符串转PublicKey实例
     * @return
     */
    public static PublicKey getPublicKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] decodedKey = Base64.getDecoder().decode(publicKey.getBytes("UTF-8")); // 对公钥进行Base64编码解密
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 公钥加密
     *
     * @param data      待加密数据
     * @return
     */
    public static String encryptByPublicKey(String data) throws Exception {
        PublicKey publicKey = RSAUtils.getPublicKey();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String dataEncoder = URLEncoder.encode(data, "utf-8");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = dataEncoder.getBytes("UTF-8").length;
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataEncoder.getBytes("UTF-8"), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataEncoder.getBytes("UTF-8"), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            // 获取加密内容使用Base64进行编码加密,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            //return new String(Base64.encodeBase64String(encryptedData));
            return new String(Base64.getEncoder().encode(encryptedData), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param data  待解密数据
     * @return
     */
    public static String decryptByPrivateKey(String data) throws Exception {
        PrivateKey privateKey  = getPrivateKey();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // 对待解密数据进行Base64编码解密
        byte[] dataBytes = Base64.getDecoder().decode(data.getBytes("UTF-8"));
        int inputLen = dataBytes.length;
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 私钥签名
     *
     * @param data       待签名数据
     * @return 签名
     */
    public static String sign(String data) throws Exception {
        PrivateKey privateKey = getPrivateKey();
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.getEncoder().encode(signature.sign()));  // 对签名内容进行Base64编码加密
    }

    /**
     * 公钥验签
     *
     * @param srcData   原始字符串
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, String sign) throws Exception {
        PublicKey publicKey = getPublicKey();
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign.getBytes())); // 对验签结果进行Base64编码解密
    }

    private static String customDecode(String encodedParam) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedParam.replace("+", "%2B"), StandardCharsets.UTF_8.toString());
    }

    public static void main(String[] args) {
        try {
            // RSA加密
            String data = "{\"id\":\"1748160068338462721\",\"realname\":\"测试\",\"sex\":\"1\",\"birthday\":\"2012-10-10\",\"phone\":\"18777888990\",\"credentials\":\"\",\"age\":\"18\",\"other\":\"1231231231231\",\"test\":\"349875930470347603460346\"}";
            String encryptData = encryptByPublicKey(data);
            System.out.println("加密后内容:" + encryptData);
            String deDate = "EnYdamu02FCkgXf4tNsPn2j3cIEsObpzKd%2BuPUusD4rTsfc1O%2F8n3ZO3oMAuTP9Z4%2FBGF77%2BecIRhbhQ%2BxRpfM0ppgEkg0PTF6ZDy71C8GRv1XD5zb9GRpE0b1kgomc9Vqn3k6oQyJaAc3O7T38aorM3zMMd9dLtvKVsW1sywI0eA%2BX0B1b6rb%2BGWZ8HpOj%2F19qVglaTX9EGgZ%2BDKuQLMCuQkhzuhlgD6O1%2BvwNYMIvyF2LrzCESBP7rlF9sX8MxZEKgtqFrIpuTVpd66r135E0LVlzzvnS93ibYDkq9c5hi8erYQ16K3v4gNMGWD7VRXCqxVFs3LXd0sYHnyjLO9Q%3D%3D";
            deDate = customDecode(deDate);
            // RSA解密
            String decryptData = decryptByPrivateKey(deDate);
            String dataDecode = URLDecoder.decode(decryptData, "utf-8");
            System.out.println("解密后内容:" + dataDecode);

            // RSA签名
            String sign = sign(data);
            System.out.println("签名内容:" + sign);

            // RSA验签
            boolean result = verify(data, sign);
            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加密解密异常");
        }
    }
}
