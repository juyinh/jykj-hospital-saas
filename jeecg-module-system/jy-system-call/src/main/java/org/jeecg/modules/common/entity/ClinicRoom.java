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
 * @Description: his_clinic_room
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@TableName("his_clinic_room")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="his_clinic_room对象", description="his_clinic_room")
public class ClinicRoom implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**诊所id*/
	@Excel(name = "诊所id", width = 15)
    @ApiModelProperty(value = "诊所id")
    private java.lang.String clinicId;
	/**科室id*/
	@Excel(name = "科室id", width = 15)
    @ApiModelProperty(value = "科室id")
    private java.lang.String officeId;
	/**诊室名称*/
	@Excel(name = "诊室名称", width = 15)
    @ApiModelProperty(value = "诊室名称")
    private java.lang.String clinicRoomName;
	/**诊室编号*/
	@Excel(name = "诊室编号", width = 15)
    @ApiModelProperty(value = "诊室编号")
    private java.lang.String clinicRoomCode;
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
