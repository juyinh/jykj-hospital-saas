package org.jeecg.modules.common.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.entity.DoctorWork;
import org.jeecg.modules.common.service.IDoctorWorkService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: his_doctor_work
 * @Author: jeecg-boot
 * @Date:   2023-12-13
 * @Version: V1.0
 */
@Api(tags="his_doctor_work")
@RestController
@RequestMapping("/common/doctorWork")
@Slf4j
public class DoctorWorkController extends JeecgController<DoctorWork, IDoctorWorkService> {
	@Autowired
	private IDoctorWorkService doctorWorkService;
	
	/**
	 * 分页列表查询
	 *
	 * @param doctorWork
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "his_doctor_work-分页列表查询")
	@ApiOperation(value="his_doctor_work-分页列表查询", notes="his_doctor_work-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DoctorWork>> queryPageList(DoctorWork doctorWork,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DoctorWork> queryWrapper = QueryGenerator.initQueryWrapper(doctorWork, req.getParameterMap());
		Page<DoctorWork> page = new Page<DoctorWork>(pageNo, pageSize);
		IPage<DoctorWork> pageList = doctorWorkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param doctorWork
	 * @return
	 */
	@AutoLog(value = "his_doctor_work-添加")
	@ApiOperation(value="his_doctor_work-添加", notes="his_doctor_work-添加")
	@RequiresPermissions("common:his_doctor_work:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DoctorWork doctorWork) {
		doctorWorkService.save(doctorWork);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param doctorWork
	 * @return
	 */
	@AutoLog(value = "his_doctor_work-编辑")
	@ApiOperation(value="his_doctor_work-编辑", notes="his_doctor_work-编辑")
	@RequiresPermissions("common:his_doctor_work:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DoctorWork doctorWork) {
		doctorWorkService.updateById(doctorWork);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "his_doctor_work-通过id删除")
	@ApiOperation(value="his_doctor_work-通过id删除", notes="his_doctor_work-通过id删除")
	@RequiresPermissions("common:his_doctor_work:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		doctorWorkService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "his_doctor_work-批量删除")
	@ApiOperation(value="his_doctor_work-批量删除", notes="his_doctor_work-批量删除")
	@RequiresPermissions("common:his_doctor_work:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.doctorWorkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "his_doctor_work-通过id查询")
	@ApiOperation(value="his_doctor_work-通过id查询", notes="his_doctor_work-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DoctorWork> queryById(@RequestParam(name="id",required=true) String id) {
		DoctorWork doctorWork = doctorWorkService.getById(id);
		if(doctorWork==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(doctorWork);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param doctorWork
    */
    @RequiresPermissions("common:his_doctor_work:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DoctorWork doctorWork) {
        return super.exportXls(request, doctorWork, DoctorWork.class, "his_doctor_work");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("common:his_doctor_work:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DoctorWork.class);
    }

}
