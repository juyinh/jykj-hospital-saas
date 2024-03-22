package org.jeecg.modules.mq.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.service.IDoctorWorkDetailService;
import org.jeecg.modules.mo.dto.MoClinicAppointAddDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueueAddDTO;
import org.jeecg.modules.mo.service.IMoClinicAppointService;
import org.jeecg.modules.mq.config.MessageModel;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: 构造EventHandler-消费者
 * @author: xiaopeng.wu
 * @create: 2023/11/27 13:55
 **/
@Service
@Slf4j
public class ConsumerEventHandler implements EventHandler<MessageModel> {
    private RedisUtil redisUtil;
    private RedissonLockClient redissonLockClient;
    private IMoClinicAppointService iMoClinicAppointService;
    private IDoctorWorkDetailService doctorWorkDetailService;
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onEvent(MessageModel event, long sequence, boolean endOfBatch) {
        try {
            log.info("消费者处理消息开始");
            if (event != null) {
                log.info("消费者消费的信息是：{}", event);
                JSONObject jsonObject = JSONObject.parseObject(event.getMessage());
                String mqName = jsonObject.getString("mqName");
                if ("clinicAppoint".equals(mqName)) {
                    //预约
                    doPatientAppoint(jsonObject.getJSONObject("mqData"));
                }
                if ("clinicAppointQueue".equals(mqName)) {
                    //预约排队
                    doPatientQueue(jsonObject.getJSONObject("mqData"));
                }

            }
        } catch (Exception e) {
            log.info("消费者处理消息失败", e);
        }
        log.info("消费者处理消息结束");
    }

    private void initObject() {
        if (redisUtil == null) {
            redisUtil = SpringContextUtils.getBean(RedisUtil.class);
        }
        if (redissonLockClient == null) {
            redissonLockClient = SpringContextUtils.getBean(RedissonLockClient.class);
        }
        if (iMoClinicAppointService == null) {
            iMoClinicAppointService = SpringContextUtils.getBean(IMoClinicAppointService.class);
        }
        if (doctorWorkDetailService == null) {
            doctorWorkDetailService = SpringContextUtils.getBean(IDoctorWorkDetailService.class);
        }
        if (redisTemplate == null) {
            redisTemplate = SpringContextUtils.getBean("redisTemplate", RedisTemplate.class);
        }
    }

    /*
     *@Description: 用户预约
     *@Param: []
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 17:48 2023/11/29
     **/
    private void doPatientAppoint(JSONObject jsonObject) {
        initObject();
        MoClinicAppointAddDTO appointAddDTO = jsonObject.toJavaObject(MoClinicAppointAddDTO.class);
        String appointDay = appointAddDTO.getAppointDay();
        String lockKey = CommonConstant.PREFIX_PATIENT_APPOINT_LOCK + appointAddDTO.getDoctorId() + "_" + appointDay;
        RLock lock = redissonLockClient.getLock(lockKey);
        try {
            //使用锁
            if (redissonLockClient.tryLock(lockKey, 10, 30)) {//等待10秒
                Long count = iMoClinicAppointService.count(new QueryWrapper<ClinicAppoint>().lambda()
                        .eq(ClinicAppoint::getId, appointAddDTO.getAppointId())
                        .eq(ClinicAppoint::getAppointStatus, 2));
                if (count != 0) {
                    //已经预约
                    log.warn("已预约：{}", appointAddDTO.getCardId());
                    //@TODO 发送预约失败消息
                    return;
                }
                String patientAppointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_List + appointAddDTO.getDoctorId() + "_" +
                        appointAddDTO.getWorkPeriod() + "_" + appointDay;
                redisTemplate.opsForSet().add(patientAppointListKey, appointAddDTO.getCardId());//添加数据
                iMoClinicAppointService.addPatientAppoint(appointAddDTO);
                //@TODO 添加预约详情
            } else {
                log.info("用户预约获取锁失败：{}", appointAddDTO.getCardId());
            }
        } catch (Exception e) {
            log.info("患者预约失败：{}", e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                log.info("释放锁成功：{}", lockKey);
                lock.unlock();
            }
        }
    }

    /*
     *@Description: 用户扫码排队
     *@Param: [jsonObject]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 10:00 2023/12/5
     **/
    private void doPatientQueue(JSONObject jsonObject) {
        initObject();
        MoClinicAppointQueueAddDTO queueAddDTO = jsonObject.toJavaObject(MoClinicAppointQueueAddDTO.class);
        String appointDay = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //排队锁
        String lockKey = CommonConstant.PREFIX_PATIENT_QUEUE_LOCK + queueAddDTO.getDoctorId() + "_" + appointDay;
        //未预约排队,现场扫码排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + queueAddDTO.getDoctorId() + "_" + appointDay;
        //预约排队
        String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + queueAddDTO.getDoctorId() + "_" + appointDay;
        //排队顺序序号
        String queueNumKey = CommonConstant.PREFIX_PATIENT_QUEUE_NUM + queueAddDTO.getDoctorId() + "_" + appointDay;
        RLock queueLock = redissonLockClient.getLock(lockKey);
        try {
            //排队队列是否存在
            Long queueCount = null;
            if (StrUtil.isBlank(queueAddDTO.getAppointId())) {
                queueCount = redisTemplate.opsForList().indexOf(queueListKey, queueAddDTO.getCardId());
            }else {
                queueCount = redisTemplate.opsForList().indexOf(appointListKey, queueAddDTO.getCardId());
            }
            if (queueCount != null) {
                //已经预约
                log.warn("患者已经预约：{}", queueAddDTO.getCardId());
                return;
            }

            if (redissonLockClient.tryLock(lockKey, 10, 30)) {//等待10秒，30秒自动释放
                Object numObject = redisUtil.get(queueNumKey);
                Integer initQueueNum = 1;
                if (numObject != null) {
                    initQueueNum = oConvertUtils.getInt(redisUtil.incr(queueNumKey, 1));
                } else {
                    redisUtil.set(queueNumKey, initQueueNum);
                }
                ClinicAppoint clinicAppoint = BeanUtil.copyProperties(queueAddDTO, ClinicAppoint.class);
                if (StrUtil.isBlank(queueAddDTO.getAppointId())) {
                    //@ TODO 执行相关操作
                    //现场排队,预约状态为未预约
                    clinicAppoint.setAppointStatus(0);
                    clinicAppoint.setClinicStatus(1);//排队中
                    //这里取预约序号和排队序号一致，
                    clinicAppoint.setAppointNumber(initQueueNum);
                    clinicAppoint.setQueueNumber(initQueueNum);
                    clinicAppoint.setAppointDay(new Date());
                    clinicAppoint.setOpenId(clinicAppoint.getOpenId());
                    iMoClinicAppointService.save(clinicAppoint);
                    redisUtil.lSet(queueListKey, clinicAppoint.getCardId());
                    //@TODO 推送排队号
                } else {
                    clinicAppoint.setId(queueAddDTO.getAppointId());
                    clinicAppoint.setQueueNumber(initQueueNum);
                    clinicAppoint.setClinicStatus(1);//排队中
                    clinicAppoint.setOpenId(queueAddDTO.getOpenId());
                    iMoClinicAppointService.updatePatientAppoint(clinicAppoint);
                    //排队
                    redisUtil.lSet(appointListKey, clinicAppoint.getCardId());
                    //@TODO 推送排队号
                }
            }
        } catch (Exception e) {
            log.info("患者预约失败：{}", e);
        } finally {
            if (queueLock != null && queueLock.isHeldByCurrentThread()) {
                log.info("释放锁成功：{}", lockKey);
                queueLock.unlock();
            }
        }
    }
}
