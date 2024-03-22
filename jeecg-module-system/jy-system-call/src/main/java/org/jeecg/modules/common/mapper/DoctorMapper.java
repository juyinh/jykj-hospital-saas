package org.jeecg.modules.common.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.Doctor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pc.dto.DoctorQueryPageDTO;
import org.jeecg.modules.pc.vo.DoctorQueryPageVO;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface DoctorMapper extends BaseMapper<Doctor> {

}
