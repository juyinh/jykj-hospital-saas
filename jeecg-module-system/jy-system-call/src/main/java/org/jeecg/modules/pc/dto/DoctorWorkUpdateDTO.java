package org.jeecg.modules.pc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/29 16:23
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DoctorWorkUpdateDTO对象", description="DoctorWorkAddDTO")
public class DoctorWorkUpdateDTO {
    @ApiModelProperty(value = "id",required = true)
    @NotEmpty(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "医生id",required = true)
    @NotEmpty(message = "医生信息不能为空")
    private String doctorId;

    @ApiModelProperty(value = "就诊室id",required = true)
    @NotEmpty(message = "就诊室信息不能为空")
    private String clinicRoomId;

    @ApiModelProperty(value = "科室id（科室信息可以为空、为空时获取医生科室）")
    private String officeId;

    @NotNull(message = "出诊日期不能为空")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出诊日期",required = true)
    private java.util.Date workDay;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始预约时间",required = false)
    private java.util.Date appointTime;

    @ApiModelProperty(value = "号源总数",required = true)
    private Integer numTotal;

    @NotNull(message = "预约类型不能为空")
    @Min(value = 1,message = "最小是1")
    @Max(value = 2, message = "最大是2")
    @ApiModelProperty(value = "预约类型（1：时段，2：排队）")
    private Integer appointType;

    @Min(value = 1,message = "最小是1")
    @Max(value = 2, message = "最大是2")
    @ApiModelProperty(value = "是否显示预约（1：显示，2：不显示）")
    private Integer isShow;

    @Valid
    @NotNull(message = "出诊详情不能为空")
    @ApiModelProperty(value = "出诊详情",required = true)
    List<DoctorWorkDetailUpdateDTO> workDetailUpdateDTOList;
}
