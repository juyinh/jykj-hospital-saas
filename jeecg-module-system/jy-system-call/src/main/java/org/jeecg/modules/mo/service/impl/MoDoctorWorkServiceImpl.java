package org.jeecg.modules.mo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.mo.mapper.MoDoctorWorkMapper;
import org.jeecg.modules.mo.service.IMoDoctorWorkService;
import org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date:   2023-12-13
 * @Version: V1.0
 */
@Service
public class MoDoctorWorkServiceImpl extends ServiceImpl<MoDoctorWorkMapper, DoctorWork> implements IMoDoctorWorkService {
    @Resource
    private MoDoctorWorkMapper moDoctorWorkMapper;

    @Override
    public List<MoDoctorWorkDetailListVO> getDoctorWorkDetailList(String workDay, String doctorId){
        List<MoDoctorWorkDetailListVO>  doctorWorkDetailListVOList = moDoctorWorkMapper.getDoctorWorkDetailList(workDay, doctorId);
        return doctorWorkDetailListVOList;
    }
}
