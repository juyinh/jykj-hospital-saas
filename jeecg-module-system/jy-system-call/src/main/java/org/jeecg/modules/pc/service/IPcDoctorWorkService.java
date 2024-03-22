package org.jeecg.modules.pc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.pc.dto.DoctorWorkAddDTO;
import org.jeecg.modules.pc.dto.DoctorWorkPageDTO;
import org.jeecg.modules.pc.dto.DoctorWorkUpdateDTO;
import org.jeecg.modules.pc.vo.DoctorWorkPageVO;
import org.jeecg.modules.pc.vo.DoctorWorkQueryVO;
import org.springframework.scheduling.annotation.Async;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
public interface IPcDoctorWorkService extends IService<DoctorWork> {
    /*
     *@Description: 医生排班信息
     *@Param: [workPageDTO, pageNo, pageSize]
     *@Return: org.jeecg.common.api.vo.Result<com.baomidou.mybatisplus.core.metadata.IPage<org.jeecg.modules.pc.vo.DoctorWorkPageVO>>
     *@author: xiaopeng.wu
     *@DateTime: 10:19 2023/12/11
    **/
    IPage<DoctorWorkPageVO> queryDoctorWorkPage(DoctorWorkPageDTO workPageDTO, Integer pageNo, Integer pageSize);

    /*
     *@Description: 查询值班详情
     *@Param: [id]
     *@Return: org.jeecg.common.api.vo.Result<org.jeecg.modules.pc.vo.DoctorWorkQueryVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:18 2023/12/13
    **/
    DoctorWorkQueryVO queryDoctorWorkById(DoctorWorkQueryVO doctorWorkQueryVO);

    /*
     *@Description: 医院排班预约
     *@Param: [doctorWork]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 16:29 2023/11/29
    **/
    void addDoctorWork(DoctorWorkAddDTO DoctorWorkAddDTO);

    /*
     *@Description: 医院排班修改
     *@Param: [doctorWork]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 16:29 2023/11/29
     **/
    void editDoctorWork(DoctorWorkUpdateDTO updateDTO);

    /*
     *@Description: 编辑工作状态
     *@Param: [updateDTO]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 10:04 2024/2/27
    **/
    @Async
    void editDoctorWorkStatus(String workDetailId, long finishCount);

    /*
     *@Description: 删除排班信息
     *@Param: [id]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 10:02 2023/12/13
    **/
    void deleteDoctorWork(DoctorWork doctorWork);
}
