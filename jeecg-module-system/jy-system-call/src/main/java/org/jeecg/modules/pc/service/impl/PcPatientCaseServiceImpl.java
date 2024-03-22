package org.jeecg.modules.pc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.common.mapper.PatientCaseMapper;
import org.jeecg.modules.pc.dto.ClinicHistoryPageDTO;
import org.jeecg.modules.pc.mapper.PcPatientCaseMapper;
import org.jeecg.modules.pc.service.IPcDoctorService;
import org.jeecg.modules.pc.service.IPcPatientCaseService;
import org.jeecg.modules.pc.vo.ClinicHistoryPageVO;
import org.jeecg.modules.pc.vo.PcPatientCaseDetailVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Service
public class PcPatientCaseServiceImpl extends ServiceImpl<PatientCaseMapper, PatientCase> implements IPcPatientCaseService {
    @Resource
    private PcPatientCaseMapper pcPatientCaseMapper;
    @Resource
    private IPcDoctorService doctorService;

    @Override
    public IPage<ClinicHistoryPageVO> queryHistoryCase(ClinicHistoryPageDTO pageDTO, Integer pageNo, Integer pageSize) {
        String doctorId = null;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser.getUserIdentity().equals(1)) {
            Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>().eq(Doctor::getUserId, sysUser.getId()));
            doctorId = doctor.getId();
        }
        PageHelper.startPage(pageNo, pageSize);
        List<ClinicHistoryPageVO> pageVOList = pcPatientCaseMapper.queryHistoryCase(pageDTO, doctorId);
        return oConvertUtils.pageInfoToIPage(new PageInfo<>(pageVOList));
    }

    @Override
    public PcPatientCaseDetailVO queryDetailById(String caseId) {
        return pcPatientCaseMapper.getPatientCaseDetail(caseId);
    }
}
