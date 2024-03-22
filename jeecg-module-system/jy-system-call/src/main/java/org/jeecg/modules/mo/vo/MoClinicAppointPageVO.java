package org.jeecg.modules.mo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-ClinicAppointPageVO对象", description="his_clinic_appoint")
public class MoClinicAppointPageVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "预约值班医生详情Id;")
    private String doctorWorkDetailId;

    @ApiModelProperty(value = "患者就诊卡id")
    @Dict(dictTable = "his_patient_card", dicCode = "id", dicText = "realname")
    private String cardId;
	/**医生Id;*/
	@Excel(name = "医生Id;", width = 15)
    @ApiModelProperty(value = "医生Id;")
    private String doctorId;
	/**排队号;*/
	@Excel(name = "排队号;", width = 15)
    @ApiModelProperty(value = "排队号;")
    private Integer queueNumber;
	/**预约号*/
	@Excel(name = "预约号", width = 15)
    @ApiModelProperty(value = "预约号")
    private Integer appointNumber;
	/**预约日期*/
	@Excel(name = "预约日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预约日期")
    private Date appointDay;
	/**预约时段*/
	@Excel(name = "预约时段", width = 15)
    @ApiModelProperty(value = "预约时段")
    private String appointPeriod;
	/**预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）*/
	@Excel(name = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）", width = 15)
    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;
    /**就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约）*/
    @Excel(name = "就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约）", width = 15)
    @ApiModelProperty(value = "就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约）")
    private Integer clinicStatus;
	/**就诊开始时间*/
	@Excel(name = "就诊开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊开始时间")
    private Date clinicBeginTime;
	/**就诊结束时间*/
	@Excel(name = "就诊结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊结束时间")
    private Date clinicEndTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
