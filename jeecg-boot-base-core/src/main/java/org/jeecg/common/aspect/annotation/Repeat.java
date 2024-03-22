package org.jeecg.common.aspect.annotation;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/12/8 9:40
 **/
import java.lang.annotation.*;

/*
 *@Description: 防止重复提交
 *@Param: 
 *@Return: 
 *@author: xiaopeng.wu
 *@DateTime: 10:25 2023/12/8
**/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Repeat {

    /**
     * 超时时间
     *
     * @return
     */
    int lockTime();


    /**
     * redis 锁key的
     *
     * @return redis 锁key
     */
    String lockKey() default "";

    /*
     *@Description: 提示消息
     *@Param: []
     *@Return: java.lang.String
     *@author: xiaopeng.wu
     *@DateTime: 13:50 2023/12/8
    **/
    String msg() default "";
}
