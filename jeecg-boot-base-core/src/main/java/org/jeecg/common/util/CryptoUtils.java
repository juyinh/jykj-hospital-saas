package org.jeecg.common.util;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2024/1/15 10:39
 **/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持AES、DES、RSA加密、数字签名以及生成对称密钥和非对称密钥对
 */
public class CryptoUtils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Decoder BASE64_DECODER = Base64.getDecoder();
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final Map<Algorithm, KeyFactory> KEY_FACTORY_CACHE = new ConcurrentHashMap<>();
    private static final Map<Algorithm, Cipher> CIPHER_CACHE = new HashMap<>();

    /**
     * 生成非对称密钥对，目前支持的算法有RSA、DSA。备注：默认生成的密钥格式为PKCS8
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static AsymmetricKeyPair generateAsymmetricKeyPair(Algorithm algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm.getName());
        generator.initialize(algorithm.getKeySize());
        KeyPair keyPair = generator.generateKeyPair();
        String publicKey = BASE64_ENCODER.encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = BASE64_ENCODER.encodeToString(keyPair.getPrivate().getEncoded());
        System.out.println("publicKey:"+ publicKey);
        System.out.println("privateKey:"+ privateKey);
        return new AsymmetricKeyPair(publicKey, privateKey);
    }

    /**
     * 非对称加密
     * @param publicKeyText 公钥
     * @param plainText 明文
     * @return
     * @throws Exception
     */
    public static String encryptAsymmetrically(String publicKeyText, String plainText) throws Exception {
        Algorithm algorithm = Algorithm.Encryption.RSA_ECB_PKCS1;
        PublicKey publicKey = regeneratePublicKey(publicKeyText, Algorithm.Encryption.RSA_ECB_PKCS1);
        byte[] ciphertextInBytes = transform(algorithm, Cipher.ENCRYPT_MODE, publicKey, plainText);
        return BASE64_ENCODER.encodeToString(ciphertextInBytes);
    }

    /**
     * 非对称解密
     * @param privateKeyText 私钥
     * @param ciphertext 密文
     * @return
     * @throws Exception
     */
    public static String decryptAsymmetrically(String privateKeyText, String ciphertext) throws Exception {
        Algorithm algorithm = Algorithm.Encryption.RSA_ECB_PKCS1;
        PrivateKey privateKey = regeneratePrivateKey(privateKeyText, algorithm);
        byte[] plainTextInBytes = transform(algorithm, Cipher.DECRYPT_MODE, privateKey, ciphertext);
        return new String(plainTextInBytes, DEFAULT_CHARSET);
    }

    /**
     * 生成数字签名
     * @param privateKeyText 私钥
     * @param msg 传输的数据
     * @return 数字签名
     * @throws Exception
     */
    public static String doSign(String privateKeyText, String msg) throws Exception {
        Algorithm encryptionAlgorithm = Algorithm.Encryption.RSA_ECB_PKCS1;
        Algorithm signatureAlgorithm = Algorithm.Signing.SHA256WithRSA;
        PrivateKey privateKey = regeneratePrivateKey(privateKeyText, encryptionAlgorithm);
        // Signature只支持签名算法
        Signature signature = Signature.getInstance(signatureAlgorithm.getName());
        signature.initSign(privateKey);
        signature.update(msg.getBytes(DEFAULT_CHARSET));
        byte[] signatureInBytes = signature.sign();
        return BASE64_ENCODER.encodeToString(signatureInBytes);
    }

    /**
     * 数字签名验证
     * @param publicKeyText 公钥
     * @param msg 传输的数据
     * @param signatureText 数字签名
     * @return 校验是否成功
     * @throws Exception
     */
    public static boolean doVerify(String publicKeyText, String msg, String signatureText) throws Exception {
        Algorithm encryptionAlgorithm = Algorithm.Encryption.RSA_ECB_PKCS1;
        Algorithm signatureAlgorithm = Algorithm.Signing.SHA256WithRSA;
        PublicKey publicKey = regeneratePublicKey(publicKeyText, encryptionAlgorithm);
        Signature signature = Signature.getInstance(signatureAlgorithm.getName());
        signature.initVerify(publicKey);
        signature.update(msg.getBytes(DEFAULT_CHARSET));
        return signature.verify(BASE64_DECODER.decode(signatureText));
    }

    private static PublicKey regeneratePublicKey(String publicKeyText, Algorithm algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyInBytes = BASE64_DECODER.decode(publicKeyText);
        KeyFactory keyFactory = getKeyFactory(algorithm);
        // 公钥必须使用RSAPublicKeySpec或者X509EncodedKeySpec
        KeySpec publicKeySpec = new X509EncodedKeySpec(keyInBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }

    private static PrivateKey regeneratePrivateKey(String key, Algorithm algorithm) throws Exception {
        byte[] keyInBytes = BASE64_DECODER.decode(key);
        KeyFactory keyFactory = getKeyFactory(algorithm);
        // 私钥必须使用RSAPrivateCrtKeySpec或者PKCS8EncodedKeySpec
        KeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyInBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        return privateKey;
    }

    private static KeyFactory getKeyFactory(Algorithm algorithm) throws NoSuchAlgorithmException {
        KeyFactory keyFactory = KEY_FACTORY_CACHE.get(algorithm);
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance(algorithm.getName());
            KEY_FACTORY_CACHE.put(algorithm, keyFactory);
        }

        return keyFactory;
    }

    private static byte[] transform(Algorithm algorithm, int mode, Key key, String data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = data.getBytes("UTF-8").length;
        int offset = 0;
        byte[] cache;
        int i = 0;
        Cipher cipher = CIPHER_CACHE.get(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes("UTF-8"), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes("UTF-8"), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        // 解密后的内容
        return out.toByteArray();
    }

    private static byte[] transform(Algorithm algorithm, int mode, Key key, IvParameterSpec iv, byte[] msg) throws Exception {
        Cipher cipher = CIPHER_CACHE.get(algorithm);
        // double check，减少上下文切换
        if (cipher == null) {
            synchronized (CryptoUtils.class) {
                if ((cipher = CIPHER_CACHE.get(algorithm)) == null) {
                    cipher = determineWhichCipherToUse(algorithm);
                    CIPHER_CACHE.put(algorithm, cipher);
                }
                cipher.init(mode, key, iv);
                return cipher.doFinal(msg);
            }
        }

        synchronized (CryptoUtils.class) {
            cipher.init(mode, key, iv);
            return cipher.doFinal(msg);
        }
    }

    private static Cipher determineWhichCipherToUse(Algorithm algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher;
        String transformation = algorithm.getTransformation();
        // 官方推荐的transformation使用algorithm/mode/padding组合，SunJCE使用ECB作为默认模式，使用PKCS5Padding作为默认填充
        if (StringUtils.isNotEmpty(transformation)) {
            cipher = Cipher.getInstance(transformation);
        } else {
            cipher = Cipher.getInstance(algorithm.getName());
        }
        return cipher;
    }

    /**
     * 算法分为加密算法和签名算法，更多算法实现见：<br/>
     * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#impl">jdk8中的标准算法</a>
     */
    public static class Algorithm {

        public interface Encryption {
            Algorithm AES_ECB_PKCS5 = new Algorithm("AES", "AES/ECB/PKCS5Padding", 128);
            Algorithm AES_CBC_PKCS5 = new Algorithm("AES", "AES/CBC/PKCS5Padding", 128);
            Algorithm DES_ECB_PKCS5 = new Algorithm("DES", "DES/ECB/PKCS5Padding", 56);
            Algorithm DES_CBC_PKCS5 = new Algorithm("DES", "DES/CBC/PKCS5Padding", 56);
            Algorithm RSA_ECB_PKCS1 = new Algorithm("RSA", "RSA/ECB/PKCS1Padding", 1024);
            Algorithm DSA = new Algorithm("DSA", 1024);
        }

        public interface Signing {
            Algorithm SHA1WithDSA = new Algorithm("SHA1withDSA", 1024);
            Algorithm SHA1WithRSA = new Algorithm("SHA1WithRSA", 2048);
            Algorithm SHA256WithRSA = new Algorithm("SHA256WithRSA", 2048);
        }

        @Getter
        private String name;
        @Getter
        private String transformation;
        @Getter
        private int keySize;

        public Algorithm(String name, int keySize) {
            this(name, null, keySize);
        }

        public Algorithm(String name, String transformation, int keySize) {
            this.name = name;
            this.transformation = transformation;
            this.keySize = keySize;
        }

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AsymmetricKeyPair {

        private String publicKey;
        private String privateKey;
    }

    public static void main(String[] args) throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBuCwvmwmSTFebSu0x/cSVtuULhy00Eq+gwVcwRb/KQmEnFHzqWsFJTtKkfE4sEpNaEnCrTu2/Y8oFsX1fCZFl3JAcQ17t6an2r5FEFHS75yZhARucq+SKarLuJRBEWYBq7AbBU33yDeWABPjMLSwzn6NGFJidxO0CTVcJdof2RwIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMG4LC+bCZJMV5tK7TH9xJW25QuHLTQSr6DBVzBFv8pCYScUfOpawUlO0qR8TiwSk1oScKtO7b9jygWxfV8JkWXckBxDXu3pqfavkUQUdLvnJmEBG5yr5Ipqsu4lEERZgGrsBsFTffIN5YAE+MwtLDOfo0YUmJ3E7QJNVwl2h/ZHAgMBAAECgYEAgndxetZlydwUnIgH28VXYqV8+RifPxTep9vOxroTAztVhyZrQsLAroihoRn+4/vWtbD5pMZRpLamVtNE5n7hJpXF/qRl2qPEqc0VNDKbN9hQmhy9NDfylMfa0JLaLpzKCiSNyVyL/LjG9VqxZD6wLNp+NsOWja870qqEmX6Fx1ECQQD179ZKShWD4qHprkI8qo6z8YFs9nEUo6m2KnHN7hRnnMSSSfD5Fk65O+IR+JDLYb1BhdiCbcZBwoIBbtjK+xQ1AkEAyaVdAh1sW4c+ZeoNdKLxW8HqQSa7EHf2O3RzNo5xAOhOvnjih0QHwkjEmjXXdZP345/ivlgTC3IJ8J6QoVi4CwJBAORXOe7fufopgYslb/pDoRsRL8mrRnKTQg8QeXlpgyhwNVJdtI6QV6dEIjHtRwKOTNl5G/x9T4XewjXyZPaRB8ECQHy9nWHXjda5PXKSbax/uEuJFD8llIdFjr+M9FEFoQZsr3nJEmmXPi2EEotLT3RfCvR3Ti9uEKn97AQ8M0B8v60CQCZhj4Rt2OYi9J5ucq2FtqU8P7gjpiVWu20h/SDWeIUmmwP9KxfOnOFAeamKzXF71G/UzwqTabf1/PfKVuXqglg=";
        //generateAsymmetricKeyPair(Algorithm.Encryption.RSA_ECB_PKCS1);
        String text = "我是中华人民共和国！";
        String encode = encryptAsymmetrically(publicKey, text);
        System.out.println(encode);
        //加签
        String sign = doSign(privateKey, text);

        //String decode = decryptAsymmetrically(privateKey, encode);
        //System.out.println(decode);
        //验签
        //System.out.println(doVerify(publicKey, decode , sign));

        String decode1 = RSAUtils.decryptByPrivateKey(encode);

        System.out.println(decode1);
    }
}
