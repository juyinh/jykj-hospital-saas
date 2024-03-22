package org.jeecg.modules.pc.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.common.service.IClinicAppointService;
import org.jeecg.modules.common.service.IPatientCaseService;
import org.jeecg.modules.pc.dto.ClinicAppointHistoryPageDTO;
import org.jeecg.modules.pc.dto.ClinicAppointTodayPageDTO;
import org.jeecg.modules.pc.dto.PatientCaseUpdateDTO;
import org.jeecg.modules.pc.dto.PatientClinicEditDTO;
import org.jeecg.modules.pc.service.IPcClinicAppointService;
import org.jeecg.modules.pc.vo.ClinicAppointDetailVO;
import org.jeecg.modules.pc.vo.ClinicAppointHistoryPageVO;
import org.jeecg.modules.pc.vo.ClinicAppointPageVO;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "医院-出诊预约管理")
@RestController
@RequestMapping("/pc/clinicAppoint")
@Slf4j
@Validate
public class PcClinicAppointController extends JeecgController<ClinicAppoint, IClinicAppointService> {
    @Resource
    private IPcClinicAppointService iPcClinicAppointService;
    @Resource
    private IPatientCaseService patientCaseService;


    @ApiOperation(value = "出诊信息表-分页查询当日列表", notes = "出诊信息表-分页查询当日列表，按排队或预约号排序")
    @GetMapping(value = "/queryTodayPageList")
    public Result<IPage<ClinicAppointPageVO>> queryTodayPageList(ClinicAppointTodayPageDTO todayPage,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws ParseException {
        Result<IPage<ClinicAppointPageVO>> result = iPcClinicAppointService.queryTodayPageList(todayPage, pageNo, pageSize);
        return result;
    }

    @ApiOperation(value = "用户预约就诊历史记录-分页", notes = "用户预约就诊历史记录-分页")
    @GetMapping(value = "/queryAppointHistoryPage")
    public Result<IPage<ClinicAppointHistoryPageVO>> queryAppointHistoryPage(ClinicAppointHistoryPageDTO pageDTO,
                                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<ClinicAppointHistoryPageVO> pageList = iPcClinicAppointService.queryAppointHistoryPage(pageDTO, pageNo, pageSize);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "预约详情", notes = "获取预约详情")
    @GetMapping(value = "/queryPatientDetail")
    public Result<ClinicAppointDetailVO> queryPatientDetail(@RequestParam("appointId") String appointId){
        ClinicAppointDetailVO result = iPcClinicAppointService.queryPatientDetail(appointId);
        return Result.OK(result);
    }

    @ApiOperation(value = "开始就诊详情", notes = "获取开始就诊详情-改变就诊状态（就诊中）")
    @GetMapping(value = "/queryPatientClinicDetail")
    public Result<ClinicAppointDetailVO> queryPatientClinicDetail(@RequestParam("appointId") String appointId) throws ParseException {
        ClinicAppointDetailVO result = iPcClinicAppointService.queryPatientClinicDetail(appointId);
        return Result.OK(result);
    }

    @ApiOperation(value = "保存就诊（获取下一位）", notes = "保存就诊（获取下一位）-开始就诊页面")
    @PutMapping(value = "/editPatientClinicDetail")
    public Result<String> editPatientClinicDetail(@Validate @RequestBody PatientClinicEditDTO clinicEditDTO) throws ParseException {
        String result = iPcClinicAppointService.editPatientClinicDetail(clinicEditDTO);
        return Result.OK("保存成功", result);
    }

    @ApiOperation(value = "编辑病例", notes = "编辑病例-已就诊后病例")
    @PutMapping(value = "/editPatientCase")
    public Result editPatientCase(@Validate @RequestBody PatientCaseUpdateDTO patientCaseUpdateDTO){
        PatientCase patientCase = BeanUtil.toBean(patientCaseUpdateDTO, PatientCase.class);
        patientCase.setId(patientCaseUpdateDTO.getPatientCaseId());
        patientCaseService.updateById(patientCase);
        return Result.OK("保存成功");
    }


    @ApiOperation(value = "爽约", notes = "爽约")
    @PutMapping(value = "/cancelAppoint")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appointId", value = "预约Id", paramType = "query", dataType = "String", required = true)
    })
    public Result<String> cancelAppoint(@RequestBody JSONObject jsonObject) {
        String nextAppointId = iPcClinicAppointService.cancelAppoint(jsonObject.getString("appointId"));
        return Result.OK("爽约成功", nextAppointId);
    }

    @ApiOperation(value = "复约", notes = "复约（爽约后在约，在大于三个人时，自动往后排三个号）")
    @PutMapping(value = "/againAppoint")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appointId", value = "预约Id", paramType = "query", dataType = "String", required = true)
    })
    public Result<String> againAppoint(@RequestBody JSONObject jsonObject) {
        iPcClinicAppointService.againAppoint(jsonObject.getString("appointId"));
        return Result.OK("复约成功!");
    }
}
