package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description:设置请求信息
 * @author: XiaoPeng Wu
 * @create: 2021-12-02 17:50
 **/
@Component
@Slf4j
public class SpringHttpUtil {
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;


    public String getHeader(String name) {
        String result = request.getHeader(name);
        if (StringUtils.isBlank(result)) {
            result = request.getParameter(name);
        }
        return result;
    }


    public void setHeader(Map<String, String> map) {
        if (map == null || map.isEmpty() || map.size() == 0)
            return;
        map.entrySet().stream().forEach(x -> {
            response.setHeader(x.getKey(), x.getValue());
        });
    }

    public void setHeader(String key, String value) {
        response.setHeader(key, value);
    }
}
