package org.jeecg.modules.mo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.common.entity.Patient;
import org.jeecg.modules.common.service.IPatientService;
import org.jeecg.modules.mo.service.IWeixinService;
import org.jeecgframework.minidao.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: ai_member
 * @Author: jeecg-boot
 * @Date:   2023-04-21
 * @Version: V1.0
 */
@Service
@Slf4j
public class WeixinServiceImpl implements IWeixinService {
    @Value("${wechat.secret}")
    private String secret;
    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.grant_type}")
    private String grantType;
    @Value("${wechat.accessTokenUrl}")
    private String accessTokenUrl;
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;
    @Resource
    private IPatientService patientService;
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private final static String OPEN_QRCODE_URL= "https://open.weixin.qq.com/connect/qrconnect";

    @Override
    public Map<String, Object> getOpenidByCode(String code, String ip) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object>  hashMap = new HashMap<>();
        param.put("secret", secret);
        param.put("appid", appid);
        param.put("code", code);
        param.put("grant_type", "authorization_code");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(accessTokenUrl, param));
        log.info("获取openId，返回信息：{}", jsonObject);
        String openid = String.valueOf(jsonObject.get("openid"));
        String accessToken = String.valueOf(jsonObject.get("access_token"));
        Patient patient = patientService.getOne(new QueryWrapper<Patient>().eq("open_id", openid));
        if (patient == null) {
            patient = new Patient();
            getAuthUserInfo(accessToken, openid, patient);//获取用户授权信息
            patient.setOpenId(openid);
            patientService.save(patient);
        }else {
            Patient patientUpdate = new Patient();
            if (StrUtil.isEmpty(patient.getAvatar())) {
                log.info("已关注用户授权获取信息：{}",openid);
                getAuthUserInfo(accessToken, openid, patient);//获取用户授权信息，通过memberUpdate值传递
            }
            patientUpdate.setId(patient.getId());
            patientUpdate.setNickName(patient.getNickName());
            patientUpdate.setAvatar(patient.getAvatar());
            patientUpdate.setSex(patient.getSex());
            patientService.updateById(patientUpdate);
        }
        hashMap.put("openid", patient.getOpenId());
        hashMap.put("sex", patient.getSex());
        hashMap.put("avatar", patient.getAvatar());
        hashMap.put("nickName", patient.getNickName());
        return hashMap;
    }

   /*
    *@Description: 获取用户授权信息
    *@Param: [accessToken, openid, patient]
    *@Return: void
    *@author: xiaopeng.wu
    *@DateTime: 13:46 2023/12/5
   **/
    private void getAuthUserInfo(String accessToken, String openid, Patient patient){
        Map<String, Object>  userMap = new HashMap<>();
        userMap.put("access_token", accessToken);
        userMap.put("openid", openid);
        userMap.put("lang", "zh_CN");
        JSONObject jsonUserInfo = JSONObject.parseObject(HttpUtil.get(userInfoUrl, userMap));//获取用户信息
        String nickname = String.valueOf(jsonUserInfo.get("nickname"));
        String headimgurl = String.valueOf(jsonUserInfo.get("headimgurl"));
        log.info("获取用户信息，返回信息：{}", jsonUserInfo);
        patient.setNickName(nickname);
        patient.setAvatar(headimgurl);
        patient.setSex(jsonUserInfo.get("sex") != null ? Integer.parseInt(jsonUserInfo.get("sex").toString()) : 0);
    }

    public static void main(String[] args) {
        Map<String, Object>  userMap = new HashMap<>();
        userMap.put("appid", "wx81e553a8dd7f01f1");
        userMap.put("redirect_uri", "http://36.41.65.31:8090/jeecg-boot/mo/wx/loginAuthCallback");
        userMap.put("response_type", "code");
        userMap.put("scope", "snsapi_login");
        userMap.put("state", SnowflakeIdWorker.generateId() + "#wechat_redirect");
        JSONObject jsonUserInfo = JSONObject.parseObject(HttpUtil.get(OPEN_QRCODE_URL, userMap));//获取用户信息
        System.out.println(jsonUserInfo);
    }

}
