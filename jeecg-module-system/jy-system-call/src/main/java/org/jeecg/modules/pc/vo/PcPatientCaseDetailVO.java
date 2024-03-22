package org.jeecg.modules.pc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/*
 *@Description: 患者获取用户就诊详情
 *@Param:
 *@Return:
 *@author: xiaopeng.wu
 *@DateTime: 14:52 2023/12/14
**/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PcPatientCaseDetailVO对象", description="PcPatientCaseDetailVO")
public class PcPatientCaseDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "就诊病例Id")
    private String id;

    @ApiModelProperty(value = "就诊卡id")
    private String cardId;

    @ApiModelProperty(value = "患者名称")
    private String patientName;

    @ApiModelProperty(value = "预约id")
    private String appointId;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "病历详情")
    private String caseDetail;

    @ApiModelProperty(value = "就诊结果;")
    private String diagnosisResult;

    @ApiModelProperty(value = "就诊费用;")
    private BigDecimal diagnosisFee;

    @ApiModelProperty(value = "支付状态（缴费状态（0：未缴费，1：已缴费））;")
    private Integer paymentStatus;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "姓名")
    private String realname;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;


    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @ApiModelProperty(value = "预约日期")
    private String appointDay;

    @ApiModelProperty(value = "就诊时间")
    private String clinicTime;
}
