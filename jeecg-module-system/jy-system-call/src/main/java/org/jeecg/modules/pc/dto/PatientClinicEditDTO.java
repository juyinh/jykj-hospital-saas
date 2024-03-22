package org.jeecg.modules.pc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PatientClinicEditDTO对象", description="PatientClinicEditDTO")
public class PatientClinicEditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约id",required = true)
    @NotEmpty(message = "预约id不能为空")
    private String appointId;

    @ApiModelProperty(value = "病历详情",required = false)
    private String caseDetail;

    @ApiModelProperty(value = "就诊结果;",required = false)
    private String diagnosisResult;

    @ApiModelProperty(value = "就诊费用;",required = false)
    private BigDecimal diagnosisFee;
}
