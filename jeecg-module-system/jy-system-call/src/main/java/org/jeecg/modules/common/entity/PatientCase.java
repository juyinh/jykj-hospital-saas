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
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Data
@TableName("his_patient_case")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="his_patient_case对象", description="his_patient_case")
public class PatientCase implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**就诊卡id*/
	@Excel(name = "就诊卡id", width = 15)
    @ApiModelProperty(value = "就诊卡id")
    private java.lang.String cardId;
	/**预约id*/
	@Excel(name = "预约id", width = 15)
    @ApiModelProperty(value = "预约id")
    private java.lang.String appointId;
	/**医生id*/
	@Excel(name = "医生id", width = 15)
    @ApiModelProperty(value = "医生id")
    private java.lang.String doctorId;
	/**病历详情*/
	@Excel(name = "病历详情", width = 15)
    @ApiModelProperty(value = "病历详情")
    private java.lang.String caseDetail;
	/**就诊结果;*/
	@Excel(name = "就诊结果;", width = 15)
    @ApiModelProperty(value = "就诊结果;")
    private java.lang.String diagnosisResult;
	/**就诊费用;*/
	@Excel(name = "就诊费用;", width = 15)
    @ApiModelProperty(value = "就诊费用;")
    private java.math.BigDecimal diagnosisFee;
	/**支付状态（缴费状态（0：未缴费，1：已缴费））;*/
	@Excel(name = "支付状态（缴费状态（0：未缴费，1：已缴费））;", width = 15)
    @ApiModelProperty(value = "支付状态（缴费状态（0：未缴费，1：已缴费））;")
    private java.lang.Integer paymentStatus;
    @ApiModelProperty(value = "删除（0：未删除，1：已删除）")
    private java.lang.Integer delFlag;
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
