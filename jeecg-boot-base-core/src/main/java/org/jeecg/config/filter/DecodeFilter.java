package org.jeecg.config.filter;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RSAUtils;
import org.jeecg.common.util.SpringHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/*
 *@Description: 解密拦截器
 *@Param: 
 *@Return: 
 *@author: xiaopeng.wu
 *@DateTime: 13:46 2024/1/16
**/
@Slf4j
public class DecodeFilter implements Filter {
    @Autowired
    private SpringHttpUtil springHttpUtil;
    /**
     * 需要过滤的地址
     */
    private static List<String> urlList = Arrays.asList("/subsystem/qa/query", "/subsystem/qa/healthz");

    /**
     * 是否不需要过滤
     *
     * @param requestUrl 请求的url
     * @return
     */
    public boolean isPast(String requestUrl) {
        for (String url : urlList) {
            if (requestUrl.equals(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String url = ((HttpServletRequest) request).getRequestURI().substring(((HttpServletRequest) request).getContextPath().length());
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info("请求url：{}", url);
        //通过地址对特定的请求进行处理，如果不需要可以不用，如果不用，就会对所有的请求进行过滤
        if (url.contains("/mo/")) {
            try {
                MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper((HttpServletRequest) request);
                String plaintext;
                // POST请求类型，才获取POST请求体
                if(CommonConstant.HTTP_GET.equals(requestWrapper.getMethod())){
                    //@TODO 解密参数匹配有问题，会有中括号
//                    String ciphertext = requestWrapper.getParameter("ciphertext");
//                    if (StrUtil.isNotBlank(ciphertext)) {
//                        ciphertext = customDecode(ciphertext);
//                        //解密、明文
//                        plaintext = RSAUtils.decryptByPrivateKey(ciphertext);
//                        log.info("解密参数plaintext:{}", plaintext);
//                        Map<String,Object> map = JSON.parseObject(plaintext, Map.class);
//                        requestWrapper.setParameterMap(map);
//                    }
                }else {
                    // 1.获取需要处理的参数
                    String ciphertext = requestWrapper.getBody();
                    ciphertext = customDecode(ciphertext);
                    plaintext = RSAUtils.decryptByPrivateKey(ciphertext);
                    log.info("解密参数plaintext:{}", plaintext);
                    requestWrapper.setBody(plaintext);
                }
//                String signature = requestWrapper.getHeader("signature");
//                log.info("获取参数signature:{}", signature);
//                Boolean isVerifier = RSAUtils.verify(plaintext, signature);
//                if (!isVerifier) {
//                    //throw new RuntimeException("验签失败");
//                }
                // 3.放行，把我们的requestWrapper放到方法当中
                chain.doFilter(requestWrapper, response);
                // 在视图页面返回给客户端之前执行，但是执行顺序在Interceptor之后
                //System.out.println("##############doInterceptor after##############");
            } catch (Exception ex) {
                log.error("filter异常：{}", ex);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public Object handleException(String errorMessage) {
        // 获取异常信息
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

    private static String customDecode(String encodedParam) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedParam.replace("+", "%2B"), StandardCharsets.UTF_8.toString());
    }
}
