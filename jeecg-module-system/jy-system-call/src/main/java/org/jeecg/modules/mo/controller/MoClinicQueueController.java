package org.jeecg.modules.mo.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.lock.annotation.JRepeat;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.service.IClinicAppointService;
import org.jeecg.modules.mo.dto.MoClinicAppointQueueAddDTO;
import org.jeecg.modules.mo.service.IMoClinicAppointService;
import org.jeecg.modules.mo.vo.MoQueueUiDetailVO;
import org.jeecg.modules.mo.vo.MoTodayQueueListVO;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "C端-就诊排队")
@RestController
@RequestMapping("/mo/clinicQueue")
@Slf4j
@RequiredArgsConstructor
public class MoClinicQueueController extends JeecgController<ClinicAppoint, IClinicAppointService> {
    private final IMoClinicAppointService moClinicAppointService;
    private final IClinicAppointService clinicAppointService;
    /*每秒控制10个许可，每秒以固定的速率输出令牌，以达到平滑输出的效果*/
    RateLimiter rateLimiter = RateLimiter.create(20.0);

    @ApiOperation(value = "当日排队列表-查询列表", notes = "获取患者当日排队信息")
    @GetMapping(value = "/queryMoTodayQueueList")
    public Result<List<MoTodayQueueListVO>> queryMoTodayQueueList(@RequestHeader(name = "openId", required = true) String openId) {
        List<MoTodayQueueListVO> listVO = moClinicAppointService.queryMoTodayQueueList(openId);
        return Result.OK(listVO);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户Id", paramType = "query", dataType = "String", required = true),
    })
    @ApiOperation(value = "扫码排队", notes = "患者扫码时，跳转到就诊排队页面信息")
    @GetMapping(value = "/queryQueueUiDetail")
    public Result<MoQueueUiDetailVO> queryQueueUiDetail(@RequestParam("tenantId") String tenantId,
                                                        @RequestHeader(name = "openId") String openId) {
        if (!rateLimiter.tryAcquire()) {
            return Result.ok("网络繁忙，请稍微再试");
        }
        MoQueueUiDetailVO detailVO = moClinicAppointService.queryQueueUiDetail(openId, tenantId);
        return Result.OK(detailVO);
    }

    @ApiOperation(value = "就诊排队", notes = "患者扫码排队")
    @PostMapping(value = "/patientAppointQueueMq")
    @JRepeat(lockKey = "#appointQueueAddDTO.cardId", lockTime = 30)//30秒钟之内禁止重复提交
    public Result<String> patientAppointQueueMq(@Validate @RequestBody MoClinicAppointQueueAddDTO appointQueueAddDTO, @RequestHeader("openId") String openId) {
        if (!rateLimiter.tryAcquire()) {
            return Result.ok("网络繁忙，请稍微再试");
        }
        appointQueueAddDTO.setOpenId(openId);
        moClinicAppointService.patientAppointQueueMq(appointQueueAddDTO);
        return Result.OK("添加成功！");
    }

    @ApiOperation(value = "排队取消", notes = "排队取消")
    @PutMapping(value = "/cancelAppointQueue")
    public Result<String> cancelAppointQueue(@RequestBody JSONObject jsonObject) {
        String appointId = jsonObject.getString("appointId");
        ClinicAppoint clinicAppoint = clinicAppointService.getById(appointId);
        if (clinicAppoint.getClinicStatus() != 1) {
            throw new RuntimeException("不需要取消");
        }
        ClinicAppoint clinicAppointUpdate = new ClinicAppoint();
        clinicAppointUpdate.setId(appointId);
        clinicAppointUpdate.setClinicStatus(4);
        clinicAppointService.updateById(clinicAppointUpdate);
        return Result.OK("修改成功！");
    }
}
