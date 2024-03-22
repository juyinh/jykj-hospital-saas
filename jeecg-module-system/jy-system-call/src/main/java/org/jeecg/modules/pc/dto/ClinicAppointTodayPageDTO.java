package org.jeecg.modules.pc.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@TableName("his_clinic_appoint")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ClinicAppointTodayPage对象", description="his_clinic_appoint")
public class ClinicAppointTodayPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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
}
