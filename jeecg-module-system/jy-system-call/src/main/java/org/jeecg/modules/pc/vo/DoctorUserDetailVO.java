package org.jeecg.modules.pc.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DoctorUserDetail对象", description="his_doctor")
public class DoctorUserDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("userId")
    private String id;

    @ApiModelProperty("角色Id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("登录账号")
    private String username;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("头像")
    private String avatar;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("生日")
    private Date birthday;

    @Dict(dicCode = "sex")
    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    @Dict(dicCode = "user_status")
    @ApiModelProperty("状态(1：正常  2：冻结 ）")
    private Integer status;

    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
    @TableLogic
    @ApiModelProperty("删除状态（0，正常，1已删除）")
    private Integer delFlag;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty(value = "科室id")
    private java.lang.String officeId;

    @ApiModelProperty(value = "科室名称")
    private java.lang.String officeName;

    @ApiModelProperty(value = "诊室Id")
    private java.lang.String clinicRoomId;

    @ApiModelProperty(value = "诊室名称")
    private java.lang.String clinicRoomName;

    @ApiModelProperty(value = "用户id")
    private java.lang.String userId;

    @ApiModelProperty(value = "医生Id")
    private java.lang.String doctorId;

    @ApiModelProperty(value = "职称")
    private java.lang.String career;

    @ApiModelProperty(value = "简介")
    private java.lang.String introduction;

    @ApiModelProperty(value = "挂号费用;")
    private java.math.BigDecimal regFee;
}
