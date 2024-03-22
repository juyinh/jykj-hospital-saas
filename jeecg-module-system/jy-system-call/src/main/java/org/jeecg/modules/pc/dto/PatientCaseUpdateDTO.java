package org.jeecg.modules.pc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PatientCaseUpdateDTO对象", description="his_patient_case")
public class PatientCaseUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotEmpty(message = "病例Id")
    private String patientCaseId;

    @ApiModelProperty(value = "病历详情")
    private String caseDetail;

    @ApiModelProperty(value = "就诊结果;")
    private String diagnosisResult;

    @ApiModelProperty(value = "就诊费用;")
    private BigDecimal diagnosisFee;

    @ApiModelProperty(value = "支付状态（缴费状态（0：未缴费，1：已缴费））;")
    private Integer paymentStatus;
}
