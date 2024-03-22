package org.jeecg.modules.pc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 排班详情表
 * @Author: jeecg-boot
 * @Date:   2023-11-30
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DoctorWorkDetailUpdateDTO对象", description="排班详情表")
public class DoctorWorkDetailUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "出诊时段（1：全天，2：上午，3：下午）",required = true)
    @NotNull(message = "出诊时段不能为空")
    private Integer workPeriod;

    @ApiModelProperty(value = "号源总数",required = true)
    @NotNull(message = "号源总数不能为空")
    private Integer numTotal;

    @ApiModelProperty(value = "每个人就诊时间")
    private Integer everyoneTimeClinic;

	@JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss")
    @ApiModelProperty(value = "开始就诊时间")
    private Date beginTimeClinic;

	@JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss")
    @ApiModelProperty(value = "结束就诊时间")
    private Date endTimeClinic;
}
