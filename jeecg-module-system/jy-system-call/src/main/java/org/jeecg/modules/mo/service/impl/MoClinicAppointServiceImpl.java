package org.jeecg.modules.mo.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.common.entity.PatientCard;
import org.jeecg.modules.mo.dto.MoClinicAppointAddDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueueAddDTO;
import org.jeecg.modules.mo.mapper.MoClinicAppointMapper;
import org.jeecg.modules.mo.mapper.MoClinicRoomMapper;
import org.jeecg.modules.mo.service.IMoClinicAppointService;
import org.jeecg.modules.mo.service.IMoDoctorService;
import org.jeecg.modules.mo.service.IMoDoctorWorkService;
import org.jeecg.modules.mo.service.IMoPatientCardService;
import org.jeecg.modules.mo.vo.*;
import org.jeecg.modules.mq.service.ProducerMqService;
import org.jeecg.modules.system.service.ISysTenantService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 就诊预约表
 * @Author: jeecg-boot
 * @Date: 2023-11-29
 * @Version: V1.0
 */
@Slf4j
@Service
public class MoClinicAppointServiceImpl extends ServiceImpl<MoClinicAppointMapper, ClinicAppoint> implements IMoClinicAppointService {

    @Resource
    private ProducerMqService disruptorMqService;
    @Resource
    private IMoDoctorService moDoctorService;
    @Resource
    private IMoPatientCardService moPatientCardService;
    @Resource
    private MoClinicAppointMapper moClinicAppointMapper;
    @Resource
    private ISysTenantService iSysTenantService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MoClinicRoomMapper moClinicRoomMapper;
    @Resource
    private IMoDoctorWorkService moDoctorWorkService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public IPage<MoPatientClinicAppointPageVO> queryPatientAppointList(String openId, MoClinicAppointQueryDTO queryDTO, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<MoPatientClinicAppointPageVO> clinicAppointList = moClinicAppointMapper.queryPatientAppointList(openId, queryDTO);
        for (MoPatientClinicAppointPageVO appointListVO : clinicAppointList) {
            String appointDay = DateUtil.format(appointListVO.getAppointDay(), "yyyy-MM-dd");
            Date appointTime =DateUtil.parse( appointDay + " " + appointListVO.getAppointPeriod().substring(0, 5), "yyyy-MM-dd HH:mm") ;
            if (new Date().compareTo(appointTime) == 1) {
                appointListVO.setIsExpired(true);
            }else {
                appointListVO.setIsExpired(false);
            }
        }
        return oConvertUtils.pageInfoToIPage(new PageInfo<>(clinicAppointList));
    }

    @Override
    public List<MoDoctorWorkDetailListVO> queryAppointDetailList(String workDay, String doctorId) {
        List<MoDoctorWorkDetailListVO> workDetailListVOList = moDoctorWorkService.getDoctorWorkDetailList(workDay, doctorId);
        for (MoDoctorWorkDetailListVO detailListVO : workDetailListVOList) {
            List<MoClinicAppointDetailListVO> clinicAppointList = moClinicAppointMapper.getAppointDetailList(detailListVO.getId());
            for (MoClinicAppointDetailListVO appointDetailListVO : clinicAppointList) {
                String appointTimeStr = DateUtil.format(detailListVO.getWorkDay(), "yyyy-MM-dd") + " " + appointDetailListVO.getAppointPeriod().substring(0, 5);
                Date appointTime = DateUtil.parse(appointTimeStr, "yyyy-MM-dd HH:mm");
                if (new Date().compareTo(appointTime) == 1) {
                    //不可预约状态
                    appointDetailListVO.setAppointStatus(-1);
                }
            }
            detailListVO.setClinicAppointDetailListVOList(oConvertUtils.toList(clinicAppointList, MoClinicAppointDetailListVO.class));
            if (clinicAppointList.size() > 0) {
                //预约时段
                String clinicPeriod = clinicAppointList.get(0).getAppointPeriod().substring(0, 5) +
                        clinicAppointList.get(clinicAppointList.size() - 1).getAppointPeriod().substring(5, 11);
                detailListVO.setClinicPeriod(clinicPeriod);
            }
            detailListVO.setWorkPeriodDict(getPeriodString(detailListVO.getWorkPeriod()));
        }
        return workDetailListVOList;
    }

    @Override
    public MoClinicAppointDetailVO queryAppointDetailById(String id) {
        MoClinicAppointDetailVO detailVO = moClinicAppointMapper.queryAppointDetailById(id);
        if (detailVO == null) {
            throw new RuntimeException("未找到对应数据");
        }
        return detailVO;
    }

    @Override
    public MoQueueUiDetailVO queryQueueUiDetail(String openId, String tenantId) {

        MoQueueUiDetailVO queueUiDetailVO = new MoQueueUiDetailVO();
        String today = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<ClinicAppoint> clinicAppointList = this.list(new QueryWrapper<ClinicAppoint>().lambda()
                .eq(ClinicAppoint::getOpenId, openId)
                .eq(ClinicAppoint::getAppointDay, today)
                .eq(ClinicAppoint::getClinicStatus, 0)
                .orderByDesc(ClinicAppoint::getCreateTime));
        if (clinicAppointList.size() > 0) {
            queueUiDetailVO.setAppointId(clinicAppointList.get(0).getId());
            PatientCard patientCard = moPatientCardService.getById(clinicAppointList.get(0).getCardId());
            queueUiDetailVO.setCardId(patientCard.getId());
            //获取就诊人名称
            queueUiDetailVO.setAppointPatientName(patientCard != null ? patientCard.getRealname() : "");
            Doctor doctor = moDoctorService.getById(clinicAppointList.get(0).getDoctorId());
            queueUiDetailVO.setDoctorName(doctor != null ? doctor.getDoctorName() : "");
            queueUiDetailVO.setDoctorId(clinicAppointList.get(0).getDoctorId());
            queueUiDetailVO.setRegFee(doctor.getRegFee());
        } else {
            PatientCard patientCard = moPatientCardService.getOne(new QueryWrapper<PatientCard>().lambda()
                    .eq(PatientCard::getOpenId, openId)
                    .orderByDesc(PatientCard::getIsDefault, PatientCard::getCreateTime)
                    .last("limit 1"));
            if (patientCard != null) {
                queueUiDetailVO.setCardId(patientCard.getId());
                queueUiDetailVO.setPatientName(patientCard.getRealname());
            }
        }
        queueUiDetailVO.setHospitalName(iSysTenantService.getById(tenantId).getName());
        return queueUiDetailVO;
    }

    @Override
    public List<MoTodayQueueListVO> queryMoTodayQueueList(String openId) {
        String appointDay = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<MoTodayQueueListVO> moTodayQueueListVOList = moClinicAppointMapper.getTodayQueueList(openId, appointDay);
        for (MoTodayQueueListVO queueListVO : moTodayQueueListVOList) {
            if (queueListVO.getAppointStatus() == 0) {//未预约
                //未预约排队,现场扫码排队
                String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + queueListVO.getDoctorId() + "_" + appointDay;
                //预约排队
                String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + queueListVO.getDoctorId() + "_" + appointDay;

                Long appointCount = redisTemplate.opsForList().size(appointListKey);
                //未预约排队位置
                Long queueCount = redisTemplate.opsForList().indexOf(queueListKey, queueListVO.getCardId());
                if (queueCount == null) {
                    queueCount = 0L;
                }
                queueListVO.setNowQueueCount(appointCount + queueCount);
                queueListVO.setQueueNumber("B" + queueListVO.getQueueNumber());
            } else {
                //已预约
                //预约排队
                String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + queueListVO.getDoctorId() + "_" + appointDay;
                Long appointCount = redisTemplate.opsForList().indexOf(appointListKey, queueListVO.getCardId());
                queueListVO.setNowQueueCount(appointCount);
                queueListVO.setQueueNumber("A" + queueListVO.getQueueNumber());
                Map<String, String> clinicRoom = moClinicRoomMapper.getClinicRoomNameByAppoint(queueListVO.getId());
                queueListVO.setClinicRoomName(clinicRoom.get("clinicRoomName"));
            }
        }
        return moTodayQueueListVOList;
    }

    @Override
    public void patientAppointMq(MoClinicAppointAddDTO appointAddDTO) {
        disruptorMqService.patientAppointMq(appointAddDTO);
    }

    @Override
    public void patientAppointQueueMq(MoClinicAppointQueueAddDTO queueAddDTO) {
        String appointDay = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //排队锁
        //未预约排队,现场扫码排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + queueAddDTO.getDoctorId() + "_" + appointDay;
        //预约排队
        String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + queueAddDTO.getDoctorId() + "_" + appointDay;
        //排队队列是否存在
        Long queueCount = null;
        queueCount = redisTemplate.opsForList().indexOf(appointListKey, queueAddDTO.getCardId());//已预约排队
        if (queueCount != null){
            //已经预约
            log.warn("患者已经预约：{}", queueAddDTO.getCardId());
            throw new RuntimeException("已扫码排队，请勿重复扫码！");
        }
        if (StrUtil.isBlank(queueAddDTO.getAppointId())) {
            queueCount = redisTemplate.opsForList().indexOf(queueListKey, queueAddDTO.getCardId());//未预约排队
            if (queueCount != null) {
                //已经预约
                log.warn("患者已经预约：{}", queueAddDTO.getCardId());
                throw new RuntimeException("已扫码排队，请勿重复扫码！");
            }
        }
        disruptorMqService.patientAppointQueueMq(queueAddDTO);
    }

    @Override
    public void addPatientAppoint(MoClinicAppointAddDTO appointAddDTO) {
        ClinicAppoint clinicAppoint = new ClinicAppoint();
        //预约成功
        clinicAppoint.setAppointStatus(2);
        //排队序号
        clinicAppoint.setCardId(appointAddDTO.getCardId());
        clinicAppoint.setId(appointAddDTO.getAppointId());
        clinicAppoint.setOpenId(appointAddDTO.getOpenId());
        moClinicAppointMapper.updateById(clinicAppoint);
    }

    @Override
    public void updatePatientAppoint(ClinicAppoint clinicAppoint) {
        moClinicAppointMapper.updateById(clinicAppoint);
    }

    @Override
    public IPage<ClinicAppoint> queryActivityList(ClinicAppoint clinicAppoint, Integer pageNo, Integer pageSize, HttpServletRequest req) {

        return null;
    }

    private String getPeriodString(Integer period){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "全天");
        map.put(2, "上午");
        map.put(3, "下午");
        return map.get(period);
    }
}
