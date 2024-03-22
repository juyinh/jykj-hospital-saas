package org.jeecg.modules.mo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 租户信息
 * @author: jeecg-boot
 */
@Data
@ApiModel(value="HospitalPageDTO对象", description="SysTenant")
public class MoHospitalPageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "医院名称")
    private String name;
}
