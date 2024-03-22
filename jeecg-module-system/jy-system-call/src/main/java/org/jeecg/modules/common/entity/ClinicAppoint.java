package org.jeecg.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

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
@ApiModel(value="his_clinic_appoint对象", description="his_clinic_appoint")
public class ClinicAppoint implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**预约值班医生详情Id;*/
	@Excel(name = "预约值班医生详情Id;", width = 15)
    @ApiModelProperty(value = "预约值班医生详情Id;")
    private java.lang.String doctorWorkDetailId;
	/**openId;*/
	@Excel(name = "openId;", width = 15)
    @ApiModelProperty(value = "openId;")
    private java.lang.String openId;
	/**患者就诊卡id*/
	@Excel(name = "患者就诊卡id", width = 15)
    @ApiModelProperty(value = "患者就诊卡id")
    private java.lang.String cardId;
	/**医生Id;*/
	@Excel(name = "医生Id;", width = 15)
    @ApiModelProperty(value = "医生Id;")
    private java.lang.String doctorId;
	/**排队号;*/
	@Excel(name = "排队号;", width = 15)
    @ApiModelProperty(value = "排队号;")
    private java.lang.Integer queueNumber;
	/**预约号*/
	@Excel(name = "预约号", width = 15)
    @ApiModelProperty(value = "预约号")
    private java.lang.Integer appointNumber;
	/**预约日期*/
	@Excel(name = "预约日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private java.util.Date appointDay;
	/**预约时段*/
	@Excel(name = "预约时段", width = 15)
    @ApiModelProperty(value = "预约时段")
    private java.lang.String appointPeriod;
    /**预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）*/
    @Excel(name = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）", width = 15)
    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;
    /**是否显示预约（1：显示，2：不显示）*/
    @Excel(name = "是否显示预约（1：显示，2：不显示）", width = 15)
    @ApiModelProperty(value = "是否显示预约（1：显示，2：不显示）")
    private Integer isShow;
    @ApiModelProperty(value = "删除（0：未删除，1：已删除）")
    private java.lang.Integer delFlag;
    /**就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约，5:取消）*/
    @Excel(name = "就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约）", width = 15)
    @ApiModelProperty(value = "就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约，5：取消）")
    private Integer clinicStatus;
	/**就诊开始时间*/
	@Excel(name = "就诊开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊开始时间")
    private java.util.Date clinicBeginTime;
	/**就诊结束时间*/
	@Excel(name = "就诊结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊结束时间")
    private java.util.Date clinicEndTime;
	/**租户号*/
	@Excel(name = "租户号", width = 15)
    @ApiModelProperty(value = "租户号")
    private java.lang.Integer tenantId;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
