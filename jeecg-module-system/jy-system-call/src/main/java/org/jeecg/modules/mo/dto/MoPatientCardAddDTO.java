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
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: his_patient_card
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-PatientCardAddDTO对象", description="his_patient_card")
public class MoPatientCardAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "就诊openId",required = false)
    private String openId;

    @NotEmpty(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名",required = true)
    private String realname;

    @ApiModelProperty(value = "年龄",required = false)
    private Integer age;

    @ApiModelProperty(value = "年龄",required = false)
    private java.lang.Integer month;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)",required = true)
    private Integer sex;

    @NotEmpty(message = "电话号码不能为空")
    @ApiModelProperty(value = "电话",required = true)
    private String phone;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期",required = false)
    private Date birthday;

    @ApiModelProperty(value = "证件（身份证之类）",required = false)
    private String credentials;
}
