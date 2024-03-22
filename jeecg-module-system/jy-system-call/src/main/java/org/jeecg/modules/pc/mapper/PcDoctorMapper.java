package org.jeecg.modules.pc.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.pc.dto.DoctorQueryPageDTO;
import org.jeecg.modules.pc.vo.DoctorQueryPageVO;

import java.util.List;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface PcDoctorMapper extends BaseMapper<Doctor> {
    /**
     * 查询医生
     * @param userId
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    Doctor getDoctor(@Param("userId") String userId);

    /*
     *@Description:查询医生信息分页
     *@Param: [doctorQueryPageDTO]
     *@Return: com.baomidou.mybatisplus.core.metadata.IPage
     *@author: xiaopeng.wu
     *@DateTime: 14:49 2023/12/8
    **/
    List<DoctorQueryPageVO> getDoctorList(@Param("dto")DoctorQueryPageDTO dto);
}
