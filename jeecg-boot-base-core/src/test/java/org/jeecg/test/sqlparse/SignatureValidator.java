package org.jeecg.test.sqlparse;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2024/1/12 11:03
 **/
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignatureValidator {

    private static final String TOKEN = "your_token"; // 你在公众号平台设置的Token

    public static boolean validateSignature(String signature, String timestamp, String nonce) {
        // 将token、timestamp、nonce三个参数按字典序排序
        String[] arr = {TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        // 将三个参数字符串拼接成一个字符串
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        // 对拼接后的字符串进行SHA1加密
        String encrypted = sha1(content.toString());
        // 将加密后的字符串与微信发送的signature进行对比
        return encrypted.equals(signature);
    }

    private static String sha1(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] result = digest.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // 示例：假设收到微信服务器的请求中包含以下参数
        String signature = "76a13da6c2f47b2c7a709206fb3ebd9304d71c08";
        String timestamp = "1609459603";
        String nonce = "123456789";

        // 调用验证方法
        boolean isValid = validateSignature(signature, timestamp, nonce);
        // 输出验证结果
        System.out.println("Is valid: " + isValid);
    }
}

