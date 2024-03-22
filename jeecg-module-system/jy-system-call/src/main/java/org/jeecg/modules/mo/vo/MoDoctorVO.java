package org.jeecg.modules.mo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MoDoctorVO对象", description="his_doctor")
public class MoDoctorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("userId")
    private String id;

    @ApiModelProperty("医生排班Id")
    private String doctorWorkId;

    @ApiModelProperty("doctorId")
    private String doctorId;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("头像")
    private String avatar;

    @Dict(dicCode = "sex")
    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty(value = "职称")
    private String career;

    @ApiModelProperty(value = "简介")
    private java.lang.String introduction;

    @ApiModelProperty(value = "挂号费用;")
    private java.math.BigDecimal regFee;
}
