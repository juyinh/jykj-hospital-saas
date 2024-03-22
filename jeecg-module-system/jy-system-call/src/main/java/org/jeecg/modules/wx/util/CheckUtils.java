package org.jeecg.modules.wx.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/12/5 11:33
 **/
public class CheckUtils {
    //private static final String token = "gzjychatai"; //这个token值要和服务器配置一致
    //private static final String token = "juyinTest"; //这个token值要和服务器配置一致

    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {

        String[] arr = new String[]{token, timestamp, nonce};
        // 排序
        Arrays.sort(arr);
        // 生成字符串
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }

        // sha1加密
        String temp = getSHA1String(content.toString());

        return temp.equals(signature); // 与微信传递过来的签名进行比较
    }

    private static String getSHA1String(String data) {
        // 使用commons codec生成sha1字符串
        return DigestUtils.shaHex(data);
    }
}
