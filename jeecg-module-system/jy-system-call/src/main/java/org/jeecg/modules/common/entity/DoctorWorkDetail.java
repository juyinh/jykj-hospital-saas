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
 * @Description: 排班详情表
 * @Author: jeecg-boot
 * @Date:   2023-11-30
 * @Version: V1.0
 */
@Data
@TableName("his_doctor_work_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="his_doctor_work_detail对象", description="排班详情表")
public class DoctorWorkDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**值班id*/
	@Excel(name = "值班id", width = 15)
    @ApiModelProperty(value = "值班id")
    private java.lang.String doctorWordId;
	/**出诊时段（1：全天，2：上午，3：下午）*/
	@Excel(name = "出诊时段（1：全天，2：上午，3：下午）", width = 15)
    @ApiModelProperty(value = "出诊时段（1：全天，2：上午，3：下午）")
    private java.lang.Integer workPeriod;
	/**号源总数*/
	@Excel(name = "号源总数", width = 15)
    @ApiModelProperty(value = "号源总数")
    private java.lang.Integer numTotal;
	/**每个人就诊时间*/
	@Excel(name = "每个人就诊时间", width = 15)
    @ApiModelProperty(value = "每个人就诊时间")
    private java.lang.Integer everyoneTimeClinic;
	/**开始就诊时间*/
	@Excel(name = "开始就诊时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始就诊时间")
    private java.util.Date beginTimeClinic;
	/**结束就诊时间*/
	@Excel(name = "结束就诊时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束就诊时间")
    private java.util.Date endTimeClinic;
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
