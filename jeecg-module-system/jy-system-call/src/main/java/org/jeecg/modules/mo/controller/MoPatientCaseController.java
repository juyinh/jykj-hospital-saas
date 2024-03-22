package org.jeecg.modules.mo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.common.service.IPatientCaseService;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.service.IMoPatientCaseService;
import org.jeecg.modules.mo.vo.MoPatientCaseDetailVO;
import org.jeecg.modules.mo.vo.MoPatientClinicCasePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "C端-患者病例")
@RestController
@RequestMapping("/mo/patientCase")
@Slf4j
public class MoPatientCaseController extends JeecgController<PatientCase, IPatientCaseService> {
    @Autowired
    private IMoPatientCaseService moPatientCaseService;

    @ApiOperation(value = "患者就诊记录-分页", notes = "患者就诊记录-分页")
    @GetMapping(value = "/queryClinicPatientCasePage")
    public Result<IPage<MoPatientClinicCasePageVO>> queryClinicPatientCasePage(@RequestHeader("openId") String openId,
                                                                               MoClinicAppointQueryDTO queryDTO,
                                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<MoPatientClinicCasePageVO> page = moPatientCaseService.queryClinicPatientCasePage(openId, queryDTO, pageNo, pageSize);
        return Result.OK(page);
    }


    @ApiOperation(value = "病例详情-通过id查询", notes = "病例详情-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MoPatientCaseDetailVO> queryById(@RequestParam(name = "caseId", required = true) String caseId) {
        MoPatientCaseDetailVO caseDetailVO = moPatientCaseService.getPatientCaseDetail(caseId);
        if (caseDetailVO == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(caseDetailVO);
    }

    @ApiOperation(value = "病例记录删除", notes = "病例记录删除")
    @DeleteMapping(value = "/deleteById")
    public Result<MoPatientCaseDetailVO> deleteById(@RequestParam(name = "caseId", required = true) String caseId) {
        PatientCase patientCase = new PatientCase();
        patientCase.setId(caseId);
        patientCase.setDelFlag(1);
        moPatientCaseService.updateById(patientCase);
        return Result.OK("删除成功");
    }
}
