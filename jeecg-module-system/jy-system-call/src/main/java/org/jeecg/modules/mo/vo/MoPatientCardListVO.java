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
import java.util.Date;

/**
 * @Description: his_patient_card
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@TableName("his_patient_card")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C-PatientCardList对象", description="his_patient_card")
public class MoPatientCardListVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "就诊openId")
    private String openId;

    @ApiModelProperty(value = "姓名")
    private String realname;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;

    @ApiModelProperty(value = "电话")
    private String phone;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "证件")
    private String credentials;
}
