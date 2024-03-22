package org.jeecg.modules.pc.dto;

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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DoctorWorkPageDTO对象", description="his_doctor_work")
public class DoctorWorkPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "医生名称")
    private String realname;

    @ApiModelProperty(value = "科室id")
    private String officeId;

    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @ApiModelProperty(value = "诊室名称")
    private String clinicRoomName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出诊日期")
    private Date workDay;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始预约时间")
    private Date appointTime;

    @ApiModelProperty(value = "预约类型（1：时段，2：排队）")
    private Integer appointType;

    @ApiModelProperty(value = "预约状态（0：未开始、1：预约中、2：已满，3：预约结束")
    private Integer appointStatus;

    @ApiModelProperty(value = "出诊状态（0：未开始、1：出诊中、2：就诊完成，3：停诊）")
    private Integer clinicStatus;

    @ApiModelProperty(value = "租户Id")
    private Integer tenantId;
}
