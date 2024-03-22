package org.jeecg.modules.mo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.ClinicAppoint;
import org.jeecg.modules.common.entity.Office;
import org.jeecg.modules.common.service.IClinicAppointService;
import org.jeecg.modules.common.service.IOfficeService;
import org.jeecg.modules.mo.dto.MoHospitalPageDTO;
import org.jeecg.modules.mo.service.IMoDoctorService;
import org.jeecg.modules.mo.service.IMoHospitalService;
import org.jeecg.modules.mo.vo.MoDoctorVO;
import org.jeecg.modules.mo.vo.MoOfficeListVO;
import org.jeecg.modules.mo.vo.MoSysTenantDetailVO;
import org.jeecg.modules.mo.vo.MoSysTenantPageVO;
import org.jeecg.modules.pc.vo.DoctorUserDetailVO;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.service.ISysTenantService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: his_clinic_appoint
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "C端-医院信息查询")
@RestController
@RequestMapping("/mo/hospital")
@Slf4j
@RequiredArgsConstructor
public class MoHospitalController extends JeecgController<ClinicAppoint, IClinicAppointService> {
    private final IMoHospitalService hospitalService;
    private final IOfficeService officeService;
    private final ISysTenantService tenantService;
    private final IMoDoctorService moDoctorService;


    @ApiOperation(value = "查询医院详情", notes = "查询医院详情")
    @GetMapping(value = "/queryHospitalDetail")
    public Result<MoSysTenantDetailVO> queryHospitalDetail(@RequestParam("tenantId") String tenantId) {
        SysTenant sysTenant = tenantService.getById(tenantId);
        return Result.OK(BeanUtil.toBean(sysTenant, MoSysTenantDetailVO.class));
    }

    @ApiOperation(value = "查询医院-分页列表查询", notes = "查询医院-分页列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "医院名称", paramType = "query", dataType = "String", required = true),
    })
    @GetMapping(value = "/queryHospitalPage")
    public Result<IPage<MoSysTenantPageVO>> queryPageList(String name,
                                                          @RequestParam(name = "pageNo") Integer pageNo,
                                                          @RequestParam(name = "pageSize") Integer pageSize,
                                                          HttpServletRequest req) {
        MoHospitalPageDTO pageDTOParam = new MoHospitalPageDTO();
        pageDTOParam.setName(name);
        IPage<MoSysTenantPageVO> pageList = hospitalService.queryPageList(pageDTOParam, pageNo, pageSize);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "查询科室-列表查询", notes = "查询科室-列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户Id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "parentId", value = "科室父Id", paramType = "query", dataType = "String", required = false)
    })
    @GetMapping(value = "/queryOfficeList")
    public Result<List<MoOfficeListVO>> queryOfficeList(@RequestParam("tenantId") String tenantId, String parentId) {
        QueryWrapper<Office> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Office::getTenantId, tenantId)
                .eq(StrUtil.isNotBlank(parentId), Office::getParentId, parentId);
        List<Office> list = officeService.list(queryWrapper);
        return Result.OK(oConvertUtils.entityListToModelList(list, MoOfficeListVO.class));
    }

    @ApiOperation(value = "查询医生-列表查询", notes = "查询医生-列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户Id", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "officeId", value = "科室Id", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping(value = "/queryDoctorList")
    public Result<List<MoDoctorVO>> queryDoctorList(@RequestParam("tenantId") String tenantId,String officeId) {
        List<MoDoctorVO> moDoctorVOList = moDoctorService.getDoctorList(tenantId, officeId);
        return Result.OK(moDoctorVOList);
    }

    @ApiOperation(value = "查询当天排班医生-列表查询", notes = "查询当天排班医生-列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户Id", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "officeId", value = "科室Id", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping(value = "/queryTodayDoctorWorkList")
    public Result<List<MoDoctorVO>> queryTodayDoctorWorkList(@RequestParam("tenantId") String tenantId,String officeId) {
        List<MoDoctorVO> moDoctorVOList = moDoctorService.queryTodayDoctorWorkList(tenantId, officeId);
        return Result.OK(moDoctorVOList);
    }

    @ApiOperation(value = "查询医生详情", notes = "查询医生详情-通过doctorId查询")
    @GetMapping(value = "/queryDoctorById")
    public Result<DoctorUserDetailVO> queryDoctorUserById(@RequestParam(name = "doctorId", required = true) String doctorId) {
        DoctorUserDetailVO userDetail = moDoctorService.queryDoctorUserById(doctorId);
        return Result.OK(userDetail);
    }
}
