package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.vo.MoClinicAppointDetailListVO;
import org.jeecg.modules.mo.vo.MoClinicAppointDetailVO;
import org.jeecg.modules.mo.vo.MoPatientClinicAppointPageVO;
import org.jeecg.modules.mo.vo.MoTodayQueueListVO;

import java.util.List;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-05
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoClinicAppointMapper extends BaseMapper<ClinicAppoint> {
    /*
     *@Description: 查询患者预约记录
     *@Param: [openId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoPatientClinicAppointListVO>
     *@author: xiaopeng.wu
     *@DateTime: 9:54 2024/1/10
    **/
    List<MoPatientClinicAppointPageVO> queryPatientAppointList(@Param("openId") String openId, @Param("queryDTO") MoClinicAppointQueryDTO queryDTO);
    /*
     *@Description: 获取预约详情列表
     *@Param: [doctorWorkDetailId]
     *@Return: java.util.List<MoClinicAppointDetailListVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:23 2024/1/9
    **/
    List<MoClinicAppointDetailListVO> getAppointDetailList(@Param("doctorWorkDetailId") String doctorWorkDetailId);
    /*
     *@Description: 查询用户就诊详情
     *@Param: [appointId]
     *@Return: org.jeecg.modules.pc.vo.ClinicAppointDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 11:41 2023/12/7
    **/
    @InterceptorIgnore(tenantLine = "true")
    MoClinicAppointDetailVO queryAppointDetailById(@Param("id") String id);
    
    /*
     *@Description:
     *@Param: [openId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoTodayQueueListVO>
     *@author: xiaopeng.wu
     *@DateTime: 13:57 2023/12/29
    **/
    @InterceptorIgnore(tenantLine = "true")
    List<MoTodayQueueListVO> getTodayQueueList(@Param("openId") String openId,@Param("appointDay") String appointDay);
}
