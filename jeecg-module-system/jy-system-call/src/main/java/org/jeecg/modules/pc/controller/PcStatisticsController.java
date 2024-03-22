package org.jeecg.modules.pc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pc.service.IPcStatisticsService;
import org.jeecg.modules.pc.vo.PatientAgeSexVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags="首页-统计管理")
@RestController
@RequestMapping("/pc/statistics")
@Slf4j
public class PcStatisticsController{
    @Resource
    private IPcStatisticsService iPcStatisticsService;

   @ApiOperation(value="查询最近一周预约就诊人数", notes="查询最近一周预约就诊人数")
   @GetMapping(value = "/queryPatientWeekSum")
   public Result queryPatientWeekSum() {
       List<Map<String, Object>> list = iPcStatisticsService.queryPatientWeekSum();
       return Result.OK(list);
   }

   @ApiOperation(value="患者年龄分布", notes="患者年龄分布")
   @GetMapping(value = "/queryPatientAgeData")
   @ApiImplicitParam(name = "period", value = "统计时段（1：一周，2：一月，3：一年）", paramType = "query", dataType = "int", required = true)
   public Result<PatientAgeSexVO> queryPatientAgeData(@RequestParam("period") Integer period) {
       PatientAgeSexVO patientAgeSexVO = iPcStatisticsService.queryPatientAgeData(period);
       return Result.OK(patientAgeSexVO);
   }

   @ApiOperation(value="患者性别分布", notes="患者性别分布")
   @GetMapping(value = "/queryPatientSexData")
   @ApiImplicitParam(name = "period", value = "统计时段（1：一周，2：一月，3：一年）", paramType = "query", dataType = "int", required = true)
   public Result queryAgeAndSexData(@RequestParam("period") Integer period) {
       Map<String, Object> map = iPcStatisticsService.queryPatientSexData(period);
       return Result.OK(map);
   }
}
