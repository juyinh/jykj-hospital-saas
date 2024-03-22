package org.jeecg.modules.pc.service;

import org.jeecg.modules.pc.vo.PatientAgeSexVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface IPcStatisticsService {
    /*
     *@Description: 统计最近7天患者预约就诊人数
     *@Param: []
     *@Return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     *@author: xiaopeng.wu
     *@DateTime: 14:58 2024/1/25
    **/
    List<Map<String, Object>> queryPatientWeekSum();
    /*
     *@Description: 统计患者年龄段和性别
     *@Param: [period]
     *@Return: org.jeecg.modules.pc.vo.PatientAgeSexVO
     *@author: xiaopeng.wu
     *@DateTime: 14:18 2024/1/25
    **/
    PatientAgeSexVO queryPatientAgeData(Integer period);

    /*
     *@Description: 查询就诊患者年龄比例
     *@Param: [period]
     *@Return: java.util.Map<java.lang.String,java.lang.Object>
     *@author: xiaopeng.wu
     *@DateTime: 10:49 2024/1/26
    **/
    Map<String, Object> queryPatientSexData(Integer period);
}
