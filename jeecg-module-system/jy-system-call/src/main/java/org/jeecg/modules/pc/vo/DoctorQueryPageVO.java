package org.jeecg.modules.pc.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Data
@TableName("his_doctor")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DoctorQueryPageVO对象", description="his_doctor")
public class DoctorQueryPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "医生id")
    private String doctorId;

    @ApiModelProperty(value = "科室id")
    private String officeId;

    @ApiModelProperty(value = "科室id")
    private String officeName;

    @ApiModelProperty(value = "职称")
    private String career;

    @ApiModelProperty(value = "挂号费用;")
    private BigDecimal regFee;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "名称")
    private String realname;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "生日")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private Integer sex;

    @Dict(dicCode = "user_status")
    @ApiModelProperty(value = "状态(1：正常  2：冻结 ）")
    private Integer status;

    @ApiModelProperty(value = "邮件")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

	/**创建人*/
    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
