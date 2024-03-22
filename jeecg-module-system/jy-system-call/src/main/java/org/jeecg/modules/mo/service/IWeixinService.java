package org.jeecg.modules.mo.service;

import java.util.Map;

/**
 * @Description: ai_member
 * @Author: jeecg-boot
 * @Date:   2023-04-21
 * @Version: V1.0
 */
public interface IWeixinService {

    Map<String, Object> getOpenidByCode(String code, String ip);
}
