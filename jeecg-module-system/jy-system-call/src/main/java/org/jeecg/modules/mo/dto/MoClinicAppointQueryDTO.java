package org.jeecg.modules.mo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/30 17:30
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-MoClinicAppointQueryDTO对象", description="预约列表查询")
public class MoClinicAppointQueryDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始预约日期")
    private String beginAppointDay;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束预约日期")
    private String endAppointDay;

    @ApiModelProperty(value = "就诊状态（就诊状态 0：预约中，1：历史预约）", required = true)
    @NotEmpty(message = "就诊状态不能为空")
    private Integer clinicStatus;
}
