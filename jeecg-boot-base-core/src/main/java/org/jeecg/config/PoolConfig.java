package org.jeecg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description: 线程池配置
 * @author: xiaopeng.wu
 * @create: 2024/2/27 10:17
 **/

@Configuration
@EnableAsync //开启注解
public class PoolConfig {
//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        // 设置核心线程数
//        executor.setCorePoolSize(10);
//        // 设置最大线程数
//        executor.setMaxPoolSize(15);
//        // 设置队列容量
//        executor.setQueueCapacity(20);
//        // 设置线程活跃时间（秒）
//        executor.setKeepAliveSeconds(60);
//        // 设置默认线程名称
//        executor.setThreadNamePrefix("zszxz-");
//        // 设置拒绝策略
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        // 等待所有任务结束后再关闭线程池
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        return executor;
//    }

}
