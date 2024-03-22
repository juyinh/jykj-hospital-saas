package org.jeecg.modules.pc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.common.entity.Doctor;
import org.jeecg.modules.common.service.IDoctorService;
import org.jeecg.modules.pc.dto.DoctorAddDTO;
import org.jeecg.modules.pc.dto.DoctorFrozenDTO;
import org.jeecg.modules.pc.dto.DoctorQueryPageDTO;
import org.jeecg.modules.pc.dto.DoctorUpdateDTO;
import org.jeecg.modules.pc.service.IPcDoctorService;
import org.jeecg.modules.pc.vo.DoctorQueryPageVO;
import org.jeecg.modules.pc.vo.DoctorUserDetailVO;
import org.jeecg.modules.system.service.ISysUserService;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "医院-医生管理")
@RestController
@RequestMapping("/pc/doctor")
@Slf4j
public class PcDoctorController extends JeecgController<Doctor, IDoctorService> {
    @Resource
    private IDoctorService doctorService;
    @Resource
    private IPcDoctorService pcDoctorService;
    @Resource
    private ISysUserService sysUserService;

    @ApiOperation(value = "医生信息-分页列表查询", notes = "医生信息-分页列表查询")
    @GetMapping(value = "/queryPageList")
    public Result<IPage<DoctorQueryPageVO>> queryPageList(DoctorQueryPageDTO queryPageDTO,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<DoctorQueryPageVO> result = pcDoctorService.queryPageList(queryPageDTO, pageNo, pageSize);
        return Result.OK(result);
    }

    @ApiOperation(value = "医生信息-列表查询", notes = "医生信息-列表查询")
    @GetMapping(value = "/queryList")
    public Result<List<DoctorQueryPageVO>> queryList(DoctorQueryPageDTO queryPageDTO) {
        List<DoctorQueryPageVO> list = pcDoctorService.queryList(queryPageDTO);
        return Result.OK(list);
    }

    @ApiOperation(value = "查询医生详情-通过id查询", notes = "查询医生详情-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<DoctorUserDetailVO> queryDoctorUserById(@RequestParam(name = "id", required = true) String id) {
        DoctorUserDetailVO userDetail = pcDoctorService.queryDoctorUserById(id);
        return Result.OK(userDetail);
    }

    @AutoLog(value = "医生信息-添加")
    @ApiOperation(value = "医生添加-添加", notes = "医生添加-添加")
    //@RequiresPermissions("common:his_doctor:add")
    @PostMapping(value = "/addDoctor")
    public Result<String> addDoctor(@RequestBody @Validate DoctorAddDTO addDTO) {
        pcDoctorService.addDoctor(addDTO);
        return Result.OK("添加成功！");
    }

    @ApiOperation(value = "医生信息-编辑", notes = "医生信息-编辑")
    //@RequiresPermissions("common:his_doctor:edit")
    @PutMapping(value = "/edit")
    public Result<String> edit(@RequestBody @Validate DoctorUpdateDTO updateDTO) {
        pcDoctorService.editDoctor(updateDTO);
        return Result.OK("编辑成功!");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "status", value = "状态(1：正常  2：冻结 ）", paramType = "query", dataType = "String", required = true),
    })
    @ApiOperation(value = "冻结&解冻用户", notes = "冻结&解冻用户")
    //@RequiresPermissions("system:user:frozen")
    @RequestMapping(value = "/frozen", method = RequestMethod.PUT)
    public Result frozenBatch(@RequestBody @Validate DoctorFrozenDTO frozenDTO) {
        sysUserService.updateStatus(frozenDTO.getUserId(), frozenDTO.getStatus());
        return Result.ok("操作成功");

    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", paramType = "query", dataType = "String", required = true)
    })
    @ApiOperation(value = "his_doctor-通过id删除", notes = "his_doctor-通过id删除")
    //@RequiresPermissions("common:his_doctor:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        sysUserService.deleteUser(id);
        return Result.OK("删除成功!");
    }
}
