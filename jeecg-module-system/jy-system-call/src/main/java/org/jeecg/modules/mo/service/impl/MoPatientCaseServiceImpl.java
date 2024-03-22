package org.jeecg.modules.mo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.mapper.MoPatientCaseMapper;
import org.jeecg.modules.mo.service.IMoPatientCaseService;
import org.jeecg.modules.mo.vo.MoPatientCaseDetailVO;
import org.jeecg.modules.mo.vo.MoPatientClinicCasePageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date: 2023-12-01
 * @Version: V1.0
 */
@Service
public class MoPatientCaseServiceImpl extends ServiceImpl<MoPatientCaseMapper, PatientCase> implements IMoPatientCaseService {
    @Resource
    private MoPatientCaseMapper moPatientCaseMapper;

    @Override
    public IPage<MoPatientClinicCasePageVO> queryClinicPatientCasePage(String openId,
                                                                       MoClinicAppointQueryDTO queryDTO,
                                                                       Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<MoPatientClinicCasePageVO> clinicCasePageVOList = moPatientCaseMapper.queryClinicPatientCaseList(openId, queryDTO);
        return oConvertUtils.pageInfoToIPage(new PageInfo<>(clinicCasePageVOList));
    }

    public MoPatientCaseDetailVO getPatientCaseDetail(String caseId) {
        MoPatientCaseDetailVO caseDetailVO = moPatientCaseMapper.getPatientCaseDetail(caseId);
        return caseDetailVO;
    }
}
