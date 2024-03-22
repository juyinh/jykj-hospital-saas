package org.jeecg.modules.common.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date:   2023-12-13
 * @Version: V1.0
 */
@Data
@TableName("his_doctor_work")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="his_doctor_work对象", description="his_doctor_work")
public class DoctorWork implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**医生id*/
	@Excel(name = "医生id", width = 15)
    @ApiModelProperty(value = "医生id")
    private java.lang.String doctorId;
	/**就诊室id;*/
	@Excel(name = "就诊室id;", width = 15)
    @ApiModelProperty(value = "就诊室id;")
    private java.lang.String clinicRoomId;
	/**科室id;*/
	@Excel(name = "科室id;", width = 15)
    @ApiModelProperty(value = "科室id;")
    private java.lang.String officeId;
	/**出诊日期*/
	@Excel(name = "出诊日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出诊日期")
    private java.util.Date workDay;
	/**开始预约时间*/
	@Excel(name = "开始预约时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始预约时间")
    private java.util.Date appointTime;
	/**号源总数*/
	@Excel(name = "号源总数", width = 15)
    @ApiModelProperty(value = "号源总数")
    private java.lang.Integer numTotal;
	/**预约类型（1：时段，2：排队）;*/
	@Excel(name = "预约类型（1：时段，2：排队）;", width = 15)
    @ApiModelProperty(value = "预约类型（1：时段，2：排队）;")
    private java.lang.Integer appointType;
	/**已预约总数*/
	@Excel(name = "已预约总数", width = 15)
    @ApiModelProperty(value = "已预约总数")
    private java.lang.Integer appointTotal;
	/**预约状态（0：未开始、1：预约中、2：已满，3：预约结束）*/
	@Excel(name = "预约状态（0：未开始、1：预约中、2：已满，3：预约结束）", width = 15)
    @ApiModelProperty(value = "预约状态（0：未开始、1：预约中、2：已满，3：预约结束）")
    private java.lang.Integer appointStatus;
	/**出诊状态（0：未开始、1：出诊中、2：就诊完成，3：停诊）*/
	@Excel(name = "出诊状态（0：未开始、1：出诊中、2：就诊完成，3：停诊）", width = 15)
    @ApiModelProperty(value = "出诊状态（0：未开始、1：出诊中、2：就诊完成，3：停诊）")
    private java.lang.Integer clinicStatus;
	/**是的显示（1：显示，2：不显示）*/
	@Excel(name = "是的显示（1：显示，2：不显示）", width = 15)
    @ApiModelProperty(value = "是的显示（1：显示，2：不显示）")
    private java.lang.Integer isShow;
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
