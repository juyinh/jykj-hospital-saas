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
@ApiModel(value="ClinicAppointHistoryPageDTO对象", description="预约详情历史查询")
public class ClinicAppointHistoryPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "患者名称(模糊查询)")
    private String patientName;

    @ApiModelProperty(value = "医生（模糊查询）")
    private String doctorName;

    @ApiModelProperty(value = "开始预约日期")
    private String beginAppointDay;

    @ApiModelProperty(value = "结束预约日期")
    private String endAppointDay;

    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;
}
