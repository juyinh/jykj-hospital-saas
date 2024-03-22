package org.jeecg.modules.pc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.pc.dto.DoctorAddDTO;
import org.jeecg.modules.pc.dto.DoctorQueryPageDTO;
import org.jeecg.modules.pc.dto.DoctorUpdateDTO;
import org.jeecg.modules.pc.vo.DoctorQueryPageVO;
import org.jeecg.modules.pc.vo.DoctorUserDetailVO;

import java.util.List;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
public interface IPcDoctorService extends IService<Doctor> {
    /*
     *@Description: 查询医生详情
     *@Param: [id]
     *@Return: org.jeecg.modules.pc.vo.DoctorUserDetail
     *@author: xiaopeng.wu
     *@DateTime: 9:26 2023/12/11
    **/
    DoctorUserDetailVO queryDoctorUserById(String id);
    IPage<DoctorQueryPageVO> queryPageList(DoctorQueryPageDTO queryPageDTO, Integer pageNo, Integer pageSize);

    List<DoctorQueryPageVO> queryList(DoctorQueryPageDTO queryPageDTO);
    /*
     *@Description: 添加医生信息
     *@Param: [addDTO]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 13:57 2023/11/30
    **/
    void addDoctor(DoctorAddDTO addDTO);

    /*
     *@Description: 编辑医生id
     *@Param: [updateDTO]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 15:29 2023/12/11
    **/
    void editDoctor(DoctorUpdateDTO updateDTO);
}
