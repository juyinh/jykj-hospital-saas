package org.jeecg.config.mybatis;

import lombok.extern.slf4j.Slf4j;

/*
 *@Description: 定义一个ThreadLocal本地线程变量 MybatisTenantContext用于维护是否开启租户隔离变量
 *@Param:
 *@Return:
 *@author: xiaopeng.wu
 *@DateTime: 9:34 2024/1/8
 **/
@Slf4j
public class MybatisTenantContext {
    private static final ThreadLocal<Boolean> TENANT_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static Boolean get() {
        return TENANT_CONTEXT_THREAD_LOCAL.get();
    }

    public static void set(boolean isIgnore) {
        TENANT_CONTEXT_THREAD_LOCAL.set(isIgnore);
    }

    public static void clear() {
        TENANT_CONTEXT_THREAD_LOCAL.remove();
    }
}
