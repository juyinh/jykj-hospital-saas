package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.common.entity.PatientCard;

/**
 * @Description: his_patient_card
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoPatientCardMapper extends BaseMapper<PatientCard> {

}
