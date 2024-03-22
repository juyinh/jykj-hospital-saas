package org.jeecg.modules.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.mo.dto.MoClinicAppointAddDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueueAddDTO;
import org.jeecg.modules.mq.config.MessageModel;
import org.jeecg.modules.mq.service.ProducerMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/27 14:04
 **/
@Slf4j
@Service
public class ProducerMqServiceImpl implements ProducerMqService {
    @Autowired
    private RingBuffer<MessageModel> messageModelRingBuffer;

    @Override
    public void sayHelloMq(String message) {
        log.info("record the message: {}",message);
        //获取下一个Event槽的下标
        long sequence = messageModelRingBuffer.next();
        try {
            //给Event填充数据
            MessageModel event = messageModelRingBuffer.get(sequence);
            event.setMessage(message);
            log.info("往消息队列中添加消息：{}", event);
        } catch (Exception e) {
            log.error("failed to add event to messageModelRingBuffer for : e = {},{}",e,e.getMessage());
        } finally {
            //发布Event，激活观察者去消费，将sequence传递给改消费者
            //注意最后的publish方法必须放在finally中以确保必须得到调用；如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            messageModelRingBuffer.publish(sequence);
        }
    }

    @Override
    public void patientAppointMq(MoClinicAppointAddDTO appointAddDTO) {
        log.info("患者预约挂号MQ: {}", appointAddDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("mqName", "clinicAppoint");
        map.put("mqData", appointAddDTO);
        //获取下一个Event槽的下标
        long sequence = messageModelRingBuffer.next();
        try {
            //给Event填充数据
            MessageModel event = messageModelRingBuffer.get(sequence);
            event.setMessage(JSON.toJSONString(map));
            log.info("往消息队列中添加消息：{}", event);
        } catch (Exception e) {
            log.error("failed to add event to messageModelRingBuffer for : e = {},{}",e,e.getMessage());
        } finally {
            //发布Event，激活观察者去消费，将sequence传递给改消费者
            //注意最后的publish方法必须放在finally中以确保必须得到调用；如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            messageModelRingBuffer.publish(sequence);
        }
    }

    @Override
    public void patientAppointQueueMq(MoClinicAppointQueueAddDTO appointQueueAddDTO) {
        log.info("患者预约挂号MQ: {}", appointQueueAddDTO);
        Map<String, Object> map = new HashMap<>();
        map.put("mqName", "clinicAppointQueue");
        map.put("mqData", appointQueueAddDTO);
        //获取下一个Event槽的下标
        long sequence = messageModelRingBuffer.next();
        try {
            //给Event填充数据
            MessageModel event = messageModelRingBuffer.get(sequence);
            event.setMessage(JSON.toJSONString(map));
            log.info("往消息队列中添加消息：{}", event);
        } catch (Exception e) {
            log.error("failed to add event to messageModelRingBuffer for : e = {},{}",e,e.getMessage());
        } finally {
            //发布Event，激活观察者去消费，将sequence传递给改消费者
            //注意最后的publish方法必须放在finally中以确保必须得到调用；如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            messageModelRingBuffer.publish(sequence);
        }
    }
}
