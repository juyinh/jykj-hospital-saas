package org.jeecg.modules.pc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.pc.dto.ClinicAppointHistoryPageDTO;
import org.jeecg.modules.pc.dto.ClinicAppointTodayPageDTO;
import org.jeecg.modules.pc.dto.PatientClinicEditDTO;
import org.jeecg.modules.pc.vo.ClinicAppointDetailVO;
import org.jeecg.modules.pc.vo.ClinicAppointHistoryPageVO;
import org.jeecg.modules.pc.vo.ClinicAppointPageVO;

import java.text.ParseException;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface IPcClinicAppointService extends IService<ClinicAppoint> {
    /*
     *@Description: 用户当日就诊列表
     *@Param: [todayPage, pageNo, pageSize, req]
     *@Return: org.jeecg.common.api.vo.Result<com.baomidou.mybatisplus.core.metadata.IPage<org.jeecg.modules.pc.vo.ClinicAppointPageVO>>
     *@author: xiaopeng.wu
     *@DateTime: 11:02 2023/12/7
     **/
    Result<IPage<ClinicAppointPageVO>> queryTodayPageList(ClinicAppointTodayPageDTO todayPage, Integer pageNo, Integer pageSize) throws ParseException;

    /*
     *@Description: 查询历史预约数据
     *@Param: [pageDTO, pageNo, pageSize]
     *@Return: com.baomidou.mybatisplus.core.metadata.IPage<ClinicAppointHistoryPageVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:27 2024/1/11
    **/
    IPage<ClinicAppointHistoryPageVO> queryAppointHistoryPage(ClinicAppointHistoryPageDTO pageDTO, Integer pageNo, Integer pageSize);

    /**
     * 获取就诊用户详情
     * @param appointId
     * @return
     * @throws ParseException
     */
    ClinicAppointDetailVO queryPatientDetail(String appointId);

    ClinicAppointDetailVO queryPatientClinicDetail(String appointId) throws ParseException;

    /*
     *@Description: 查询患者当前就诊详情信息
     *@Param: [appointId]
     *@Return: org.jeecg.modules.pc.vo.ClinicAppointDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 11:20 2023/12/7
    **/
    String editPatientClinicDetail(PatientClinicEditDTO clinicEditDTO);
    /*
     *@Description: 爽约后再次预约
     *@Param: [appointId]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 14:16 2023/12/1
    **/
    void againAppoint(String appointId);

    /*
     *@Description: 用户爽约
     *@Param: [appointId]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 16:32 2023/12/7
    **/
    String cancelAppoint(String appointId);
}
