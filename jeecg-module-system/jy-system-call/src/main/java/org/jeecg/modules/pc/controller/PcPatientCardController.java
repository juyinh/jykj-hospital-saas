package org.jeecg.modules.pc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.entity.PatientCard;
import org.jeecg.modules.common.service.IPatientCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/*
 *@Description: 用户注册的就诊信息管理
 *@Param: 
 *@Return: 
 *@author: xiaopeng.wu
 *@DateTime: 15:37 2023/12/5
**/
@Api(tags="医院-患者卡管理")
@RestController
@RequestMapping("/pc/patientCard")
@Slf4j
public class PcPatientCardController extends JeecgController<PatientCard, IPatientCardService> {
   @Autowired
   private IPatientCardService patientCardService;

   @ApiOperation(value="用户就诊卡-分页列表查询", notes="用户就诊卡-分页列表查询")
   @GetMapping(value = "/queryPageList")
   public Result<IPage<PatientCard>> queryPageList(PatientCard patientCard,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<PatientCard> queryWrapper = QueryGenerator.initQueryWrapper(patientCard, req.getParameterMap());
       queryWrapper.orderByDesc("create_time");
       Page<PatientCard> page = new Page<PatientCard>(pageNo, pageSize);
       IPage<PatientCard> pageList = patientCardService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   @ApiOperation(value="用户就诊卡-列表查询", notes="用户就诊卡-列表查询")
   @GetMapping(value = "/queryList")
   public Result<List<PatientCard>> queryList(PatientCard patientCard, HttpServletRequest req) {
       QueryWrapper<PatientCard> queryWrapper = QueryGenerator.initQueryWrapper(patientCard, req.getParameterMap());
       queryWrapper.orderByDesc("create_time");
       List<PatientCard> list = patientCardService.list(queryWrapper);
       return Result.OK(list);
   }


   @ApiOperation(value="用户就诊卡-添加", notes="用户就诊卡-添加")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody PatientCard patientCard) {
       patientCardService.save(patientCard);
       return Result.OK("添加成功！");
   }

   @ApiOperation(value="用户就诊卡-编辑", notes="用户就诊卡-编辑")
   //@RequiresPermissions("common:his_patient_card:edit")
   @PutMapping(value = "/edit")
   public Result<String> edit(@RequestBody PatientCard patientCard) {
       patientCardService.updateById(patientCard);
       return Result.OK("编辑成功!");
   }

   @ApiOperation(value="his_patient_card-通过id删除", notes="his_patient_card-通过id删除")
   //@RequiresPermissions("common:his_patient_card:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       patientCardService.removeById(id);
       return Result.OK("删除成功!");
   }
}
