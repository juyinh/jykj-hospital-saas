package org.jeecg.modules.common.service.impl;

import org.jeecg.modules.common.entity.Patient;
import org.jeecg.modules.common.mapper.PatientMapper;
import org.jeecg.modules.common.service.IPatientService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: his_patient
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

}
