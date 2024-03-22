package org.jeecg.modules.mo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.mo.dto.MoClinicAppointAddDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueueAddDTO;
import org.jeecg.modules.mo.vo.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 就诊预约表
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
public interface IMoClinicAppointService extends IService<ClinicAppoint> {
    /*
     *@Description: 查询用户预约历史记录
     *@Param: [openId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoPatientClinicAppointListVO>
     *@author: xiaopeng.wu
     *@DateTime: 9:59 2024/1/10
    **/
    IPage<MoPatientClinicAppointPageVO> queryPatientAppointList(String openId, MoClinicAppointQueryDTO queryDTO, Integer pageNo, Integer pageSize);
    /*
     *@Description: 预约详情列表
     *@Param: [doctorId]
     *@Return: org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO
     *@author: xiaopeng.wu
     *@DateTime: 10:33 2024/1/8
    **/
    List<MoDoctorWorkDetailListVO> queryAppointDetailList(String workDay, String doctorId);
    /*
     *@Description: 患者查询预约详情
     *@Param: [id]
     *@Return: org.jeecg.modules.mo.vo.ClinicAppointDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 14:33 2023/12/14
    **/
    MoClinicAppointDetailVO queryAppointDetailById(String id);

    /*
     *@Description: 获取排队就诊页面数据
     *@Param: [openId]
     *@Return: org.jeecg.modules.mo.vo.MoQueueUiDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 9:57 2023/12/25
    **/
    MoQueueUiDetailVO queryQueueUiDetail(String openId, String tenantId);

    /*
     *@Description: 获取当天排队数据
     *@Param: [openId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoTodayQueueListVO>
     *@author: xiaopeng.wu
     *@DateTime: 18:01 2023/12/28
    **/
    List<MoTodayQueueListVO> queryMoTodayQueueList(String openId);

    /*
     *@Description: 用户预约-异步
     *@Param: [clinicAppoint]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 15:52 2023/11/29
    **/
    void patientAppointMq(MoClinicAppointAddDTO appointAddDTO);

    /*
     *@Description: 用户预约排队
     *@Param: [appointQueueAddDTO]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 9:23 2023/12/5
    **/
    void patientAppointQueueMq(MoClinicAppointQueueAddDTO appointQueueAddDTO);

    /*
     *@Description: 保存用户预约消息
     *@Param: [clinicAppoint]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 15:52 2023/11/29
    **/
    void addPatientAppoint(MoClinicAppointAddDTO appointAddDTO);

    /*
     *@Description: 修改用户预约消息
     *@Param: [clinicAppoint]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 15:52 2023/11/29
    **/
    void updatePatientAppoint(ClinicAppoint clinicAppoint);

    /*
     *@Description: 获取即将预约活动
     *@Param: [clinicAppoint, pageNo, pageSize, req]
     *@Return: com.baomidou.mybatisplus.core.metadata.IPage<org.jeecg.modules.common.entity.ClinicAppoint>
     *@author: xiaopeng.wu
     *@DateTime: 9:14 2023/12/1
    **/
    IPage<ClinicAppoint> queryActivityList(ClinicAppoint clinicAppoint,
                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                            HttpServletRequest req);

}
