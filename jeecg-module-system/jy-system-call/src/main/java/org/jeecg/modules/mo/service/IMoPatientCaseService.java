package org.jeecg.modules.mo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.vo.MoPatientCaseDetailVO;
import org.jeecg.modules.mo.vo.MoPatientClinicCasePageVO;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date: 2023-12-01
 * @Version: V1.0
 */
public interface IMoPatientCaseService extends IService<PatientCase> {
    /*
     *@Description: 查询就诊记录
     *@Param: [openId, pageNo, pageSize]
     *@Return: Result<IPage<MoPatientClinicCasePageVO>>
     *@author: xiaopeng.wu
     *@DateTime: 10:48 2024/1/10
     **/
    IPage<MoPatientClinicCasePageVO> queryClinicPatientCasePage(String openId,
                                                                MoClinicAppointQueryDTO queryDTO,
                                                                Integer pageNo,
                                                                Integer pageSize);

    /*
     *@Description: 获取患者病例详情
     *@Param: [id]
     *@Return: org.jeecg.modules.mo.vo.PatientCaseDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 17:28 2023/12/14
     **/
    MoPatientCaseDetailVO getPatientCaseDetail(String caseId);
}
