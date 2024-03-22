package org.jeecg.modules.mo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.mo.vo.MoDoctorVO;
import org.jeecg.modules.pc.vo.DoctorUserDetailVO;

import java.util.List;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
public interface IMoDoctorService extends IService<Doctor> {
    /*
     *@Description: 查询医生详情信息
     *@Param: [id]
     *@Return: org.jeecg.modules.pc.vo.DoctorUserDetailVO
     *@author: xiaopeng.wu
     *@DateTime: 9:57 2024/1/8
    **/
    DoctorUserDetailVO queryDoctorUserById(String doctorId);
    
    /*
     *@Description: 查询医生信息
     *@Param: [tenantId, officeId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoDoctorVO>
     *@author: xiaopeng.wu
     *@DateTime: 15:49 2023/12/28
    **/
    List<MoDoctorVO> getDoctorList(String tenantId, String officeId);

    /*
     *@Description: 查询当天有排班医生
     *@Param: [tenantId, officeId]
     *@Return: java.util.List<org.jeecg.modules.mo.vo.MoDoctorVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:50 2024/2/28
    **/
    List<MoDoctorVO> queryTodayDoctorWorkList(String tenantId, String officeId);
}
