package org.jeecg.modules.pc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DoctorUpdateDTO对象", description="DoctorAddDTO")
public class DoctorFrozenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id",required = true)
    @NotEmpty(message = "用户id不能为空")
    private String userId;

    @Dict(dicCode = "user_status")
    @ApiModelProperty(value = "状态(1：正常  2：冻结 ）",required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;
}
