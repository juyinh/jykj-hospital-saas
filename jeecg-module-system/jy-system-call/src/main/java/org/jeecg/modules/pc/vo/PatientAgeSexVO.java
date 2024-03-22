package org.jeecg.modules.pc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: his_patient
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PatientAgeSexVO对象", description="PatientAgeSexVO")
public class PatientAgeSexVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "3以下")
    private Long agebelow3;

    @ApiModelProperty(value = "3-10")
    private Long age3To10;

    @ApiModelProperty(value = "10-20")
    private Long age10To20;

    @ApiModelProperty(value = "20-40")
    private Long age20To40;

    @ApiModelProperty(value = "40-60")
    private Long age40To60;

    @ApiModelProperty(value = "60以上")
    private Long ageAbove60;
}
