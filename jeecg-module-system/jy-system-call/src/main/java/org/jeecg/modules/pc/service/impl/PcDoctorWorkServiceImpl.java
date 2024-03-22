package org.jeecg.modules.pc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisTenantContext;
import org.jeecg.modules.common.entity.*;
import org.jeecg.modules.common.mapper.DoctorWorkMapper;
import org.jeecg.modules.common.service.*;
import org.jeecg.modules.pc.dto.*;
import org.jeecg.modules.pc.mapper.PcDoctorWorkMapper;
import org.jeecg.modules.pc.service.IPcDoctorWorkService;
import org.jeecg.modules.pc.vo.DoctorWorkDetailQueryVO;
import org.jeecg.modules.pc.vo.DoctorWorkPageVO;
import org.jeecg.modules.pc.vo.DoctorWorkQueryVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Service
@Slf4j
public class PcDoctorWorkServiceImpl extends ServiceImpl<DoctorWorkMapper, DoctorWork> implements IPcDoctorWorkService {
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PcDoctorWorkMapper pcDoctorWorkMapper;
    @Resource
    private IDoctorWorkDetailService doctorWorkDetailService;
    @Resource
    private IDoctorWorkDetailService workDetailService;
    @Resource
    private IDoctorWorkService doctorWorkService;
    @Autowired
    private IClinicAppointService clinicAppointService;
    @Resource
    private IOfficeService officeService;
    @Resource
    private IClinicRoomService clinicRoomService;
    @Resource
    private IDoctorService doctorService;
    @Resource
    private ISysUserService sysUserService;

    @Override
    public IPage<DoctorWorkPageVO> queryDoctorWorkPage(DoctorWorkPageDTO workPageDTO, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<DoctorWorkPageVO> pageVOList = pcDoctorWorkMapper.getDoctorWorkList(workPageDTO);
        for (DoctorWorkPageVO workPageVO : pageVOList) {
            Date nowDate = DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"));
            if (!(workPageVO.getClinicStatus().equals(2) && workPageVO.getClinicStatus().equals(4))) {//不等于就诊完成和过期
                List<DoctorWorkDetail> doctorWorkDetailList = doctorWorkDetailService.list(new LambdaQueryWrapper<DoctorWorkDetail>()
                        .eq(DoctorWorkDetail::getDoctorWordId, workPageVO.getId())
                        .orderByAsc(DoctorWorkDetail::getWorkPeriod));
                for (DoctorWorkDetail doctorWorkDetail : doctorWorkDetailList) {
                    if (new Date().compareTo(doctorWorkDetail.getBeginTimeClinic()) == 1) {//开始就诊时间小于当前时间，属于就诊中
                        workPageVO.setClinicStatus(1);//就诊中
                    }
                    if (new Date().compareTo(doctorWorkDetail.getEndTimeClinic()) == 1) {
                        updateWork(workPageVO.getId(), 4);
                        workPageVO.setClinicStatus(4);//过期
                    }
                }
            }
            if (workPageVO.getAppointTime().getTime() < new Date().getTime()) {
                workPageVO.setAppointStatus(1);//预约中
            } if(nowDate.compareTo(workPageVO.getWorkDay()) == 1 && (workPageVO.getAppointStatus().equals(0)|| workPageVO.getAppointStatus().equals(1))) {
                workPageVO.setAppointStatus(4);//过期
            }
        }
        return oConvertUtils.pageInfoToIPage(new PageInfo<>(pageVOList));
    }

    private void updateWork(String workId, Integer clinicStatus){
        DoctorWork doctorWork = new DoctorWork();
        doctorWork.setId(workId);
        doctorWork.setClinicStatus(clinicStatus);
        this.updateById(doctorWork);
    }

    @Override
    public DoctorWorkQueryVO queryDoctorWorkById(DoctorWorkQueryVO doctorWorkQueryVO) {
        if ((doctorWorkQueryVO.getAppointTime().getTime() < new Date().getTime()) && doctorWorkQueryVO.getAppointStatus() == 0) {
            doctorWorkQueryVO.setAppointStatus(1);//预约中
        }
        Doctor doctor = doctorService.getById(doctorWorkQueryVO.getDoctorId());
        if (StrUtil.isBlank(doctorWorkQueryVO.getOfficeId())) {
            doctorWorkQueryVO.setOfficeId(doctor.getOfficeId());
        }
        SysUser sysUser = sysUserService.getById(doctor.getUserId());
        doctorWorkQueryVO.setRealname(sysUser.getRealname());
        Office office = officeService.getById(doctorWorkQueryVO.getOfficeId());
        if (office != null) {
            doctorWorkQueryVO.setOfficeName(office.getOfficeName());
        }
        ClinicRoom clinicRoom = clinicRoomService.getById(doctorWorkQueryVO.getClinicRoomId());
        if (clinicRoom != null) {
            doctorWorkQueryVO.setClinicRoomName(clinicRoom.getClinicRoomName());
        }
        List<DoctorWorkDetail> doctorWorkDetailList = workDetailService.list(new QueryWrapper<DoctorWorkDetail>()
                .lambda().eq(DoctorWorkDetail::getDoctorWordId, doctorWorkQueryVO.getId()));

        doctorWorkQueryVO.setWorkDetailAddDTOList(oConvertUtils.entityListToModelList(doctorWorkDetailList, DoctorWorkDetailQueryVO.class));
        return doctorWorkQueryVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDoctorWork(DoctorWorkAddDTO workAddDTO) {
        String workDay = DateUtils.formatDate(workAddDTO.getWorkDay(), "yyyy-MM-dd");
        String dayKey = CommonConstant.PREFIX_DOCTOR_WORK_NUM + workAddDTO.getDoctorId() + "_" + workDay;
        //判断可以是否存在
        if (redisUtil.hasKey(dayKey)) {
            log.warn(DateUtils.formatDate(workAddDTO.getWorkDay(), "yyyy-MM-dd") + ",排班信息已存在：{}", dayKey);
            throw new RuntimeException(DateUtils.formatDate(workAddDTO.getWorkDay(), "yyyy-MM-dd") + ",当日排班信息已存在，请勿重复提交！");
        }
        if (workAddDTO.getAppointType().equals(1)) {
            doAppointPeriod(workAddDTO, dayKey);
        } else if (workAddDTO.getAppointType().equals(2)) {
            //预约时段排队
            doAppointQueue(workAddDTO, workDay, dayKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDoctorWork(DoctorWorkUpdateDTO updateDTO) {
        //删除redis缓存数据
        String workDay = DateUtils.formatDate(updateDTO.getWorkDay(), "yyyy-MM-dd");
        String dayKey = CommonConstant.PREFIX_DOCTOR_WORK_NUM + updateDTO.getDoctorId() + "_" + workDay;
        //转换成新增数据
        DoctorWorkAddDTO doctorWorkUpdate = BeanUtil.copyProperties(updateDTO, DoctorWorkAddDTO.class);

        List<DoctorWorkDetailAddDTO> doctorWorkDetailList = new ArrayList<>();
        //删除排班详情数据
        workDetailService.remove(new UpdateWrapper<DoctorWorkDetail>().lambda().eq(DoctorWorkDetail::getDoctorWordId, doctorWorkUpdate.getId()));
        for (DoctorWorkDetailUpdateDTO detailUpdateDTO : updateDTO.getWorkDetailUpdateDTOList()) {
            DoctorWorkDetailAddDTO detailAddDTO = BeanUtil.copyProperties(detailUpdateDTO, DoctorWorkDetailAddDTO.class);
            detailAddDTO.setDoctorWordId(updateDTO.getId());
            doctorWorkDetailList.add(detailAddDTO);
            //删除排班预约时段详情
            clinicAppointService.remove(new UpdateWrapper<ClinicAppoint>().lambda().eq(ClinicAppoint::getDoctorWorkDetailId, detailUpdateDTO.getId()));
            String detailKey = CommonConstant.PREFIX_DOCTOR_WORK_PERIOD_NUM + updateDTO.getDoctorId() + "_" +
                    detailUpdateDTO.getWorkPeriod() + "_" + workDay;
            //删除详情可预约总数
            redisUtil.removeAll(detailKey);
        }
        //删除当天可预约总数
        redisUtil.removeAll(dayKey);
        doctorWorkUpdate.setWorkDetailAddDTOList(doctorWorkDetailList);

        if (updateDTO.getAppointType().equals(1)) {
            doAppointPeriod(doctorWorkUpdate, dayKey);
        } else if (updateDTO.getAppointType().equals(2)) {
            //预约时段排队
            doAppointQueue(doctorWorkUpdate, workDay, dayKey);
        }
    }

    @Override
    public void editDoctorWorkStatus(String workDetailId, long finishCount) {
        DoctorWork doctorWork = new DoctorWork();
        DoctorWorkDetail doctorWorkDetail = workDetailService.getById(workDetailId);
        doctorWork.setId(doctorWorkDetail.getDoctorWordId());
        if (finishCount == 0) {
            doctorWork.setClinicStatus(1);
            MybatisTenantContext.set(true);//不需要租户拦截
            doctorWorkService.updateById(doctorWork);
            MybatisTenantContext.clear();
        } else if (doctorWorkDetail.getWorkPeriod() == 1 && finishCount == doctorWorkDetail.getNumTotal()) {
            doctorWork.setClinicStatus(2);
            MybatisTenantContext.set(true);//不需要租户拦截
            doctorWorkService.updateById(doctorWork);
            MybatisTenantContext.clear();
        } else if (doctorWorkDetail.getWorkPeriod() != 1) {
            Integer workPeriod = doctorWorkDetail.getWorkPeriod() == 2 ? 3 : 2;
            DoctorWorkDetail doctorWorkDetail1 = workDetailService.getOne(new LambdaQueryWrapper<DoctorWorkDetail>()
                    .eq(DoctorWorkDetail::getDoctorWordId, doctorWorkDetail.getDoctorWordId())
                    .eq(DoctorWorkDetail::getWorkPeriod, workPeriod));
            Integer clinicCount = doctorWorkDetail.getNumTotal() + doctorWorkDetail1.getNumTotal();
            if (clinicCount == finishCount) {
                doctorWork.setClinicStatus(2);
                MybatisTenantContext.set(true);//不需要租户拦截
                doctorWorkService.updateById(doctorWork);
                MybatisTenantContext.clear();
            }
        }
    }

    @Override
    public void deleteDoctorWork(DoctorWork doctorWork) {
        doctorWorkService.removeById(doctorWork.getId());
        //删除redis缓存数据
        String workDay = DateUtils.formatDate(doctorWork.getWorkDay(), "yyyy-MM-dd");
        String dayKey = CommonConstant.PREFIX_DOCTOR_WORK_NUM + doctorWork.getDoctorId() + "_" + workDay;
        //删除当天可预约总数
        redisUtil.removeAll(dayKey);

        List<DoctorWorkDetail> doctorWorkDetailList = workDetailService.list(new QueryWrapper<DoctorWorkDetail>()
                .lambda().eq(DoctorWorkDetail::getDoctorWordId, doctorWork.getId()));
        for (DoctorWorkDetail workDetail : doctorWorkDetailList) {
            String detailKey = CommonConstant.PREFIX_DOCTOR_WORK_PERIOD_NUM + doctorWork.getDoctorId() + "_" +
                    workDetail.getWorkPeriod() + "_" + workDay;
            //删除详情可预约总数
            redisUtil.removeAll(detailKey);
            //删除排班预约时段详情
            clinicAppointService.remove(new UpdateWrapper<ClinicAppoint>().lambda().eq(ClinicAppoint::getDoctorWorkDetailId, workDetail.getId()));
        }
        //删除排班详情
        workDetailService.remove(new QueryWrapper<DoctorWorkDetail>().lambda().eq(DoctorWorkDetail::getDoctorWordId, doctorWork.getId()));
    }

    /*
     *@Description: 处理时段预约
     *@Param: []
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 10:25 2023/12/6
     **/
    private void doAppointPeriod(DoctorWorkAddDTO workAddDTO, String dayKey) {
        DoctorWork doctorWork = BeanUtil.copyProperties(workAddDTO, DoctorWork.class);
        doctorWork.setId(String.valueOf(IdWorker.getId()));
        for (DoctorWorkDetailAddDTO detailAddDTO : workAddDTO.getWorkDetailAddDTOList()) {
            DoctorWorkDetail doctorWorkDetail = BeanUtil.copyProperties(detailAddDTO, DoctorWorkDetail.class);
            doctorWorkDetail.setId(String.valueOf(IdWorker.getId()));
            doctorWorkDetail.setDoctorWordId(doctorWork.getId());

            List<ClinicAppoint> clinicAppointList = new ArrayList<>();
            //1、就诊总时间分钟
            Long clinicSumTime = DateUtil.between(detailAddDTO.getEndTimeClinic(), detailAddDTO.getBeginTimeClinic(), DateUnit.HOUR) * 60;

            //2、每就诊一个人需要的时间
            Integer clinicMinute = (int)Math.ceil((double) clinicSumTime / (double) detailAddDTO.getNumTotal());
            for (int i = 1; i <= detailAddDTO.getNumTotal(); i++) {
                ClinicAppoint clinicAppoint = new ClinicAppoint();
                clinicAppoint.setDoctorWorkDetailId(doctorWorkDetail.getId());
                clinicAppoint.setDoctorId(workAddDTO.getDoctorId());
                clinicAppoint.setAppointDay(workAddDTO.getWorkDay());
                clinicAppoint.setAppointNumber(i);
                Date beginPeriod = DateUtil.offsetMinute(detailAddDTO.getBeginTimeClinic(), (i - 1) * clinicMinute);
                Date endPeriod = DateUtil.offsetMinute(detailAddDTO.getBeginTimeClinic(), (i) * clinicMinute);
                clinicAppoint.setAppointPeriod(DateUtils.formatDate(beginPeriod, "HH:mm") + "-" + DateUtils.formatDate(endPeriod, "HH:mm"));
                clinicAppointList.add(clinicAppoint);
                if (detailAddDTO.getEndTimeClinic().getTime() < endPeriod.getTime()) {
                    //预约时间大于当前出诊时间，将推出
                    break;
                }
            }
            clinicAppointService.saveBatch(clinicAppointList);
            detailAddDTO.setNumTotal(clinicAppointList.size());
            //保存医生值班详情
            doctorWorkDetailService.save(doctorWorkDetail);
        }
        doctorWork.setNumTotal(workAddDTO.getWorkDetailAddDTOList().stream().collect(Collectors.summingInt(DoctorWorkDetailAddDTO::getNumTotal)));
        this.saveOrUpdate(doctorWork);//保存排班数据
        //保存号源总数
        redisUtil.set(dayKey, doctorWork.getNumTotal());
    }

    /*
     *@Description: 处理预约排队情况
     *@Param: []
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 9:36 2023/12/6
     **/
    private void doAppointQueue(DoctorWorkAddDTO workAddDTO, String workDay, String dayKey) {
        SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
        String doctorWorkId = String.valueOf(snowflakeGenerator.next());
        Integer numTotal = 0;
        for (DoctorWorkDetailAddDTO detailAddDTO : workAddDTO.getWorkDetailAddDTOList()) {
            DoctorWorkDetail doctorWorkDetail = BeanUtil.copyProperties(detailAddDTO, DoctorWorkDetail.class);
            doctorWorkDetail.setDoctorWordId(doctorWorkId);
            numTotal += doctorWorkDetail.getNumTotal();//获取总号源
            doctorWorkDetailService.save(doctorWorkDetail);
            String detailKey = CommonConstant.PREFIX_DOCTOR_WORK_PERIOD_NUM + workAddDTO.getDoctorId() + "_" +
                    doctorWorkDetail.getWorkPeriod() + "_" + workDay;
            //保存号源总数
            redisUtil.set(detailKey, doctorWorkDetail.getNumTotal());
        }
        DoctorWork doctorWork = BeanUtil.copyProperties(workAddDTO, DoctorWork.class);
        doctorWork.setNumTotal(numTotal);
        this.saveOrUpdate(doctorWork);//保存排班信息
        //保存号源总数
        redisUtil.set(dayKey, doctorWork.getNumTotal());
    }
}
