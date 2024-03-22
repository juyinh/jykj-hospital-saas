package org.jeecg.modules.system.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 租户审核DTO
 * @author: xiaopeng.wu
 * @create: 2023/11/23 14:10
 **/
@Data
@Api(tags = "租户审核TDO")
public class TenantAuditDTO implements Serializable {
    /**
     * 租户Id
     */
    @ApiModelProperty(value = "租户Id")
    @NotNull(message = "租户id不能为空")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "username")
    private String createBy;

    /**
     * 审核状态（0：审核中，1：审核通过，2：审核未通过）
     */
    @ApiModelProperty(value = "审核状态（0：审核中，1：审核通过，2：审核未通过）")
    private Integer auditStatus;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    private String auditRemake;
}
