package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户信息
 * @author: jeecg-boot
 */
@Data
@TableName("sys_tenant")
@ApiModel(value="SysTenant对象", description="SysTenant")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "租户名称")
    private String name;


    @ApiModelProperty(value = "创建人")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "username")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty(value = "状态 1正常 0冻结")
    @Dict(dicCode = "tenant_status")
    private Integer status;

    @ApiModelProperty(value = "所属行业")
    @Dict(dicCode = "trade")
    private String trade;

    @ApiModelProperty(value = "公司规模")
    @Dict(dicCode = "company_size")
    private String companySize;

    @ApiModelProperty(value = "公司地址")
    private String companyAddress;

    @ApiModelProperty(value = "公司地址code")
    private String companyAddressCode;

    @ApiModelProperty(value = "公司logo")
    private String companyLogo;

    @ApiModelProperty(value = "门牌号")
    private String houseNumber;

    @ApiModelProperty(value = "商家简介")
    private String description;

    /**
     * 工作地点
     */
    @ApiModelProperty(value = "工作地点")
    private String workPlace;

    /**
     * 二级域名(暂时无用,预留字段)
     */
    @ApiModelProperty(value = "二级域名(暂时无用,预留字段)")
    private String secondaryDomain;

    /**
     * 登录背景图片(暂时无用，预留字段)
     */
    @ApiModelProperty(value = "登录背景图片(暂时无用，预留字段)")
    private String loginBkgdImg;

    /**
     * 职级
     */
    @ApiModelProperty(value = "职级")
    @Dict(dicCode = "company_rank")
    private String position;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    @Dict(dicCode = "company_department")
    private String department;
    
    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    /**更新人登录名称*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 允许申请管理员 1允许 0不允许
     */
    @ApiModelProperty(value = "允许申请管理员 1允许 0不允许")
    private Integer applyStatus;
    /**
     * 审核状态（0：审核中，1：审核通过，2：审核未通过）
     */
    @ApiModelProperty(value = "审核状态（0：审核中，1：审核通过，2：审核未通过）")
    private Integer auditStatus;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    private String auditRemake;

    /** 证件类型（1:营业执照） */
    @ApiModelProperty(name = "证件类型（1:营业执照）")
    private Integer credentialsType ;

    /** 证件 */
    @ApiModelProperty(name = "证件信息")
    private String credentials;

    /** 营业执照 */
    @ApiModelProperty(name = "营业执照")
    private String businessLicense;

    /** 营业执照 */
    @ApiModelProperty(name = "门头照")
    private String doorImage;

    /** 联系电话 */
    @ApiModelProperty(name = "联系电话")
    private String phone;

    /** 营业时间 */
    @ApiModelProperty(name = "营业时间")
    private String businessTime;
}
