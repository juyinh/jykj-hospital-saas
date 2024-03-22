package org.jeecg.modules.mo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*
 *@Description: 患者预约详情
 *@Param:
 *@Return:
 *@author: xiaopeng.wu
 *@DateTime: 14:18 2023/12/14
**/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-ClinicAppointDetailVO对象", description="his_clinic_appoint")
public class MoQueueUiDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "患者就诊卡id")
    private String cardId;

    @ApiModelProperty(value = "患者姓名")
    private String patientName;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "预约Id")
    private String appointId;

    @ApiModelProperty(value = "预约患者名称")
    private java.lang.String appointPatientName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "true：已存在排队，false：不存在")
    private Boolean isQueue;

    @ApiModelProperty(value = "挂号费用;")
    private java.math.BigDecimal regFee;
}
