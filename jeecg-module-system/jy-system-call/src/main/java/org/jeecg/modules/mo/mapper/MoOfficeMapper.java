package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.common.entity.Office;

/**
 * @Description: his_office
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoOfficeMapper extends BaseMapper<Office> {

}
