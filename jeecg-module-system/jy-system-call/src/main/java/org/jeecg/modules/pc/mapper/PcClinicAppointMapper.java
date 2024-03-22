package org.jeecg.modules.pc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.pc.dto.ClinicAppointHistoryPageDTO;
import org.jeecg.modules.pc.vo.ClinicAppointDetailVO;
import org.jeecg.modules.pc.vo.ClinicAppointHistoryPageVO;
import org.jeecg.modules.pc.vo.PatientAgeSexVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-05
 * @Version: V1.0
 */
public interface PcClinicAppointMapper extends BaseMapper<ClinicAppoint> {
    /*
     *@Description: 查询用户就诊详情
     *@Param: [appointId]
     *@Return: org.jeecg.modules.pc.vo.ClinicAppointDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 11:41 2023/12/7
    **/
    ClinicAppointDetailVO getPatientClinicDetail(String appointId);

    /*
     *@Description: 查询预约历史记录
     *@Param: [pageDTO, doctorId]
     *@Return: java.util.List<org.jeecg.modules.pc.vo.ClinicAppointHistoryPageVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:32 2024/1/11
    **/
    List<ClinicAppointHistoryPageVO> getAppointHistoryList(@Param("pageDTO") ClinicAppointHistoryPageDTO pageDTO, @Param("doctorId") String doctorId);

    /*
     *@Description: 统计患者预约就诊人数
     *@Param: [beginTime, endTime]
     *@Return: org.jeecg.modules.pc.vo.PatientAgeSexVO
     *@author: xiaopeng.wu
     *@DateTime: 18:04 2024/1/24
    **/
    List<Map<String,Object>> getPatientAppointClinicSum(String beginTime, String endTime);

    /*
     *@Description: 获取患者年龄统计数据
     *@Param: [beginTime, endTime]
     *@Return: org.jeecg.modules.pc.vo.PatientAgeSexVO
     *@author: xiaopeng.wu
     *@DateTime: 18:04 2024/1/24
     **/
    PatientAgeSexVO getAgeData(String beginTime, String endTime);

    /*
     *@Description: 获取就诊性别统计
     *@Param: [beginTime, endTime]
     *@Return: java.util.Map<java.lang.String,java.lang.Object>
     *@author: xiaopeng.wu
     *@DateTime: 15:10 2024/1/25
    **/
    Map<String, Object> getSexData(String beginTime, String endTime);
}
