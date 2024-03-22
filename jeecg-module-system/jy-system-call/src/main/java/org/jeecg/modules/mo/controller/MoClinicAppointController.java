package org.jeecg.modules.mo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.Repeat;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.common.entity.DoctorWorkDetail;
import org.jeecg.modules.common.service.IClinicAppointService;
import org.jeecg.modules.common.service.IDoctorWorkDetailService;
import org.jeecg.modules.mo.dto.MoClinicAppointAddDTO;
import org.jeecg.modules.mo.dto.MoClinicAppointQueryDTO;
import org.jeecg.modules.mo.service.IMoClinicAppointService;
import org.jeecg.modules.mo.service.IMoDoctorWorkService;
import org.jeecg.modules.mo.vo.MoClinicAppointDetailVO;
import org.jeecg.modules.mo.vo.MoDoctorWorkDayListVO;
import org.jeecg.modules.mo.vo.MoDoctorWorkDetailListVO;
import org.jeecg.modules.mo.vo.MoPatientClinicAppointPageVO;
import org.simpleframework.xml.core.Validate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "C端-就诊预约")
@RestController
@RequestMapping("/mo/clinicAppoint")
@Slf4j
@RequiredArgsConstructor
public class MoClinicAppointController extends JeecgController<ClinicAppoint, IClinicAppointService> {
    private final IMoClinicAppointService moClinicAppointService;
    private final IMoDoctorWorkService doctorWorkService;
    private final IDoctorWorkDetailService doctorWorkDetailService;
    private final RedisTemplate<String, Object> redisTemplate;
    /*每秒控制10个许可，每秒以固定的速率输出令牌，以达到平滑输出的效果*/
    RateLimiter rateLimiter = RateLimiter.create(30.0);

    @ApiOperation(value = "预约记录-分页", notes = "预约记录-分页")
    @GetMapping(value = "/queryPatientAppointPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hospitalName", value = "医院名称", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "beginAppointDay", value = "开始预约日期", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "endAppointDay", value = "结束预约日期", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "clinicStatus", value = "就诊状态（就诊状态 0：预约中，1：历史预约）", paramType = "query", dataType = "String", required = true)
    })
    public Result<IPage<MoPatientClinicAppointPageVO>> queryPatientAppointPage(@RequestHeader(name = "openId", required = true) String openId,
                                                                               MoClinicAppointQueryDTO queryDTO,
                                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                               HttpServletRequest req) {
        IPage<MoPatientClinicAppointPageVO> workDetailListVOPage = moClinicAppointService.queryPatientAppointList(openId, queryDTO, pageNo, pageSize);
        return Result.OK(workDetailListVOPage);
    }

    @ApiOperation(value = "医生值班日期-列表", notes = "医生值班日期-列表")
    @GetMapping(value = "/queryDoctorWorkDayList")
    @ApiImplicitParam(name = "doctorId", value = "医生Id", paramType = "query", dataType = "String", required = true)
    public Result<List<MoDoctorWorkDayListVO>> queryDoctorWorkDayList(@RequestParam(name = "doctorId", required = true) String doctorId) {
        String nowDay = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<DoctorWork> doctorWorkList = doctorWorkService.list(new LambdaQueryWrapper<DoctorWork>()
                .ge(DoctorWork::getWorkDay, nowDay)
                .eq(DoctorWork::getDoctorId, doctorId)
                .le(DoctorWork::getAppointTime, new Date())
                .orderByAsc(DoctorWork::getWorkDay));
        List<MoDoctorWorkDayListVO> result = new ArrayList<>();
        for (DoctorWork doctorWork : doctorWorkList) {
            MoDoctorWorkDayListVO workDayListVO = BeanUtil.copyProperties(doctorWork, MoDoctorWorkDayListVO.class);
            workDayListVO.setWorkDay(DateUtil.format(doctorWork.getWorkDay(), "yyyy-MM-dd"));
            result.add(workDayListVO);
        }
        return Result.OK(result);
    }

    @ApiOperation(value = "预约详情列表", notes = "预约详情列表")
    @GetMapping(value = "/queryAppointDetailList")
    public Result<List<MoDoctorWorkDetailListVO>> queryAppointDetailList(
            @RequestParam(name = "workDay", required = true) String workDay,
            @RequestParam(name = "doctorId", required = true) String doctorId) {
        if (!rateLimiter.tryAcquire()) {
            return Result.ok("网络繁忙，请稍微再试");
        }
        List<MoDoctorWorkDetailListVO> workDetailListVOList = moClinicAppointService.queryAppointDetailList(workDay, doctorId);
        return Result.OK(workDetailListVOList);
    }

    @ApiOperation(value = "获取即将开始预约活动-List", notes = "获取即将开始预约活动-List")
    @GetMapping(value = "/queryActivityList")
    public Result<IPage<ClinicAppoint>> queryActivityList(ClinicAppoint clinicAppoint,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        QueryWrapper<ClinicAppoint> queryWrapper = QueryGenerator.initQueryWrapper(clinicAppoint, req.getParameterMap());
        Page<ClinicAppoint> page = new Page<ClinicAppoint>(pageNo, pageSize);
        IPage<ClinicAppoint> pageList = moClinicAppointService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "预约详情-通过id查询", notes = "预约详情-通过id查询")
    @GetMapping(value = "/queryAppointDetailById")
    public Result<MoClinicAppointDetailVO> queryAppointDetailById(@RequestParam(name = "id", required = true) String id) {
        MoClinicAppointDetailVO detailVO = moClinicAppointService.queryAppointDetailById(id);
        return Result.OK(detailVO);
    }

    @ApiOperation(value = "患者预约", notes = "患者预约，信息提交采用异步方式")
    @PostMapping(value = "/patientAppointMq")
    @Repeat(lockKey = "#appointAddDTO.cardId", lockTime = 30)//30秒钟之内禁止重复提交
    public Result<String> patientAppointMq(@Validate @RequestBody MoClinicAppointAddDTO appointAddDTO, @RequestHeader("openId") String openId) {
        if (!rateLimiter.tryAcquire()) {
            return Result.ok("请稍微再试，请稍微再试");
        }
        String patientAppointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_List + appointAddDTO.getDoctorId() + "_" +
                appointAddDTO.getWorkPeriod() + "_" + appointAddDTO.getAppointDay();
        Boolean isExist = redisTemplate.opsForSet().isMember(patientAppointListKey, appointAddDTO.getCardId());
        if (isExist) {
            return Result.error("预约失败，该时段已预约，请刷新后再试！");
        }
        Date appointTime = DateUtil.parse(appointAddDTO.getAppointDay() + " " + appointAddDTO.getAppointTime(), "yyyy-MM-dd HH:mm");
        if (new Date().compareTo(appointTime) == 1) {
            return Result.error("预约失败，预约时段已过期！");
        }
        appointAddDTO.setOpenId(openId);
        moClinicAppointService.patientAppointMq(appointAddDTO);
        return Result.OK("添加成功！");
    }

    @ApiOperation(value = "取消预约", notes = "取消预约")
    @PostMapping(value = "/cancelAppoint")
    public Result<String> cancelAppoint(@Validate @RequestBody JSONObject jsonObject) {
        String appointId = jsonObject.getString("appointId");
        ClinicAppoint clinicAppoint = moClinicAppointService.getById(appointId);
        if (!clinicAppoint.getAppointStatus().equals(2)) {
            throw new RuntimeException("暂不能取消！");
        }
        DoctorWorkDetail doctorWorkDetail = doctorWorkDetailService.getById(clinicAppoint.getDoctorWorkDetailId());
        String patientAppointListKey = CommonConstant.PREFIX_PATIENT_APPOINT_List + clinicAppoint.getDoctorId() + "_" +
                doctorWorkDetail.getWorkPeriod() + "_" + clinicAppoint.getAppointDay();
        ClinicAppoint clinicAppointUpdate = new ClinicAppoint();
        clinicAppointUpdate.setId(appointId);
        clinicAppointUpdate.setAppointStatus(4);
        clinicAppointUpdate.setClinicStatus(5);
        moClinicAppointService.updateById(clinicAppointUpdate);
        redisTemplate.opsForSet().remove(patientAppointListKey, appointId);
        clinicAppoint.setId(null);
        clinicAppoint.setCardId(null);
        clinicAppoint.setAppointStatus(0);
        moClinicAppointService.save(clinicAppoint);
        return Result.OK("取消成功！");
    }

    @ApiOperation(value = "删除预约", notes = "删除预约")
    @DeleteMapping(value = "/deleteAppoint")
    public Result<String> deleteAppoint(@RequestBody JSONObject jsonObject) {
        String appointId = jsonObject.getString("appointId");
        ClinicAppoint clinicAppoint = new ClinicAppoint();
        clinicAppoint.setId(appointId);
        clinicAppoint.setDelFlag(1);
        moClinicAppointService.updateById(clinicAppoint);
        return Result.OK("删除预约成功！");
    }
}
