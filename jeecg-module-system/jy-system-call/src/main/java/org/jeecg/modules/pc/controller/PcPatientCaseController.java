package org.jeecg.modules.pc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.common.entity.PatientCase;
import org.jeecg.modules.common.service.IPatientCaseService;
import org.jeecg.modules.pc.dto.ClinicHistoryPageDTO;
import org.jeecg.modules.pc.service.IPcPatientCaseService;
import org.jeecg.modules.pc.vo.ClinicHistoryPageVO;
import org.jeecg.modules.pc.vo.PcPatientCaseDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 *@Description: 用户就诊病例信息管理
 *@Param: 
 *@Return: 
 *@author: xiaopeng.wu
 *@DateTime: 15:37 2023/12/5
**/
@Api(tags="医院-就诊病例管理")
@RestController
@RequestMapping("/pc/patientCase")
@Slf4j
public class PcPatientCaseController extends JeecgController<PatientCase, IPatientCaseService> {
   @Autowired
   private IPcPatientCaseService pcPatientCaseService;

   @ApiOperation(value="历史就诊病例信息-列表信息", notes="历史就诊病例信息-列表信息")
   @GetMapping(value = "/queryHistoryCase")
   public Result<IPage<ClinicHistoryPageVO>> queryHistoryCase(ClinicHistoryPageDTO pageDTO,
                                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
       IPage<ClinicHistoryPageVO>  page = pcPatientCaseService.queryHistoryCase(pageDTO, pageNo, pageSize);
       return Result.OK(page);
   }

    @ApiOperation(value="查询病历详情-通过id查询", notes="查询病历详情-通过id查询")
    @GetMapping(value = "/queryDetailById")
    public Result<PcPatientCaseDetailVO> queryDetailById(@RequestParam(name="caseId",required=true) String caseId) {
        PcPatientCaseDetailVO patientCase = pcPatientCaseService.queryDetailById(caseId);
        if(patientCase==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(patientCase);
    }
}
