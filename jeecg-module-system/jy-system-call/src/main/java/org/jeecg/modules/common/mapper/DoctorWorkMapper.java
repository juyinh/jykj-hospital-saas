package org.jeecg.modules.common.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.DoctorWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pc.dto.DoctorWorkPageDTO;
import org.jeecg.modules.pc.vo.DoctorWorkPageVO;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date: 2023-11-30
 * @Version: V1.0
 */
public interface DoctorWorkMapper extends BaseMapper<DoctorWork> {

}
