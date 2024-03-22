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
@ApiModel(value="DoctorAddDTO对象", description="DoctorAddDTO")
public class DoctorAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录账号(手机号)",required = true)
    @NotEmpty(message = "登录账号不能为空")
    private String phone;

    @ApiModelProperty(value = "真实姓名",required = true)
    @NotEmpty(message = "真实姓名不能为空")
    private String realname;

    @ApiModelProperty(value = "科室id",required = true)
    @NotEmpty(message = "科室信息不能为空")
    private String officeId;

    @ApiModelProperty(value = "诊室id",required = true)
    @NotEmpty(message = "诊室不能为空")
    private String clinicRoomId;

    @ApiModelProperty(value = "职称",required = true)
    @NotEmpty(message = "职称不能为空")
    private String career;

    @ApiModelProperty(value = "简介",required = true)
    @NotEmpty(message = "简介不能为空")
    private String introduction;

    @ApiModelProperty(value = "角色", required = true)
    @NotEmpty(message = "角色不能为空")
    private String roleId;

    @ApiModelProperty(value = "头像",required = true)
    @NotEmpty(message = "头像不能为空")
    private String avatar;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    @Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别（1：男 2：女）")
    private Integer sex;

    @ApiModelProperty(value = "电子邮件")
    private String email;


    @ApiModelProperty(value = "出诊费用")
    private BigDecimal regFee;

    @ApiModelProperty(value = "部门")
    private String depart;
}
