package org.jeecg.modules.mo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO;

import java.util.List;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date:   2023-12-13
 * @Version: V1.0
 */
public interface IMoDoctorWorkService extends IService<DoctorWork> {
    /*
     *@Description: 获取预约详情
     *@Param: [workDay, doctorId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:40 2024/1/8
    **/
    List<MoDoctorWorkDetailListVO> getDoctorWorkDetailList(String workDay, String doctorId);
}
