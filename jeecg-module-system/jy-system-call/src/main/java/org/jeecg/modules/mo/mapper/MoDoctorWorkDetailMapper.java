package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.common.entity.DoctorWorkDetail;

/**
 * @Description: 排班详情表
 * @Author: jeecg-boot
 * @Date:   2023-11-30
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoDoctorWorkDetailMapper extends BaseMapper<DoctorWorkDetail> {

}
