package org.jeecg.modules.mo.vo;

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
@ApiModel(value="SysTenantPageVO对象", description="SysTenant")
public class MoSysTenantPageVO implements Serializable {

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

    @ApiModelProperty(value = "公司logo")
    private String companyLogo;

    @ApiModelProperty(value = "门牌号")
    private String houseNumber;

    @ApiModelProperty(value = "商家简介")
    private String description;

    @ApiModelProperty(value = "工作地点")
    private String workPlace;
    
    @ApiModelProperty(value = "审核状态（0：审核中，1：审核通过，2：审核未通过）")
    private Integer auditStatus;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "审核备注")
    private String auditRemake;

    @ApiModelProperty(name = "证件类型（1:营业执照）")
    private Integer credentialsType ;

    @ApiModelProperty(name = "证件信息")
    private String credentials;

    @ApiModelProperty(name = "营业执照")
    private String businessLicense;

    @ApiModelProperty(name = "门头照")
    private String doorImage;

    @ApiModelProperty(name = "联系电话")
    private String phone;

    @ApiModelProperty(name = "营业时间")
    private String businessTime;

    @ApiModelProperty(value = "公司地址code")
    private String companyAddressCode;
}
