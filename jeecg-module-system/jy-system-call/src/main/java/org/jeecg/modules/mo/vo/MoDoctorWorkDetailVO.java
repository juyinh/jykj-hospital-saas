package org.jeecg.modules.mo.vo;

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

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 排班详情表
 * @Author: jeecg-boot
 * @Date:   2023-11-30
 * @Version: V1.0
 */
@Data
@TableName("his_doctor_work_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="his_doctor_work_detail对象", description="排班详情表")
public class MoDoctorWorkDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**值班id*/
	@Excel(name = "值班id", width = 15)
    @ApiModelProperty(value = "值班id")
    private String doctorWordId;
	/**出诊时段（1：全天，2：上午，3：下午）*/
	@Excel(name = "出诊时段（1：全天，2：上午，3：下午）", width = 15)
    @ApiModelProperty(value = "出诊时段（1：全天，2：上午，3：下午）")
    private Integer workPeriod;
	/**号源总数*/
	@Excel(name = "号源总数", width = 15)
    @ApiModelProperty(value = "号源总数")
    private Integer numTotal;
	/**每个人就诊时间*/
	@Excel(name = "每个人就诊时间", width = 15)
    @ApiModelProperty(value = "每个人就诊时间")
    private Integer everyoneTimeClinic;
	/**租户号*/
	@Excel(name = "租户号", width = 15)
    @ApiModelProperty(value = "租户号")
    private Integer tenantId;
}
