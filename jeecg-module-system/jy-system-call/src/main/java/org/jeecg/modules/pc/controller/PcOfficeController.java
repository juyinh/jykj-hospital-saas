package org.jeecg.modules.pc.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.ClinicRoom;
import org.jeecg.modules.common.entity.Office;
import org.jeecg.modules.common.service.IClinicRoomService;
import org.jeecg.modules.common.service.IOfficeService;
import org.jeecg.modules.pc.vo.OfficePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Api(tags="医院-科室管理")
@RestController
@RequestMapping("/pc/office")
@Slf4j
public class PcOfficeController extends JeecgController<Office, IOfficeService> {
   @Autowired
   private IOfficeService officeService;
    @Resource
    private IClinicRoomService clinicRoomService;

   @ApiOperation(value="科室信息-分页列表查询", notes="科室信息-分页列表查询")
   @GetMapping(value = "/queryPageList")
   public Result<IPage<OfficePageVO>> queryPageList(String officeName,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
       QueryWrapper<Office> queryWrapper = new QueryWrapper<Office>();
       queryWrapper.lambda().like(StrUtil.isNotBlank(officeName), Office::getOfficeName, officeName);
       queryWrapper.orderByDesc("create_time");
       Page<Office> page = new Page<Office>(pageNo, pageSize);
       IPage<Office> pageList = officeService.page(page, queryWrapper);
       return Result.OK(oConvertUtils.toModelPage(pageList, OfficePageVO.class));
   }

   @ApiOperation(value="科室信息-列表查询", notes="科室信息-列表查询")
   @GetMapping(value = "/list")
   public Result<List<Office>> queryList(Office office, HttpServletRequest req) {
       QueryWrapper<Office> queryWrapper = QueryGenerator.initQueryWrapper(office, req.getParameterMap());
       List<Office> list = officeService.list(queryWrapper);
       return Result.OK(list);
   }

   @ApiOperation(value="科室信息-添加", notes="科室信息-添加")
   //@RequiresPermissions("common:his_office:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody Office office) {
       officeService.save(office);
       return Result.OK("添加成功！");
   }

   @ApiOperation(value="科室信息-编辑", notes="科室信息-编辑")
   //@RequiresPermissions("common:his_office:edit")
   @PutMapping(value = "/edit")
   public Result<String> edit(@RequestBody Office office) {
       officeService.updateById(office);
       return Result.OK("编辑成功!");
   }

   @ApiOperation(value="科室信息-通过id删除", notes="科室信息-通过id删除")
   //@RequiresPermissions("common:his_office:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       Long count = clinicRoomService.count(new LambdaQueryWrapper<ClinicRoom>()
               .eq(ClinicRoom::getOfficeId, id));
       if (count > 0) {
           throw new RuntimeException("暂不能删除，该科室下存在诊室数据");
       }
       officeService.removeById(id);
       return Result.OK("删除成功!");
   }

   @ApiOperation(value="科室信息-批量删除", notes="科室信息-批量删除")
   //@RequiresPermissions("common:his_office:deleteBatch")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.officeService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   @ApiOperation(value="科室信息-通过id查询", notes="科室信息-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<Office> queryById(@RequestParam(name="id",required=true) String id) {
       Office office = officeService.getById(id);
       if(office==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(office);
   }
}
