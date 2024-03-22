package org.jeecg.modules.mo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@ApiModel(value="C-PatientCardListQueryDTO对象", description="his_patient_card")
public class MoPatientCardListQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "就诊openId")
    private String openId;

    @ApiModelProperty(value = "姓名")
    private String realname;

    @ApiModelProperty(value = "电话")
    private String phone;
}
