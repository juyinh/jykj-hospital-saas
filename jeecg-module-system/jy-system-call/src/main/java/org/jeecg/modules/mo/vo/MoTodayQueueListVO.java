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
@ApiModel(value="C-MoTodayQueueVO对象", description="his_clinic_appoint")
public class MoTodayQueueListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "当前排队人数")
    private Long nowQueueCount;

    @ApiModelProperty(value = "患者就诊卡id")
    private String cardId;

    @ApiModelProperty(value = "患者姓名")
    private String patientName;

    @ApiModelProperty(value = "科室名称")
    private java.lang.String officeName;

    @ApiModelProperty(value = "医生Id;")
    private String doctorId;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "排队号;")
    private String queueNumber;

    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;

    @ApiModelProperty(value = "诊室名称")
    private java.lang.String clinicRoomName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "排队时间")
    private Date queueDay;
}
