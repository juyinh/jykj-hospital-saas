package org.jeecg.modules.mo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/30 17:30
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-ClinicAppointQueueAddDTO对象", description="诊所预约排队")
public class MoClinicAppointQueueAddDTO {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "预约医院ID")
    @ApiModelProperty(value = "预约医院ID")
    private java.lang.String tenantId;

    @ApiModelProperty(value = "就诊卡id")
    private String cardId;

    @NotEmpty(message = "预约医生不能为空")
    @ApiModelProperty(value = "预约医生")
    private String doctorId;

    @ApiModelProperty(value = "预约Id")
    private String appointId;

    @ApiModelProperty(value = "预约Id")
    private String openId;
}
