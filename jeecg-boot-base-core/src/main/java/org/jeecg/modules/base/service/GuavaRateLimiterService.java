package org.jeecg.modules.base.service;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/28 9:32
 **/
public class GuavaRateLimiterService {
    /*每秒控制5个许可*/
    RateLimiter rateLimiter = RateLimiter.create(5.0);

    /**
     * 获取令牌
     *
     * @return
     */
    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }

}
