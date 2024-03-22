package org.jeecg.modules.mo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.api.WxMpService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.IpUtils;
import org.jeecg.modules.common.entity.Patient;
import org.jeecg.modules.common.service.IPatientService;
import org.jeecg.modules.mo.service.IWeixinService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: XiaoPeng Wu
 * @create: 2023-04-21 16:37
 * @Description:微信公众号验证使用
 **/
@Api(tags = "C端-公众号信息管理")
@RestController
@RequestMapping("/mo/wx")
@Slf4j
@AllArgsConstructor
public class WXAuthController {
    private final IWeixinService weixinService;
    private final WxMpService wxService;
    private final IPatientService patientService;
    /**
     * 微信开放平台二维码连接
     */
    private final static String OPEN_QRCODE_URL= "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    @ApiOperation(value = "获取用户微信信息")
    @GetMapping(value = "/getPatientInfo")
    public Result<Patient> getPatientInfo(@RequestHeader(name = "openId") String openId) {
        log.info("获取用户信息openId：{}", openId);
        Patient result = patientService.getOne(new LambdaQueryWrapper<Patient>().eq(Patient::getOpenId, openId));
        return Result.OK(result);
    }


    @ApiOperation(value = "获取用户openid")
    @GetMapping(value = "/getOpenidByCode")
    public Result getOpenidByCode(@RequestParam(name = "code") String code, HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        log.info("获取用户openid的code：{}", code);
        log.info("获取用户openid的ip：{}", ip);
        Map<String, Object> result = weixinService.getOpenidByCode(code, ip);
        return Result.OK(result);
    }

    @ApiOperation(value = "扫码授权")
    @GetMapping(value = "/getScanQrAuth")
    public Result getScanQrAuth(@RequestParam("redirectUrl") String redirectUrl, HttpServletResponse response) {
        WxJsapiSignature wxJsapiSignature = new WxJsapiSignature();
        try {
            wxJsapiSignature = wxService.createJsapiSignature(redirectUrl);
            log.info("扫码授权:{}", wxJsapiSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.OK(wxJsapiSignature);
    }
}
