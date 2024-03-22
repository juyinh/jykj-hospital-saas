package org.jeecg.modules.pc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date:   2023-12-05
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ClinicHistoryPageVO对象", description="ClinicHistoryPageVO查询历史就诊数据")
public class ClinicHistoryPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "病例Id主键（caseId）")
    private String id;

    @ApiModelProperty(value = "患者名称")
    private String patientName;

    @ApiModelProperty(value = "医院名称")
    private String doctorName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "就诊开始时间")
    private java.util.Date clinicBeginTime;

    @ApiModelProperty(value = "就诊状态（就诊状态 0：未就诊，1：排队中、2：就诊中，3：就诊完成，4：爽约）")
    private Integer clinicStatus;
}
