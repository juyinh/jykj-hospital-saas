package org.jeecg.modules.mo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

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
@ApiModel(value="MoDoctorWorkDayList对象", description="医生值班日期")
public class MoDoctorWorkDayListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键（医生排班id）")
    private String id;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出诊日期")
    private String workDay;
}
