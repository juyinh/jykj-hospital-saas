package org.jeecg.modules.pc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.common.mapper.ClinicAppointMapper;
import org.jeecg.modules.common.service.IDoctorService;
import org.jeecg.modules.common.service.IPatientCaseService;
import org.jeecg.modules.pc.dto.ClinicAppointHistoryPageDTO;
import org.jeecg.modules.pc.dto.ClinicAppointTodayPageDTO;
import org.jeecg.modules.pc.dto.PatientClinicEditDTO;
import org.jeecg.modules.pc.mapper.PcClinicAppointMapper;
import org.jeecg.modules.pc.service.IPcClinicAppointService;
import org.jeecg.modules.pc.service.IPcDoctorWorkService;
import org.jeecg.modules.pc.vo.ClinicAppointDetailVO;
import org.jeecg.modules.pc.vo.ClinicAppointHistoryPageVO;
import org.jeecg.modules.pc.vo.ClinicAppointPageVO;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.service.ISysTenantService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class PcClinicAppointServiceImpl extends ServiceImpl<ClinicAppointMapper, ClinicAppoint> implements IPcClinicAppointService {
    @Resource
    private ClinicAppointMapper clinicAppointMapper;
    @Resource
    private PcClinicAppointMapper pcClinicAppointMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private IDoctorService doctorService;
    @Resource
    private IPatientCaseService patientCaseService;
    @Resource
    private IPcDoctorWorkService pcDoctorWorkService;
    @Resource
    private ISysTenantService sysTenantService;

    @Override
    public Result<IPage<ClinicAppointPageVO>> queryTodayPageList(ClinicAppointTodayPageDTO todayPage, Integer pageNo, Integer pageSize) throws ParseException {
        QueryWrapper<ClinicAppoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("appoint_day", DateUtils.parseDate(DateUtils.formatDate(), "yyyy-MM-dd"));
        queryWrapper.eq(todayPage.getAppointStatus() != null,"appoint_status", todayPage.getAppointStatus());
        queryWrapper.eq(todayPage.getClinicStatus()!= null, "clinic_status", todayPage.getClinicStatus());
        queryWrapper.isNotNull("card_id");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Doctor doctor = doctorService.getOne(new QueryWrapper<Doctor>().lambda().eq(Doctor::getUserId, sysUser.getId()));
        if (doctor == null) {
            return Result.OK(new Page());
        }
        //普通成员只能查询个人出诊预约信息
        queryWrapper.eq("doctor_id", doctor.getId());
        queryWrapper.orderByAsc("queue_number","appoint_number");
        Page<ClinicAppoint> page = new Page<ClinicAppoint>(pageNo, pageSize);
        IPage<ClinicAppoint> pageList = clinicAppointMapper.selectPage(page, queryWrapper);
        List<ClinicAppointPageVO> appointPageVOList = new ArrayList<>();
        IPage<ClinicAppointPageVO> pageResult = new Page(pageList.getCurrent(),pageList.getSize(), pageList.getTotal());
        for (ClinicAppoint pageVO : pageList.getRecords()) {
            ClinicAppointPageVO clinicAppointPageVO = BeanUtil.copyProperties(pageVO, ClinicAppointPageVO.class);
            if (pageVO.getAppointStatus().equals(2)) {
                clinicAppointPageVO.setQueueNumber("A" + pageVO.getQueueNumber());
            }else if (pageVO.getAppointStatus().equals(0)){
                clinicAppointPageVO.setQueueNumber("B" + pageVO.getQueueNumber());
            }
            appointPageVOList.add(clinicAppointPageVO);
        }
        //排序
        pageResult.setRecords(sortAppointPageVOList(appointPageVOList, doctor.getId()));
        return Result.OK(pageResult);
    }

    @Override
    public IPage<ClinicAppointHistoryPageVO> queryAppointHistoryPage(ClinicAppointHistoryPageDTO pageDTO, Integer pageNo, Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String doctorId = "";
        if (sysUser.getUserIdentity().equals(1)) {
            Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>().eq(Doctor::getUserId, sysUser.getId()));
            doctorId = doctor.getId();
        }
        PageHelper.startPage(pageNo, pageSize);
        List<ClinicAppointHistoryPageVO> pageVOS = pcClinicAppointMapper.getAppointHistoryList(pageDTO, doctorId);
        return oConvertUtils.pageInfoToIPage(new PageInfo<>(pageVOS));
    }

    private List<ClinicAppointPageVO> sortAppointPageVOList(List<ClinicAppointPageVO> appointPageVOList, String doctorId){
        String todayDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //预约排队
        String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + doctorId  + "_" + todayDate;
        //未预约排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + doctorId  + "_" + todayDate;
        List<Object> appointList = redisTemplate.opsForList().range(appointListKey, 0, -1);
        List<Object> queueList = redisTemplate.opsForList().range(queueListKey, 0, -1);
        appointList.addAll(queueList);
        Collections.sort(appointPageVOList, Comparator.comparingInt(data->{
            int appointIndex = appointList.indexOf(data.getId());
            return appointIndex != -1 ? appointIndex : Integer.MAX_VALUE; // 将没有匹配的appointId放到末尾
        }));

        return appointPageVOList;
    }

    @Override
    public ClinicAppointDetailVO queryPatientDetail(String appointId){
        ClinicAppointDetailVO appointDetailVO = pcClinicAppointMapper.getPatientClinicDetail(appointId);
        if (appointDetailVO.getAppointStatus().equals(2)) {
            //已预约
            appointDetailVO.setQueueNumber("A" + appointDetailVO.getQueueNumber());
        }else if(appointDetailVO.getAppointStatus().equals(0)){
            //未预约
            appointDetailVO.setQueueNumber("B" + appointDetailVO.getQueueNumber());
        }
        //获取医院名称和医生名称
        Doctor doctor = doctorService.getById(appointDetailVO.getDoctorId());
        if (doctor != null) {
            appointDetailVO.setDoctorName(doctor.getDoctorName());
            SysTenant sysTenant = sysTenantService.getById(doctor.getTenantId());
            appointDetailVO.setHospitalName(sysTenant != null ? sysTenant.getName() : "");
        }
        return appointDetailVO;
    }

    @Override
    public ClinicAppointDetailVO queryPatientClinicDetail(String appointId) throws ParseException {
        ClinicAppoint clinicAppoint = this.getById(appointId);
        if (clinicAppoint == null) {
            throw new RuntimeException("患者信息不存在！");
        }
        String todayDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //未预约排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + todayDate;
        //预约排队
        String appointQueueListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + todayDate;

        //当前等待人数
        Integer waitCount = 0;
        doBeginClinic(appointId);//开始就诊
        Long appointQueueCount = redisUtil.lGetListSize(appointQueueListKey);
        Long queueCount = redisUtil.lGetListSize(queueListKey);
        //累加当前排队人数
        waitCount = oConvertUtils.getInt(appointQueueCount) + oConvertUtils.getInt(queueCount);
        ClinicAppointDetailVO appointDetailVO = pcClinicAppointMapper.getPatientClinicDetail(appointId);
        if (appointDetailVO.getAppointStatus().equals(2)) {
            //已预约
            appointDetailVO.setQueueNumber("A" + appointDetailVO.getQueueNumber());
        }else if(appointDetailVO.getAppointStatus().equals(0)){
            //未预约
            appointDetailVO.setQueueNumber("B" + appointDetailVO.getQueueNumber());
        }
        //就诊完成
        Long appointTotal = clinicAppointMapper.selectCount(new QueryWrapper<ClinicAppoint>().lambda()
                .eq(ClinicAppoint::getAppointDay,todayDate)
                .eq(ClinicAppoint::getDoctorId, appointDetailVO.getDoctorId())
                .eq(ClinicAppoint::getClinicStatus, 3));
        if (appointTotal == 0) {
            pcDoctorWorkService.editDoctorWorkStatus(clinicAppoint.getDoctorWorkDetailId(), appointTotal);
        }
        appointDetailVO.setFinishCount(oConvertUtils.getInt(appointTotal));
        appointDetailVO.setWaitCount(waitCount);
        //获取医院名称和医生名称
        Doctor doctor = doctorService.getById(clinicAppoint.getDoctorId());
        if (doctor != null) {
            appointDetailVO.setDoctorName(doctor.getDoctorName());
            SysTenant sysTenant = sysTenantService.getById(doctor.getTenantId());
            appointDetailVO.setHospitalName(sysTenant != null ? sysTenant.getName() : "");
        }
        return appointDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String editPatientClinicDetail(PatientClinicEditDTO clinicEditDTO) {
        ClinicAppoint clinicAppoint = this.getById(clinicEditDTO.getAppointId());
        if (clinicAppoint == null) {
            throw new RuntimeException("患者信息不存在！");
        }
        //下一位预约id
        String nextAppointId = null;
        String nextCardIdId = null;
        String todayDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        //未预约排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + todayDate;
        //预约排队
        String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + todayDate;
        //当前用户完成就诊
        doFinishClinic(clinicEditDTO, clinicAppoint);

        if (!clinicAppoint.getAppointStatus().equals(0)) {
            //预约队列，从左侧出队
            redisTemplate.opsForList().leftPop(appointListKey);
        }else {
            //未预约队列
            redisTemplate.opsForList().leftPop(queueListKey);
        }
        //优先获取预约排队队列
        Long appointCount = redisUtil.lGetListSize(appointListKey);
        Long queueCount = redisUtil.lGetListSize(queueListKey);
        //获取下一位队列数据
        if (appointCount > 0) {
            List<Object> appointListNext = redisTemplate.opsForList().range(appointListKey, 0 ,0);
            if (appointListNext.size() > 0) {
                nextCardIdId  = appointListNext.get(0).toString();
            }
        }else {
            if (queueCount > 0) {
                List<Object> queueListNext = redisTemplate.opsForList().range(queueListKey, 0 , 0);
                if (queueListNext.size() > 0) {
                    nextCardIdId  = queueListNext.get(0).toString();
                }
            }
        }
        if (nextCardIdId != null) {
            ClinicAppoint nextClinicAppoint = this.getOne(new LambdaQueryWrapper<ClinicAppoint>()
                    .eq(ClinicAppoint::getCardId, nextCardIdId)
                    .eq(ClinicAppoint::getDoctorId, clinicAppoint.getDoctorId())
                    .eq(ClinicAppoint::getAppointDay, clinicAppoint.getAppointDay()));
            nextAppointId = nextClinicAppoint != null ? nextClinicAppoint.getId() : null;
        }
        return nextAppointId;
    }

    @Override
    public void againAppoint(String appointId) {
        ClinicAppoint clinicAppoint = clinicAppointMapper.selectById(appointId);
        if (clinicAppoint == null) {
            throw new RuntimeException("排队信息不存在!");
        }
        String appointDay = DateUtils.formatDate(clinicAppoint.getAppointDay(),"yyyy-MM-dd");
        //未预约排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + appointDay;
        //预约排队
        String appointQueueListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + appointDay;
        //未预约,现场预约
        if (clinicAppoint.getAppointStatus().equals(0)) {
            Long queueListCount = redisUtil.lGetListSize(queueListKey);
            // 当前排队人数大于3需要,插队
            if (queueListCount > 3) {
                //获取插队位置的值
                String toAppointId = redisTemplate.opsForList().index(queueListKey, 2) + "";
                //需要插队,放在当前排队人数第3位
                redisTemplate.opsForList().rightPush(queueListKey,toAppointId, appointId);
            }else{
                //当前人数排队小于3直接放在末尾排队即可
                redisUtil.lSet(queueListKey, appointId);
            }
        } else if (clinicAppoint.getAppointStatus().equals(2)) {
            Long appointQueueListCount = redisUtil.lGetListSize(appointQueueListKey);
            if (appointQueueListCount > 3) {
                //获取插队位置的值
                String toClinicAppointId = redisTemplate.opsForList().index(appointQueueListKey, 2) + "";
                //需要插队,放在当前排队人数第3位
                redisTemplate.opsForList().rightPush(appointQueueListKey,toClinicAppointId, appointId);
            }else {
                redisUtil.lSet(appointQueueListKey, appointId);
            }
        }

        ClinicAppoint clinicAppointUpdate = new ClinicAppoint();
        clinicAppointUpdate.setId(appointId);
        clinicAppointUpdate.setClinicStatus(1);//排队中
        clinicAppointMapper.updateById(clinicAppointUpdate);
    }

    @Override
    public String cancelAppoint(String appointId) {
        String appointDay = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        ClinicAppoint clinicAppoint = clinicAppointMapper.selectById(appointId);
        if (clinicAppoint == null) {
            log.warn("预约患者：{}", appointId);
            throw new RuntimeException("预约患者不存在！");
        }
        //预约排队
        String appointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + appointDay;
        //未预约排队
        String queueListKey = CommonConstant.PREFIX_PATIENT_QUEUE_LIST + clinicAppoint.getDoctorId()  + "_" + appointDay;
        if (clinicAppoint.getAppointStatus().equals(2)) {
            //未预约出队
            //redisTemplate.opsForList().leftPop(appointQueueListKey);
            //删除指定的值
            redisTemplate.opsForList().remove(appointListKey, 1, appointId);
        }else if (clinicAppoint.getAppointStatus().equals(0)){
            //未预约出队
            //redisTemplate.opsForList().leftPop(queueListKey);
            //删除指定的值
            redisTemplate.opsForList().remove(queueListKey, 1, appointId);
        }else {
            throw new RuntimeException("爽约失败，无此患者信息！");
        }
        ClinicAppoint clinicAppointUpdate = new ClinicAppoint();
        clinicAppointUpdate.setId(appointId);
        //就诊完成
        clinicAppointUpdate.setClinicStatus(4);
        clinicAppointUpdate.setClinicEndTime(new Date());
        clinicAppointMapper.updateById(clinicAppointUpdate);

        String nextAppointId = null;
        //获取下一位就诊人
        //优先获取预约排队队列
        Long appointCount = redisUtil.lGetListSize(appointListKey);
        Long queueCount = redisUtil.lGetListSize(queueListKey);
        //获取下一位队列数据
        if (appointCount > 0) {
            List<Object> appointListNext = redisTemplate.opsForList().range(appointListKey, 0 ,0);
            if (appointListNext.size() > 0) {
                nextAppointId  = appointListNext.get(0).toString();
            }
        }else {
            if (queueCount > 0) {
                List<Object> queueListNext = redisTemplate.opsForList().range(queueListKey, 0 , 0);
                if (queueListNext.size() > 0) {
                    nextAppointId  = queueListNext.get(0).toString();
                }
            }
        }
        return nextAppointId;
    }

    /*
     *@Description: 开始就诊状态更新
     *@Param: [appointId]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 15:40 2023/12/7
    **/
    private void doBeginClinic(String appointId){
        ClinicAppoint clinicAppoint = new ClinicAppoint();
        clinicAppoint.setId(appointId);
        clinicAppoint.setClinicStatus(2);
        clinicAppoint.setClinicBeginTime(new Date());
        clinicAppointMapper.updateById(clinicAppoint);
    }

    /*
     *@Description: 就诊完成状态
     *@Param: [appointId]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 15:41 2023/12/7
    **/
    private void doFinishClinic(PatientClinicEditDTO clinicEditDTO, ClinicAppoint appoint){
        ClinicAppoint clinicAppoint = new ClinicAppoint();
        clinicAppoint.setId(clinicEditDTO.getAppointId());
        //就诊完成
        clinicAppoint.setClinicStatus(3);
        clinicAppoint.setClinicEndTime(new Date());
        clinicAppointMapper.updateById(clinicAppoint);


        PatientCase patientCase = new PatientCase();
        patientCase.setCardId(appoint.getCardId());
        patientCase.setAppointId(clinicEditDTO.getAppointId());
        patientCase.setDoctorId(appoint.getDoctorId());
        patientCase.setCaseDetail(clinicEditDTO.getCaseDetail());
        patientCase.setDiagnosisResult(clinicEditDTO.getDiagnosisResult());
        patientCase.setDiagnosisFee(clinicEditDTO.getDiagnosisFee());
        //保存病例信息
        patientCaseService.saveOrUpdate(patientCase);
    }
}
