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
@ApiModel(value="ClinicAppointHistoryPageVO对象", description="查询预约历史记录数据")
public class ClinicAppointHistoryPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "患者名称")
    private String patientName;

    @ApiModelProperty(value = "医生Id;")
    private String doctorName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private java.util.Date appointDay;

    @ApiModelProperty(value = "预约时段")
    private String appointPeriod;

    @ApiModelProperty(value = "预约状态（预约状态 0：未预约，1：预约中，2：成功，3：失败，4：取消）")
    private Integer appointStatus;
}
