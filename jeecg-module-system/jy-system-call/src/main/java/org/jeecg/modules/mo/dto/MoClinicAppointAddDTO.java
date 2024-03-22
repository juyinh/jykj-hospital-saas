package org.jeecg.modules.mo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/30 17:30
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-ClinicAppointAddDTO对象", description="添加就诊预约")
public class MoClinicAppointAddDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "openId")
    private java.lang.String openId;

    @NotEmpty(message = "预约医生不能为空")
    @ApiModelProperty(value = "预约医生", required = true)
    private java.lang.String doctorId;

    @NotEmpty(message = "就诊卡id不能为空")
    @ApiModelProperty(value = "就诊卡id", required = true)
    private java.lang.String cardId;

    @NotEmpty(message = "预约日期不能为空")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期", required = true)
    private String appointDay;

    @ApiModelProperty(value = "预约开始时间点", required = true)
    private String appointTime;

    @NotEmpty(message = "预约Id不能为空")
    @ApiModelProperty(value = "预约Id", required = true)
    private java.lang.String appointId;

    @NotNull(message = "出诊时段不能为空")
    @ApiModelProperty(value = "出诊时段（1：全天，2：上午，3：下午）", required = true)
    private Integer workPeriod;
}
