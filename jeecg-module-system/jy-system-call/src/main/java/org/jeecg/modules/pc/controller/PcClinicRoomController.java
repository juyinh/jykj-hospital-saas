package org.jeecg.modules.pc.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.ClinicRoom;
import org.jeecg.modules.common.entity.Office;
import org.jeecg.modules.common.service.IClinicRoomService;
import org.jeecg.modules.common.service.IOfficeService;
import org.jeecg.modules.pc.vo.ClinicRoomDetail;
import org.jeecg.modules.pc.vo.ClinicRoomPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@Api(tags = "医院-诊室信息管理")
@RestController
@RequestMapping("/pc/clinicRoom")
@Slf4j
public class PcClinicRoomController extends JeecgController<ClinicRoom, IClinicRoomService> {
    @Autowired
    private IClinicRoomService clinicRoomService;
    @Resource
    private IOfficeService officeService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "clinicRoomName", value = "诊室名称（模糊查询）", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "officeId", value = "科室ID", paramType = "query", dataType = "String", required = false),
    })
    @ApiOperation(value = "诊室信息-分页列表查询", notes = "诊室信息-分页列表查询")
    @GetMapping(value = "/queryPageList")
    public Result<IPage<ClinicRoomPageVO>> queryPageList(String clinicRoomName, String officeId,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<ClinicRoom> queryWrapper = new QueryWrapper<ClinicRoom>();
        queryWrapper.lambda()
                .like(StrUtil.isNotBlank(clinicRoomName), ClinicRoom::getClinicRoomName, clinicRoomName)
                .eq(StrUtil.isNotBlank(officeId), ClinicRoom::getOfficeId, officeId);
        queryWrapper.orderByDesc("create_time");
        Page<ClinicRoom> page = new Page<ClinicRoom>(pageNo, pageSize);
        IPage<ClinicRoom> pageList = clinicRoomService.page(page, queryWrapper);
        return Result.OK(oConvertUtils.toModelPage(pageList, ClinicRoomPageVO.class));
    }

    @ApiOperation(value = "诊室信息-列表查询", notes = "诊室信息-列表查询")
    @GetMapping(value = "/queryList")
    public Result<List<ClinicRoom>> queryList() {
        QueryWrapper<ClinicRoom> queryWrapper = new QueryWrapper<ClinicRoom>();
        queryWrapper.orderByDesc("create_time");
        List<ClinicRoom> list = clinicRoomService.list(queryWrapper);
        return Result.OK(list);
    }

    @ApiOperation(value = "诊室信息-通过id查询", notes = "诊室信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ClinicRoomDetail> queryById(@RequestParam(name = "id", required = true) String id) {
        ClinicRoom clinicRoom = clinicRoomService.getById(id);
        if (clinicRoom == null) {
            return Result.error("未找到对应数据");
        }
        ClinicRoomDetail result = BeanUtil.toBean(clinicRoom, ClinicRoomDetail.class);
        Office office = officeService.getById(clinicRoom.getOfficeId());
        result.setOfficeName(office != null ? office.getOfficeName() : "");
        return Result.OK(result);
    }

    @ApiOperation(value = "诊室信息-添加", notes = "诊室信息-添加")
    //@RequiresPermissions("common:his_clinic_room:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ClinicRoom clinicRoom) {
        clinicRoomService.save(clinicRoom);
        return Result.OK("添加成功！");
    }

    @ApiOperation(value = "诊室信息-编辑", notes = "诊室信息-编辑")
    //@RequiresPermissions("common:his_clinic_room:edit")
    @PutMapping(value = "/edit")
    public Result<String> edit(@RequestBody ClinicRoom clinicRoom) {
        clinicRoomService.updateById(clinicRoom);
        return Result.OK("编辑成功!");
    }

    @ApiOperation(value = "诊室信息-通过id删除", notes = "诊室信息-通过id删除")
    //@RequiresPermissions("common:his_clinic_room:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        clinicRoomService.removeById(id);
        return Result.OK("删除成功!");
    }

    @ApiOperation(value = "诊室信息-批量删除", notes = "诊室信息-批量删除")
    //@RequiresPermissions("common:his_clinic_room:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.clinicRoomService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }
}
