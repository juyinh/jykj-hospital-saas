package org.jeecg.modules.mq.service;

import org.jeecg.modules.mo.dto.MoClinicAppointAddDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueueAddDTO;

/**
 * @Description: 构造Mqservice和实现类-生产者
 * @author: xiaopeng.wu
 * @create: 2023/11/27 14:02
 **/
public interface ProducerMqService {
    /**
     * 消息
     * @param message
     */
    void sayHelloMq(String message);

    /*
     *@Description: 预约挂号MQ
     *@Param:
     *@Return:
     *@author: xiaopeng.wu
     *@DateTime: 15:47 2023/11/29
    **/
    void patientAppointMq(MoClinicAppointAddDTO appointAddDTO);

    /*
     *@Description: 用户预约排队
     *@Param: [appointAddDTO]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 9:24 2023/12/5
    **/
    void patientAppointQueueMq(MoClinicAppointQueueAddDTO appointQueueAddDTO);
}
