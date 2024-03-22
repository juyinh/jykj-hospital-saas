package org.jeecg.modules.mo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-05
 * @Version: V1.0
 */
@Data
@TableName("his_clinic_appoint")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MoPatientClinicCasePageVO对象", description="MoPatientClinicCasePageVO")
public class MoPatientClinicCasePageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "就诊病例Id")
    private String id;

    @ApiModelProperty(value = "患者就诊卡id")
    private String cardId;

    @ApiModelProperty(value = "患者名称")
    private String patientName;


    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @ApiModelProperty(value = "就诊开始时间")
    private String clinicBeginTime;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "公司地址")
    private String companyAddress;
}
