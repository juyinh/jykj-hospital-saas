package org.jeecg.config.filter;

import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RSAUtils;
import org.jeecg.common.util.SpringHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.net.URLDecoder;

/**
 * @Description:加密拦截器
 * @author: XiaoPeng Wu
 * @create: 2021-12-07 16:41
 **/
@Order(1)
@ControllerAdvice(basePackages = {"org.jeecg.modules.mo"})
@Slf4j
public class EncodeHandler implements ResponseBodyAdvice<Object> {
    @Autowired
    private SpringHttpUtil springHttpUtil;
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //supports返回真才需要进入beforeBodyWrite进行进一步处理
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //@TODO 注意这里返回类型要和方法体相同
        if (body instanceof Result) {
            springHttpUtil.setHeader("signature", RSAUtils.sign(JSONUtil.toJsonStr(body)));
            log.info("原文内容：{}", JSONUtil.toJsonStr(body));
            String encodeData = RSAUtils.encryptByPublicKey(JSONUtil.toJsonStr(body));
            String decodeData = RSAUtils.decryptByPrivateKey(encodeData);
            decodeData = URLDecoder.decode(decodeData, "utf-8");
            log.info("公钥加密内容：{}", encodeData);
            log.info("公钥解密内容：{}", decodeData);
            return encodeData;
        }
        return body;
    }




    /*
     *@Description: 统一异常处理
     *@Param: [request, ex]
     *@Return: java.lang.Object
     *@author: xiaopeng.wu
     *@DateTime: 15:36 2024/1/12
    **/
    @ExceptionHandler(value =Exception.class)
    //@ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Object handleException(Exception ex) {
        log.error("全局异常获取：{}", ex);
        // 获取异常信息
        String errorMessage = ex.getMessage();
        Result result = new Result();
        result.error500(errorMessage);
        String jsonStr = JSONUtil.toJsonStr(result);
        try {
            //获取验签
            springHttpUtil.setHeader("signature", RSAUtils.sign(jsonStr));
            return RSAUtils.encryptByPublicKey(jsonStr);
        } catch (Exception e) {
            // 处理加密异常时的错误
            log.error("加密异常信息时发生错误：{}", e);
        }
        return null;
    }
}
