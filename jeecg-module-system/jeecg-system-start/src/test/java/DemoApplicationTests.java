import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.mq.service.ProducerMqService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/27 14:45
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JeecgSystemApplication.class)
public class DemoApplicationTests {

    @Autowired
    private ProducerMqService disruptorMqService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 项目内部使用Disruptor做消息队列
     * @throws Exception
     */
    @Test
    public void sayHelloMqTest() throws Exception{
        disruptorMqService.sayHelloMq("消息到了，Hello world!");
        log.info("消息队列已发送完毕");
        //这里停止2000ms是为了确定是处理消息是异步的
        Thread.sleep(2000);
    }

    @Test
    public void execute(){
        String patientKey = "patient_*";
        Set<String> stringSet = redisTemplate.keys(patientKey);
        for (String key : stringSet) {
            System.out.println(key);
        }

    }
}
