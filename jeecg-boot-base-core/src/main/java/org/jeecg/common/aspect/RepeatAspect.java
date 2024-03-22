package org.jeecg.common.aspect;

/**
 * @author zyf
 */

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.common.aspect.annotation.Repeat;
import org.jeecg.common.exception.JeecgCloudException;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复提交分布式锁拦截器
 *
 * @author 2019年6月18日
 */
@Aspect
@Component
@Slf4j
public class RepeatAspect{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /***
     * 定义controller切入点拦截规则，拦截JRepeat注解的业务方法
     */
    @Pointcut("@annotation(repeat)")
    public void pointCut(Repeat repeat) {
    }

    /**
     * AOP分布式锁拦截
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    @Around("pointCut(repeat)")
    public Object repeatSubmit(ProceedingJoinPoint joinPoint, Repeat repeat) throws Throwable {
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
        if (Objects.nonNull(repeat)) {
            // 获取参数
            Object[] args = joinPoint.getArgs();
            String key = getValueBySpEL(repeat.lockKey(), parameterNames, args,"RepeatSubmit").get(0);
            // 如果成功获取到锁就继续执行
            if (!redisTemplate.hasKey(key)) {
                redisTemplate.opsForValue().set(key, 1, 30, TimeUnit.SECONDS);//设置30秒过期
                // 执行进程
                return joinPoint.proceed();
            } else {
                if (StrUtil.isEmpty(repeat.msg())) {
                    // 未获取到锁
                    throw new JeecgCloudException("请勿重复提交");
                }else {
                    throw new JeecgCloudException(repeat.msg());
                }
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 通过spring SpEL 获取参数
     *
     * @param redisKey            定义的key值 以#开头 例如:#user
     * @param parameterNames 形参
     * @param values         形参值
     * @param keyConstant    key的常亮
     * @return
     */
    public List<String> getValueBySpEL(String redisKey, String[] parameterNames, Object[] values, String keyConstant) {
        // 进行一些参数的处理，比如获取订单号，操作人id等
        StringBuffer lockKeyBuffer = new StringBuffer();
        List<String> keys = new ArrayList<>();
        String[] keyArr = redisKey.split("_");
        for (String key : keyArr) {
            if (!key.contains("#")) {
                String s = "redis:lock:" + key + keyConstant;
                log.debug("lockKey:" + s);
                keys.add(s);
                return keys;
            }
            //spel解析器
            ExpressionParser parser = new SpelExpressionParser();
            //spel上下文
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], values[i]);
            }
            Expression expression = parser.parseExpression(key);
            Object value = expression.getValue(context);
            if (value != null) {
                if (value instanceof List) {
                    List value1 = (List) value;
                    for (Object o : value1) {
                        addKeys(keys, o, keyConstant);
                    }
                } else if (value.getClass().isArray()) {
                    Object[] obj = (Object[]) value;
                    for (Object o : obj) {
                        addKeys(keys, o, keyConstant);
                    }
                } else {
                    lockKeyBuffer.append(oConvertUtils.getString(value) + "_");
                }
            }
            log.info("表达式key={},value={}", key, keys);
        }
        addKeys(keys, lockKeyBuffer, keyConstant);
        return keys;
    }

    private void addKeys(List<String> keys, Object o, String keyConstant) {
        keys.add("redis:lock:" + o.toString() + keyConstant);
    }
}
