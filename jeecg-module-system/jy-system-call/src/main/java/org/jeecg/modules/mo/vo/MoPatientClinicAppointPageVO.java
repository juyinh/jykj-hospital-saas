package org.jeecg.modules.mo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-05
 * @Version: V1.0
 */
@Data
@TableName("his_clinic_appoint")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MoPatientClinicAppointListVO对象", description="MoPatientClinicAppointListVO")
public class MoPatientClinicAppointPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约主键Id")
    private String id;

    @ApiModelProperty(value = "患者就诊卡id")
    private String cardId;

    @ApiModelProperty(value = "患者名称")
    private String patientName;

    @ApiModelProperty(value = "医生Id;")
    private String doctorId;

    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @ApiModelProperty(value = "预约时段")
    private String appointPeriod;

    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    @Dict(dicCode = "patinet_appoint_status")
    private Integer appointStatus;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊开始时间")
    private String clinicBeginTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private Date appointDay;

    @ApiModelProperty(value = "租户号")
    private Integer tenantId;

    @ApiModelProperty(value = "医院名称")
    private String tenantName;

    @ApiModelProperty(value = "公司地址")
    private String companyAddress;

    @ApiModelProperty(value = "公司地址Code")
    private String companyAddressCode;

    @ApiModelProperty(value = "挂号费用;")
    private java.math.BigDecimal regFee;

    @ApiModelProperty(value = "是否过期（true：是，false：否）")
    private Boolean isExpired;
}
