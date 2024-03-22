package org.jeecg.modules.pc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value="ClinicAppointDetailVO对象", description="his_clinic_appoint")
public class ClinicAppointDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "医生Id")
    private String doctorId;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "病例Id")
    private String patientCaseId;

    @ApiModelProperty(value = "病人卡Id")
    private String cardId;

    @ApiModelProperty(value = "等待人数;")
    private Integer waitCount;

    @ApiModelProperty(value = "排队号码;")
    private String queueNumber;

    @ApiModelProperty(value = "预约状态")
    private Integer appointStatus;

    @ApiModelProperty(value = "完成人数;")
    private Integer finishCount;

    @ApiModelProperty(value = "姓名")
    private java.lang.String realname;

    @ApiModelProperty(value = "年龄")
    private java.lang.Integer age;

    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private java.lang.Integer sex;

    @ApiModelProperty(value = "电话")
    private java.lang.String phone;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    private java.util.Date birthday;

    @ApiModelProperty(value = "病历详情")
    private java.lang.String caseDetail;

    @ApiModelProperty(value = "就诊结果;")
    private java.lang.String diagnosisResult;

    @ApiModelProperty(value = "就诊费用;")
    private java.math.BigDecimal diagnosisFee;
}
