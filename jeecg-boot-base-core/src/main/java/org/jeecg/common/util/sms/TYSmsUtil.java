package org.jeecg.common.util.sms;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 天翼云短信发送工具
 * @author: xiaopeng.wu
 * @create: 2024/2/22 16:08
 **/
@Slf4j
public class TYSmsUtil {
    /*
     *@Description: 用户注册
     *@Param: [phone, code]
     *@Return: java.lang.Boolean
     *@author: xiaopeng.wu
     *@DateTime: 9:42 2024/2/23
    **/
    public static Boolean sendRegister(String phone, String code){
        SendCtyunSmsRequest sendCtyunSmsRequest = new SendCtyunSmsRequest();
        sendCtyunSmsRequest.setPhoneNumber(phone);
        sendCtyunSmsRequest.setSignName("助健康导航服务");
        sendCtyunSmsRequest.setTemplateCode("SMS85490385009");
        sendCtyunSmsRequest.setTemplateParam("{\"code\":" + code + "}");
        return sendSms(sendCtyunSmsRequest);
    }
    
    /*
     *@Description: 密码重置
     *@Param: [phone, code]
     *@Return: java.lang.Boolean
     *@author: xiaopeng.wu
     *@DateTime: 9:42 2024/2/23
    **/
    public static Boolean sendPassword(String phone, String code){
        SendCtyunSmsRequest sendCtyunSmsRequest = new SendCtyunSmsRequest();
        sendCtyunSmsRequest.setPhoneNumber(phone);
        sendCtyunSmsRequest.setSignName("助健康导航服务");
        sendCtyunSmsRequest.setTemplateCode("SMS62059749657");
        sendCtyunSmsRequest.setTemplateParam("{\"code\":" + code + "}");
        return sendSms(sendCtyunSmsRequest);
    }

    /*
     *@Description: 审核通过通知
     *@Param: [phone, code]
     *@Return: java.lang.Boolean
     *@author: xiaopeng.wu
     *@DateTime: 14:59 2024/2/26
    **/
    public static Boolean sendPassAudit(String phone){
        SendCtyunSmsRequest sendCtyunSmsRequest = new SendCtyunSmsRequest();
        sendCtyunSmsRequest.setPhoneNumber(phone);
        sendCtyunSmsRequest.setSignName("助健康导航服务");
        sendCtyunSmsRequest.setTemplateCode("SMS65766451886");
        return sendSms(sendCtyunSmsRequest);
    }
    
    /*
     *@Description: 审核未通过通知
     *@Param: [phone, name]
     *@Return: java.lang.Boolean
     *@author: xiaopeng.wu
     *@DateTime: 15:00 2024/2/26
    **/
    public static Boolean sendNoPassAudit(String phone){
        SendCtyunSmsRequest sendCtyunSmsRequest = new SendCtyunSmsRequest();
        sendCtyunSmsRequest.setPhoneNumber(phone);
        sendCtyunSmsRequest.setSignName("助健康导航服务");
        sendCtyunSmsRequest.setTemplateCode("SMS63633198024");
        return sendSms(sendCtyunSmsRequest);
    }

    private static Boolean sendSms(SendCtyunSmsRequest sendCtyunSmsRequest) {
        // 获取accessKey和securityKey
        String accessKey = "8d7af26db83846c6a5936d35fa5c1347";  // 填写控制台->个人中心->用户AccessKey->查看->AccessKey
        String securityKey ="7227480d06774d568a8a50da568011ba"; // 填写控制台->个人中心->用户AccessKey->查看->SecurityKey

        // 构造body请求参数
        Map<String, String> params = buildParams(sendCtyunSmsRequest);
        String body = JSONObject.toJSONString(params);
        try {
            // 构造时间戳
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date now = new Date();
            String signatureTime = timeFormat.format(now);
            String signatureDate = dateFormat.format(now);

            // 构造请求流水号
            String uuId = UUID.randomUUID().toString();

            // 构造待签名字符串
            String campHeader = String.format("ctyun-eop-request-id:%s\neop-date:%s\n", uuId, signatureTime);
            // header的key按照26字母进行排序, 以&作为连接符连起来
            URL url = new URL("https://sms-global.ctapi.ctyun.cn/sms/api/v1");
            String query = url.getQuery();
            StringBuilder afterQuery = new StringBuilder();
            if (query != null) {
                String[] param = query.split("&");
                Arrays.sort(param);
                for (String str : param) {
                    if (afterQuery.length() < 1){
                        afterQuery.append(str);
                    } else{
                        afterQuery.append("&").append(str);
                    }
                }
            }

            // 报文原封不动进行sha256摘要
            String calculateContentHash = getSHA256(body);
            String signatureStr = campHeader + "\n" + afterQuery + "\n" + calculateContentHash;

            // 构造签名
            byte[] kTime = hmacSHA256(signatureTime.getBytes(), securityKey.getBytes());
            byte[] kAk = hmacSHA256(accessKey.getBytes(), kTime);
            byte[] kDate = hmacSHA256(signatureDate.getBytes(), kAk);
            String signature = Base64.getEncoder().encodeToString(hmacSHA256(signatureStr.getBytes(StandardCharsets.UTF_8), kDate));

            // 构造请求头
            HttpPost httpPost = new HttpPost(String.valueOf(url));
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("ctyun-eop-request-id", uuId);
            httpPost.setHeader("Eop-date", signatureTime);
            String signHeader = String.format("%s Headers=ctyun-eop-request-id;eop-date Signature=%s", accessKey, signature);
            httpPost.setHeader("Eop-Authorization", signHeader);

            httpPost.setEntity(new StringEntity(body, ContentType.create("application/json", "utf-8")));

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost) ) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                log.info("天翼云短信发送返回结果：{}", result);
                return true;
            } catch (Exception e) {
                log.error("天翼云短信发送失败：{}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("天翼云短信发送失败：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 构造请求参数
     * @param sendCtyunSmsRequest 请求参数
     * @return Map
     */
    private static Map<String, String> buildParams(SendCtyunSmsRequest sendCtyunSmsRequest) {
        Map<String, String> params = new HashMap<>(16);
        params.put("action", "SendSms");
        params.put("phoneNumber", sendCtyunSmsRequest.getPhoneNumber());
        params.put("signName", sendCtyunSmsRequest.getSignName());
        params.put("templateCode", sendCtyunSmsRequest.getTemplateCode());
        params.put("templateParam", sendCtyunSmsRequest.getTemplateParam());
        params.put("extendCode", sendCtyunSmsRequest.getExtendCode());
        params.put("sessionId", sendCtyunSmsRequest.getSessionId());
        return params;
    }

    private static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        byte[] var2 = data;
        int var3 = data.length;
        for (int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                sb.append("0");
            } else if (hex.length() == 8) {
                hex = hex.substring(6);
            }
            sb.append(hex);
        }
        return sb.toString().toLowerCase(Locale.getDefault());
    }

    private static String getSHA256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException var3) {
            return null;
        }
    }

    private static byte[] hmacSHA256(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
