package org.jeecg.modules.pc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.entity.ClinicRoom;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.common.entity.Office;
import org.jeecg.modules.common.mapper.DoctorMapper;
import org.jeecg.modules.common.service.IClinicRoomService;
import org.jeecg.modules.common.service.IOfficeService;
import org.jeecg.modules.pc.dto.DoctorAddDTO;
import org.jeecg.modules.pc.dto.DoctorQueryPageDTO;
import org.jeecg.modules.pc.dto.DoctorUpdateDTO;
import org.jeecg.modules.pc.mapper.PcClinicRoomMapper;
import org.jeecg.modules.pc.mapper.PcDoctorMapper;
import org.jeecg.modules.pc.mapper.PcOfficeMapper;
import org.jeecg.modules.pc.service.IPcDoctorService;
import org.jeecg.modules.pc.vo.DoctorQueryPageVO;
import org.jeecg.modules.pc.vo.DoctorUserDetailVO;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: his_doctor
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Service
public class PcDoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IPcDoctorService {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private BaseCommonService baseCommonService;
    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private PcDoctorMapper pcDoctorMapper;
    @Resource
    private IOfficeService officeService;
    @Resource
    private IClinicRoomService iClinicRoomService;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private ISysUserService userService;
    @Resource
    private PcOfficeMapper pcOfficeMapper;
    @Resource
    private PcClinicRoomMapper pcClinicRoomMapper;

    @Override
    public DoctorUserDetailVO queryDoctorUserById(String id) {
        SysUser sysUser = sysUserService.getById(id);
        DoctorUserDetailVO userDetail = BeanUtil.copyProperties(sysUser, DoctorUserDetailVO.class);
        if (userDetail == null) {
            throw new RuntimeException("未找到对应数据");
        }
        Doctor doctor = pcDoctorMapper.getDoctor(sysUser.getId());
        if (doctor != null) {
            userDetail.setDoctorId(doctor.getId());
            userDetail.setRegFee(doctor.getRegFee());
            userDetail.setCareer(doctor.getCareer());
            userDetail.setOfficeId(doctor.getOfficeId());
            userDetail.setIntroduction(doctor.getIntroduction());
            Office office = pcOfficeMapper.getOffice(doctor.getOfficeId());
            if (office != null) {
                userDetail.setOfficeName(office.getOfficeName());
            }
            ClinicRoom clinicRoom = pcClinicRoomMapper.getClinicRoom(doctor.getClinicRoomId());
            if (clinicRoom != null) {
                userDetail.setClinicRoomId(doctor.getClinicRoomId());
                userDetail.setClinicRoomName(clinicRoom.getClinicRoomName());
            }
        }
        Map<String, Object> roleInfo = sysUserRoleMapper.getRoleInfoByUserName(userDetail.getUsername()).get(0);
        userDetail.setRoleId(MapUtil.getStr(roleInfo, "id"));
        userDetail.setRoleName(MapUtil.getStr(roleInfo, "role_name"));
        return userDetail;
    }

    @Override
    public IPage<DoctorQueryPageVO> queryPageList(DoctorQueryPageDTO queryPageDTO, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<DoctorQueryPageVO> queryPageVOList = pcDoctorMapper.getDoctorList(queryPageDTO);
        return oConvertUtils.pageInfoToIPage(new PageInfo<>(queryPageVOList));
    }

    @Override
    public List<DoctorQueryPageVO> queryList(DoctorQueryPageDTO queryPageDTO) {
        return pcDoctorMapper.getDoctorList(queryPageDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDoctor(DoctorAddDTO addDTO) {
        //默认系统部门
        String selectedDeparts = "1";
        //默认密码
        String password = "123456";
        //获取租户信息
        String relTenantIds = TenantContext.getTenant();
        SysUser user = BeanUtil.copyProperties(addDTO, SysUser.class);
        user.setCreateTime(new Date());
        //随机盐值
        String salt = oConvertUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getPhone(), password, salt);
        user.setPassword(passwordEncode);
        user.setStatus(1);
        user.setDelFlag(CommonConstant.DEL_FLAG_0);
        //用户表字段org_code不能在这里设置他的值
        user.setOrgCode(null);
        //账户采用电话
        user.setUsername(addDTO.getPhone());
        user.setTenantId(TenantContext.getTenant());
        user.setUserIdentity(1);
        // 保存用户走一个service 保证事务
        sysUserService.saveUser(user, addDTO.getRoleId(), selectedDeparts, relTenantIds);
        Doctor doctor = BeanUtil.copyProperties(addDTO, Doctor.class);
        doctor.setUserId(user.getId());
        doctor.setDoctorName(addDTO.getRealname());
        //保存医生信息
        doctorMapper.insert(doctor);
        baseCommonService.addLog("添加用户，username： " + user.getUsername(), CommonConstant.LOG_TYPE_2, 2);
    }

    @Override
    public void editDoctor(DoctorUpdateDTO updateDTO) {
        SysUser sysUser = BeanUtil.copyProperties(updateDTO, SysUser.class);
        sysUser.setId(updateDTO.getUserId());
        userService.updateById(sysUser);

        Doctor doctor = BeanUtil.copyProperties(updateDTO, Doctor.class);
        doctor.setDoctorName(updateDTO.getRealname());
        doctorMapper.update(doctor, new UpdateWrapper<Doctor>().lambda().eq(Doctor::getUserId, updateDTO.getUserId()));

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(updateDTO.getRoleId());
        sysUserRoleMapper.update(sysUserRole, new UpdateWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, updateDTO.getUserId()));
    }
}
