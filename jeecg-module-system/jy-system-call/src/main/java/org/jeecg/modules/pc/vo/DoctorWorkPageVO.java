package org.jeecg.modules.pc.vo;

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
import java.util.Date;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date:   2023-11-30
 * @Version: V1.0
 */
@Data
@TableName("his_doctor_work")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="his_doctor_work对象", description="his_doctor_work")
public class DoctorWorkPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**医生id*/
	@Excel(name = "医生id", width = 15)
    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "医生名称")
    private String realname;

    @ApiModelProperty(value = "就诊室id;")
    private String clinicRoomId;

    @ApiModelProperty(value = "就诊室名称")
    private String clinicRoomName;

    @ApiModelProperty(value = "科室id;")
    private String officeId;

    @ApiModelProperty(value = "科室名称")
    private String officeName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出诊日期")
    private Date workDay;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始预约时间")
    private Date appointTime;

    @ApiModelProperty(value = "号源总数")
    private Integer numTotal;

    @ApiModelProperty(value = "预约类型（1：时段，2：排队）")
    private Integer appointType;

    @ApiModelProperty(value = "已预约总数")
    private Integer appointTotal;

    @ApiModelProperty(value = "预约状态（0：未开始、1：预约中、2：已满，3：预约结束,4:过期）")
    private Integer appointStatus;

    @ApiModelProperty(value = "是否显示预约（1：显示，2：不显示）")
    private Integer isShow;

    @ApiModelProperty(value = "出诊状态（0：未开始、1：就诊中、2：就诊完成，3：停诊）,4:(过期)")
    private Integer clinicStatus;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
