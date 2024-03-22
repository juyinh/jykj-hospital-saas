package org.jeecg.modules.pc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pc.dto.DoctorWorkDetailAddDTO;
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
@ApiModel(value="DoctorWorkAddDTO对象", description="DoctorWorkAddDTO")
public class DoctorWorkQueryVO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "医生名称")
    private String realname;

    @ApiModelProperty(value = "就诊室id")
    private String clinicRoomId;
    
    @ApiModelProperty(value = "就诊室名称")
    private String clinicRoomName;

    @ApiModelProperty(value = "科室id（科室信息可以为空、为空时获取医生科室）")
    private String officeId;
    
    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出诊日期")
    private java.util.Date workDay;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始预约时间",required = false)
    private java.util.Date appointTime;

    @ApiModelProperty(value = "号源总数")
    private Integer numTotal;

    @ApiModelProperty(value = "预约类型（1：时段，2：排队）")
    private Integer appointType;

    @ApiModelProperty(value = "预约状态（0：未开始、1：预约中、2：已满，3：预约结束）")
    private Integer appointStatus;

    @ApiModelProperty(value = "出诊状态（0：未开始、1：就诊中、2：就诊完成，3：停诊）")
    private Integer clinicStatus;

    @ApiModelProperty(value = "是否显示预约（1：显示，2：不显示）")
    private Integer isShow;

    @ApiModelProperty(value = "出诊详情")
    List<DoctorWorkDetailQueryVO> workDetailAddDTOList;
}
