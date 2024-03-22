package org.jeecg.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @author: XiaoPeng Wu
 * @create: 2021-12-09 10:02
 **/
@Slf4j
@Configuration
public class WebFilterConfig {
    /**
     * 配置过滤器
     * order属性:控制过滤器加载顺序：数字越小，加载越早
     * @return
     */
    @Bean
    public FilterRegistrationBean ValidatorFilterRegistration() {
        //新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加我们写好的过滤器
        registration.setFilter( new DecodeFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/mo/*");
        registration.setOrder(Integer.MAX_VALUE - 10);
        log.info("==================配置解密拦截器====================");
        return registration;
    }
}
