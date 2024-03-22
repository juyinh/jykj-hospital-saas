package org.jeecg.modules.pc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.pc.dto.ClinicHistoryPageDTO;
import org.jeecg.modules.pc.vo.ClinicHistoryPageVO;
import org.jeecg.modules.pc.vo.PcPatientCaseDetailVO;

import java.util.List;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface PcPatientCaseMapper extends BaseMapper<PatientCase> {
    /*
     *@Description: 查询就诊历史记录
     *@Param: [dto, doctorId]
     *@Return: java.util.List<org.jeecg.modules.pc.vo.ClinicHistoryPageVO>
     *@author: xiaopeng.wu
     *@DateTime: 11:15 2024/1/11
    **/
    List<ClinicHistoryPageVO> queryHistoryCase(@Param("dto") ClinicHistoryPageDTO dto, @Param("doctorId")String doctorId);

    /*
     *@Description: 获取病历详情
     *@Param: [caseId]
     *@Return: org.jeecg.modules.pc.vo.PcPatientCaseDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 13:41 2024/1/11
    **/
    PcPatientCaseDetailVO getPatientCaseDetail(@Param("caseId") String caseId);
}
