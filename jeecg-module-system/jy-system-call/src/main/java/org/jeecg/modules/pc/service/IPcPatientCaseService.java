package org.jeecg.modules.pc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.pc.dto.ClinicHistoryPageDTO;
import org.jeecg.modules.pc.vo.ClinicHistoryPageVO;
import org.jeecg.modules.pc.vo.PcPatientCaseDetailVO;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface IPcPatientCaseService extends IService<PatientCase> {
    /*
     *@Description: 查询就诊分页数据
     *@Param: [pageDTO]
     *@Return: com.baomidou.mybatisplus.core.metadata.IPage<java.util.List<org.jeecg.modules.pc.dto.ClinicHistoryPageDTO>>
     *@author: xiaopeng.wu
     *@DateTime: 11:05 2024/1/11
    **/
    IPage<ClinicHistoryPageVO> queryHistoryCase(ClinicHistoryPageDTO pageDTO, Integer pageNo, Integer pageSize);

    /*
     *@Description: 查询病例详情
     *@Param: [caseId]
     *@Return: org.jeecg.modules.pc.vo.PcPatientCaseDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 13:54 2024/1/11
    **/
    PcPatientCaseDetailVO queryDetailById(String caseId);
}
