package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.mo.vo.MoDoctorVO;

import java.util.List;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "true")
public interface MoDoctorMapper extends BaseMapper<Doctor> {
    /*
     *@Description: 查询医生信息
     *@Param: [tenantId, officeId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoDoctorVO>
     *@author: xiaopeng.wu
     *@DateTime: 15:45 2023/12/28
    **/
    List<MoDoctorVO> getDoctorList(@Param("tenantId") String tenantId, @Param("officeId")String officeId);
    
    /*
     *@Description: 查询当天有排班医生信息
     *@Param: [tenantId, officeId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoDoctorVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:57 2024/2/28
    **/
    List<MoDoctorVO> queryTodayDoctorWorkList(@Param("tenantId") String tenantId, @Param("officeId")String officeId);
}
