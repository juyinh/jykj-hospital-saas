package org.jeecg.modules.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.common.mapper.PatientCaseMapper;
import org.jeecg.modules.common.service.IPatientCaseService;
import org.springframework.stereotype.Service;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Service
public class PatientCaseServiceImpl extends ServiceImpl<PatientCaseMapper, PatientCase> implements IPatientCaseService {

}
