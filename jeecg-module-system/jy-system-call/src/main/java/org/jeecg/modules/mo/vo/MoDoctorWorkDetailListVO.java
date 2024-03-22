package org.jeecg.modules.mo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 排班详情表
 * @Author: jeecg-boot
 * @Date:   2023-11-30
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MoDoctorWorkDetailListVO对象", description="MoDoctorWorkDetailListVO")
public class MoDoctorWorkDetailListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键(排班详情Id)")
    private String id;

    @ApiModelProperty(value = "值班id")
    private String doctorId;

    @ApiModelProperty(value = "出诊时段（1：全天，2：上午，3：下午）")
    private Integer workPeriod;

    @ApiModelProperty(value = "出诊时段（1：全天，2：上午，3：下午）")
    private String workPeriodDict;

    @ApiModelProperty(value = "出诊时段")
    private String clinicPeriod;

    @ApiModelProperty(value = "号源总数")
    private Integer numTotal;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出诊日期")
    private java.util.Date workDay;

    @ApiModelProperty(value = "预约详情列表")
    List<MoClinicAppointDetailListVO> clinicAppointDetailListVOList;
}
