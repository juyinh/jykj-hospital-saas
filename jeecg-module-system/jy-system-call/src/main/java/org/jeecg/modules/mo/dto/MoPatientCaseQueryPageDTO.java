package org.jeecg.modules.mo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-PatientCaseQueryPage对象", description="his_patient_case")
public class MoPatientCaseQueryPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @NotEmpty(message = "就诊卡id不能为空")
    @ApiModelProperty(value = "就诊卡id",required = true)
    private String cardId;

    @ApiModelProperty(value = "预约id",required = false)
    private String appointId;

    @ApiModelProperty(value = "医生id",required = true)
    private String doctorId;
}
