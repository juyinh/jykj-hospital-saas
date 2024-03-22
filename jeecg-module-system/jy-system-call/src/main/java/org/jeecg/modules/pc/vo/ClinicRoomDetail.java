package org.jeecg.modules.pc.vo;

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
 * @Description: his_clinic_room
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ClinicRoomDetail对象", description="诊室详情")
public class ClinicRoomDetail implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "诊所id")
    private String clinicId;

    @ApiModelProperty(value = "科室id")
    private String officeId;

    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @ApiModelProperty(value = "诊室名称")
    private String clinicRoomName;

    @ApiModelProperty(value = "诊室编号")
    private String clinicRoomCode;

    @ApiModelProperty(value = "租户号")
    private Integer tenantId;

    @ApiModelProperty(value = "创建人")
    private String createBy;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
