package org.jeecg.modules.mo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.common.entity.ClinicRoom;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.common.entity.DoctorWorkDetail;
import org.jeecg.modules.common.entity.Office;
import org.jeecg.modules.mo.mapper.MoClinicRoomMapper;
import org.jeecg.modules.mo.mapper.MoDoctorMapper;
import org.jeecg.modules.mo.mapper.MoDoctorWorkDetailMapper;
import org.jeecg.modules.mo.mapper.MoOfficeMapper;
import org.jeecg.modules.mo.service.IMoDoctorService;
import org.jeecg.modules.mo.vo.MoDoctorVO;
import org.jeecg.modules.pc.vo.DoctorUserDetailVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date: 2023-12-01
 * @Version: V1.0
 */
@Service
public class MoDoctorServiceImpl extends ServiceImpl<MoDoctorMapper, Doctor> implements IMoDoctorService {
    @Resource
    private MoDoctorMapper moDoctorMapper;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private MoOfficeMapper moOfficeMapper;
    @Resource
    private MoClinicRoomMapper moClinicRoomMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private MoDoctorWorkDetailMapper moDoctorWorkDetailMapper;


    @Override
    public DoctorUserDetailVO queryDoctorUserById(String doctorId) {
        Doctor doctor = moDoctorMapper.selectById(doctorId);
        if (doctor == null) {
            throw new RuntimeException("未找到对应数据");
        }
        SysUser sysUser = sysUserService.getById(doctor.getUserId());
        DoctorUserDetailVO userDetail = BeanUtil.copyProperties(sysUser, DoctorUserDetailVO.class);
        if (doctor != null) {
            userDetail.setDoctorId(doctor.getId());
            userDetail.setRegFee(doctor.getRegFee());
            userDetail.setCareer(doctor.getCareer());
            userDetail.setOfficeId(doctor.getOfficeId());
            userDetail.setIntroduction(doctor.getIntroduction());
            Office office = moOfficeMapper.selectById(doctor.getOfficeId());
            if (office != null) {
                userDetail.setOfficeName(office.getOfficeName());
            }
            ClinicRoom clinicRoom = moClinicRoomMapper.selectById(doctor.getClinicRoomId());
            if (clinicRoom != null) {
                userDetail.setClinicRoomId(doctor.getClinicRoomId());
                userDetail.setClinicRoomName(clinicRoom.getClinicRoomName());
            }
        }
        List<Map<String, Object>> roleInfoList = sysUserRoleMapper.getRoleInfoByUserName(userDetail.getUsername());
        if (roleInfoList.size() > 0) {
            Map<String, Object> roleInfo = roleInfoList.get(0);
            userDetail.setRoleId(MapUtil.getStr(roleInfo, "id"));
            userDetail.setRoleName(MapUtil.getStr(roleInfo, "role_name"));
        }
        return userDetail;
    }

    @Override
    public List<MoDoctorVO> getDoctorList(String tenantId, String officeId) {
        return moDoctorMapper.getDoctorList(tenantId, officeId);
    }

    @Override
    public List<MoDoctorVO> queryTodayDoctorWorkList(String tenantId, String officeId) {
        List<MoDoctorVO> resultsList = new ArrayList<>();
        List<MoDoctorVO> moDoctorVOList = moDoctorMapper.queryTodayDoctorWorkList(tenantId, officeId);
        for (MoDoctorVO moDoctorVO : moDoctorVOList) {
            List<DoctorWorkDetail> doctorWorkDetailList = moDoctorWorkDetailMapper.selectList(new LambdaQueryWrapper<DoctorWorkDetail>()
                    .eq(DoctorWorkDetail::getDoctorWordId, moDoctorVO.getDoctorWorkId()));
            Boolean addFlag = false;
            for (DoctorWorkDetail doctorWorkDetail : doctorWorkDetailList) {
                if (doctorWorkDetail.getEndTimeClinic().compareTo(new Date()) >= 0) {//结束时间大于当前时间才能排队
                    addFlag = true;
                }
            }
            if (addFlag){
                resultsList.add(moDoctorVO);
            }
        }
        return resultsList;
    }
}
