package org.jeecg.modules.mo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
public class MoClinicAppointDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "预约值班医生详情Id;")
    private String doctorWorkDetailId;

    @ApiModelProperty(value = "患者就诊卡id")
    private String cardId;

    @ApiModelProperty(value = "患者姓名")
    private java.lang.String patientName;

    @ApiModelProperty(value = "医生Id;")
    private String doctorId;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "排队号;")
    private Integer queueNumber;

    @ApiModelProperty(value = "预约号")
    private Integer appointNumber;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private Date appointDay;

    @ApiModelProperty(value = "预约时段")
    private String appointPeriod;

    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;

    @ApiModelProperty(value = "就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约）")
    private Integer clinicStatus;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊开始时间")
    private Date clinicBeginTime;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊结束时间")
    private Date clinicEndTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
