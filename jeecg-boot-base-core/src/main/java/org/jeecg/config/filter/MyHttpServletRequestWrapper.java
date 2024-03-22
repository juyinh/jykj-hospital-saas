package org.jeecg.config.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:设置参数值
 * @author: XiaoPeng Wu
 * @create: 2021-12-09 9:37
 **/
@Slf4j
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    // 用于存储请求参数
    private Map<String, String[]> params = new HashMap<String, String[]>();

    private byte[] body;

    // 构造方法
    public MyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        log.info("MyHttpServletRequestWrapper");
        // 把请求参数添加到我们自己的map当中
        this.params.putAll(request.getParameterMap());

        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    public String getBody() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public void setBody(String newBody) {
        // Set the modified body
        this.body = newBody.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 处理@RequestBody再次读取流信息失败问题
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


    /**
     * 添加参数到map中
     *
     * @param extraParams
     */
    public void setParameterMap(Map<String, Object> extraParams) {
        for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加参数到map中
     *
     * @param name
     * @param value
     */
    public void setParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }

    /**
     * 重写getParameter，代表参数从当前类中的map获取
     *
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    /**
     * 重写getParameterValues方法，从当前类的 map中取值
     *
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }
}
