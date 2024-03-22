package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO;

import java.util.List;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date: 2023-11-30
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoDoctorWorkMapper extends BaseMapper<DoctorWork> {
    /*
     *@Description: 获取预约详情List
     *@Param: [workDay, doctorId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:38 2024/1/8
    **/
    List<MoDoctorWorkDetailListVO> getDoctorWorkDetailList(@Param("workDay") String workDay,
                                                           @Param("doctorId") String doctorId);
}
