package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.vo.MoPatientCaseDetailVO;
import org.jeecg.modules.mo.vo.MoPatientClinicCasePageVO;

import java.util.List;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoPatientCaseMapper extends BaseMapper<PatientCase> {
    /*
     *@Description: 查询就医记录
     *@Param: [openId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoPatientClinicCasePageVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:50 2024/1/10
    **/
    List<MoPatientClinicCasePageVO> queryClinicPatientCaseList(@Param("openId") String openId,
                                                               @Param("queryDTO") MoClinicAppointQueryDTO queryDTO);
    /*
     *@Description: 获取患者就诊详情
     *@Param: [id]
     *@Return: org.jeecg.modules.mo.vo.PatientCaseDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 17:26 2023/12/14
    **/
    MoPatientCaseDetailVO getPatientCaseDetail(@Param("caseId") String caseId);
}
