package org.jeecg.modules.pc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-05
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ClinicHistoryPageDTO对象", description="就诊列表历史查询")
public class ClinicHistoryPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "患者名称(模糊查询)")
    private String patientName;

    @ApiModelProperty(value = "医生（模糊查询）")
    private String doctorName;

    @ApiModelProperty(value = "开始预约日期")
    private String beginAppointDay;

    @ApiModelProperty(value = "结束预约日期")
    private String endAppointDay;
}
