package org.jeecg.modules.pc.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mq.service.ProducerMqService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/27 14:43
 **/
@Slf4j
@RestController
@RequestMapping("/pc/test")
@RequiredArgsConstructor
public class TestController {
    private final ProducerMqService disruptorMqService;

    //原子性保证线程安全
    private static AtomicInteger a = new AtomicInteger(1);

    /*每秒控制5个许可，每秒以固定的速率输出令牌，以达到平滑输出的效果*/
    RateLimiter rateLimiter = RateLimiter.create(10.0);

    @GetMapping("/sayHi")
    public String sayHi(String name) throws InterruptedException {
        if (!rateLimiter.tryAcquire()) {
            return "服务拥挤中，请稍微再试";
        }
        //Thread.sleep(2000);
        System.out.println("成功");
        return "hi,"+name;
    }

    @GetMapping("/disruptorMsg")
    public String disruptorMsg() throws InterruptedException {
        disruptorMqService.sayHelloMq("消息到了，消息：" + a.getAndIncrement());
        log.info("消息队列已发送完毕");
        //这里停止2000ms是为了确定是处理消息是异步的
        Thread.sleep(2000);
        return "success";
    }

    @GetMapping("/queryDoctor")
    //@AkaliHot(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 1, duration = 5)
    public Result queryDoctor(){
        return Result.ok(a.getAndIncrement());
    }


    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1);
        while(true){
            System.out.println("time:" + rateLimiter.acquire() + "s");
        }
    }
}
