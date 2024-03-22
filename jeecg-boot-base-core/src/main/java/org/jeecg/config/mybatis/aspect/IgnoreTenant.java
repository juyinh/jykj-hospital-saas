package org.jeecg.config.mybatis.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IgnoreTenant {

    /**
     * true为不做租户隔离 false为做租户隔离
     * @return
     */
    boolean isIgnore() default true;
}
