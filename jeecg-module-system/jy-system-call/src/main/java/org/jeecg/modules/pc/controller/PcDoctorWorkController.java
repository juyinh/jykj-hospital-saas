package org.jeecg.modules.pc.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.DateUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.common.service.IDoctorWorkService;
import org.jeecg.modules.pc.dto.*;
import org.jeecg.modules.pc.service.IPcDoctorWorkService;
import org.jeecg.modules.pc.vo.DoctorWorkPageVO;
import org.jeecg.modules.pc.vo.DoctorWorkQueryVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;

/**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "医院-医生排班管理")
@RestController
@RequestMapping("/pc/doctorWork")
@Slf4j
@Validated
public class PcDoctorWorkController extends JeecgController<DoctorWork, IDoctorWorkService> {
    @Resource
    private IDoctorWorkService doctorWorkService;
    @Resource
    private IPcDoctorWorkService pcDoctorWorkService;

    @ApiOperation(value = "医生排班信息-分页列表查询", notes = "医生排班信息-分页列表查询")
    @GetMapping(value = "/queryDoctorWorkPage")
    public Result<IPage<DoctorWorkPageVO>> queryDoctorWorkPage(DoctorWorkPageDTO workPageDTO,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (MybatisPlusSaasConfig.getOpenSystemTenantControl()) {
            //管理员不需要租户隔离
            workPageDTO.setTenantId(Integer.valueOf(TenantContext.getTenant()));
        }
        IPage<DoctorWorkPageVO> pageList = pcDoctorWorkService.queryDoctorWorkPage(workPageDTO, pageNo, pageSize);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "医生排班-通过id查询", notes = "医生排班-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<DoctorWorkQueryVO> queryDoctorWorkById(@RequestParam(name = "id", required = true) String id) {
        DoctorWorkQueryVO doctorWorkQueryVO = BeanUtil.copyProperties(doctorWorkService.getById(id), DoctorWorkQueryVO.class);
        if (doctorWorkQueryVO == null) {
            return Result.error("未找到对应数据");
        }
        doctorWorkQueryVO = pcDoctorWorkService.queryDoctorWorkById(doctorWorkQueryVO);
        return Result.OK(doctorWorkQueryVO);
    }


    @ApiOperation(value = "医生排班-添加", notes = "医生排班-添加")
    //@RequiresPermissions("common:his_doctor_work:add")
    @PostMapping(value = "/addDoctorWork")
    public Result<String> addDoctorWork(@RequestBody @Valid DoctorWorkAddDTO workAddDTO) throws ParseException {
        addDoctorWorkCheck(workAddDTO);
        pcDoctorWorkService.addDoctorWork(workAddDTO);
        return Result.OK("添加成功！");
    }

    private void addDoctorWorkCheck(DoctorWorkAddDTO workAddDTO) throws ParseException {
        if (workAddDTO.getAppointTime() != null ) {
            if (workAddDTO.getAppointTime() .getTime() < new Date().getTime()) {
                throw new RuntimeException("放号时间不能小于当前时间！");
            }
        }
        if (workAddDTO.getAppointType().equals(2)) {
            if (workAddDTO.getAppointTime() == null) {
                throw new RuntimeException("放号时间不能为空");
            }
        } else {
            if (workAddDTO.getAppointTime() == null) {
                workAddDTO.setAppointTime(new Date());
            }
        }
        for (DoctorWorkDetailAddDTO detailAddDTO : workAddDTO.getWorkDetailAddDTOList()) {
            detailAddDTO.setBeginTimeClinic(DateUtils.appendDate(workAddDTO.getWorkDay(), detailAddDTO.getBeginTimeClinic()));
            detailAddDTO.setEndTimeClinic(DateUtils.appendDate(workAddDTO.getWorkDay(), detailAddDTO.getEndTimeClinic()));
            if (detailAddDTO.getBeginTimeClinic().getTime() > detailAddDTO.getEndTimeClinic().getTime()) {
                throw new RuntimeException("开始坐诊时间不能大于结束坐诊时间");
            }else if ((detailAddDTO.getBeginTimeClinic().compareTo(new Date()) == -1)
                    || (detailAddDTO.getEndTimeClinic().compareTo(new Date()) == -1)) {
                throw new RuntimeException("开始或结束坐诊时间不能小于当前时间");
            } else if (detailAddDTO.getBeginTimeClinic().getTime() < workAddDTO.getAppointTime().getTime()) {
                throw new RuntimeException("开始放号时间不能大于开始坐诊时间");
            }
        }
    }

    @ApiOperation(value = "医生排班信息-编辑", notes = "医生排班信息-编辑")
    //@RequiresPermissions("common:his_doctor_work:edit")
    @PutMapping(value = "/edit")
    public Result<String> editDoctorWork(@RequestBody @Validated DoctorWorkUpdateDTO updateDTO) throws ParseException {
        DoctorWork doctorWork = doctorWorkService.getById(updateDTO.getId());
        if (doctorWork == null) {
            throw new RuntimeException("编辑数据不存在！");
        }
        if (updateDTO.getAppointTime() != null ) {
            if (updateDTO.getAppointTime() .getTime() < new Date().getTime()) {
                throw new RuntimeException("预约时间不能小于当前时间！");
            }
        }
        if (doctorWork.getAppointStatus() != 0 || doctorWork.getAppointStatus() !=0) {
            throw new RuntimeException("已预约或者出诊中数据不可以编辑");
        }
        if (updateDTO.getAppointType().equals(2)) {
            if (updateDTO.getAppointTime() == null) {
                throw new RuntimeException("开始预约不能为空");
            }
        } else {
            if (updateDTO.getAppointTime() == null) {
                updateDTO.setAppointTime(new Date());
            }
        }
        for (DoctorWorkDetailUpdateDTO detailAddDTO : updateDTO.getWorkDetailUpdateDTOList()) {
            detailAddDTO.setBeginTimeClinic(DateUtils.appendDate(updateDTO.getWorkDay(), detailAddDTO.getBeginTimeClinic()));
            detailAddDTO.setEndTimeClinic(DateUtils.appendDate(updateDTO.getWorkDay(), detailAddDTO.getEndTimeClinic()));
            if (detailAddDTO.getEndTimeClinic().getTime() < detailAddDTO.getBeginTimeClinic().getTime()) {
                throw new RuntimeException("出诊开始时间不能小于出诊结束时间");
            }
            if (detailAddDTO.getBeginTimeClinic().getTime() < updateDTO.getAppointTime().getTime()) {
                throw new RuntimeException("预约时间不能大于出诊开始时间");
            }
        }
        pcDoctorWorkService.editDoctorWork(updateDTO);
        return Result.OK("编辑成功!");
    }

    @ApiOperation(value = "医生排班-通过id删除", notes = "医生排班-通过id删除")
    //@RequiresPermissions("common:his_doctor_work:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> deleteDoctorWork(@RequestParam(name = "id", required = true) String id) {
        DoctorWork doctorWork = doctorWorkService.getById(id);
        if (doctorWork.getAppointStatus() != 0 || doctorWork.getAppointStatus() !=0) {
            throw new RuntimeException("预约或者出诊中数据不可以删除");
        }
        pcDoctorWorkService.deleteDoctorWork(doctorWork);
        return Result.OK("删除成功!");
    }

}
