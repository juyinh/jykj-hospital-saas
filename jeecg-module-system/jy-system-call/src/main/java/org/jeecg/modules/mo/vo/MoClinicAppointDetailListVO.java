package org.jeecg.modules.mo.vo;

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
@ApiModel(value="C-MoClinicAppointDetailListVO", description="查询预约详情列表")
public class MoClinicAppointDetailListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约主键Id")
    private String appointId;

    @ApiModelProperty(value = "就诊时段")
    private String appointPeriod;

    @ApiModelProperty(value = "预约状态（预约状态 -1：不可预约，0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;
}
