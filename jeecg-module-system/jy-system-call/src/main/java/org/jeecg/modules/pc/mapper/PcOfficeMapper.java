package org.jeecg.modules.pc.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.common.entity.Office;

/**
 * @Description: his_office
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
public interface PcOfficeMapper extends BaseMapper<Office> {
    @InterceptorIgnore(tenantLine = "true")
    Office getOffice(@Param("officeId")String officeId);
}
