package org.jeecg.modules.mo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: his_office
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Data
@TableName("his_office")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OfficeList对象", description="his_office")
public class MoOfficeListVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "科室名称")
    private String officeName;

    @ApiModelProperty(value = "科室简介")
    private String officeDescription;
}
