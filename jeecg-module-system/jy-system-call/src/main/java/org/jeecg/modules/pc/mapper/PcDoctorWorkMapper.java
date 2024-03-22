package org.jeecg.modules.pc.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.pc.dto.DoctorWorkPageDTO;
import org.jeecg.modules.pc.vo.DoctorWorkPageVO;

import java.util.List;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date: 2023-11-30
 * @Version: V1.0
 */
public interface PcDoctorWorkMapper extends BaseMapper<DoctorWork> {
    /*
     *@Description: 获取诊室信息
     *@Param: [appointStatus, realname, officeName, clinicRoomName]
     *@Return: java.util.List<org.jeecg.modules.pc.vo.DoctorWorkPageVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:27 2023/12/11
    **/
    @InterceptorIgnore(tenantLine = "true")
    List<DoctorWorkPageVO> getDoctorWorkList(@Param("dto") DoctorWorkPageDTO workPageDTO);
}
