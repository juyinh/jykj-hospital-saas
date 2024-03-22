package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.SysUserRoleCountVo;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
@Api(tags = "系统角色管理")
public class SysRoleController {
	@Autowired
	private ISysRoleService sysRoleService;
	@Autowired
	private ISysPermissionDataRuleService sysPermissionDataRuleService;
	@Autowired
	private ISysRolePermissionService sysRolePermissionService;
	@Autowired
	private ISysPermissionService sysPermissionService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
	@Autowired
	private BaseCommonService baseCommonService;
	@Autowired
	private ISysRoleIndexService sysRoleIndexService;

	/**
	  * 分页列表查询 【系统角色，不做租户隔离】
	 * @param role
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequiresPermissions("system:role:list")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysRole>> queryPageList(SysRole role,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysRole>> result = new Result<IPage<SysRole>>();
		//QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(role, req.getParameterMap());
		//IPage<SysRole> pageList = sysRoleService.page(page, queryWrapper);
		Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
		//换成不做租户隔离的方法，实际上还是存在缺陷（缺陷：如果开启租户隔离，虽然能看到其他租户下的角色，编辑会提示报错）
		IPage<SysRole> pageList = sysRoleService.listAllSysRole(page, role);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	@ApiOperation("查询租户列表角色-分页(租户隔离)")
	@RequestMapping(value = "/queryRoleByTenantPage", method = RequestMethod.GET)
	public Result<IPage<SysRole>> listByTenant(SysRole role,
												@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												HttpServletRequest req) {
		Result<IPage<SysRole>> result = new Result<IPage<SysRole>>();
		//------------------------------------------------------------------------------------------------
		//此接口必须通过租户来隔离查询
		role.setTenantId(oConvertUtils.getInt(!"0".equals(TenantContext.getTenant()) ? TenantContext.getTenant() : "", -1));
		if (StrUtil.isNotBlank(role.getRoleName())) {
			role.setRoleName("+" + role.getRoleName() + "+");//添加模糊查询
		}
		QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(role, req.getParameterMap());
		Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
		IPage<SysRole> pageList = sysRoleService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	@ApiOperation(value="查询租户下的角色-列表查询", notes="查询租户下的角色-列表查询")
	@RequestMapping(value = "/queryRoleList", method = RequestMethod.GET)
	public Result<List<SysRole>> queryRoleList() {
		Result<List<SysRole>> result = new Result<>();
		LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<SysRole>();
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysRole::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		List<SysRole> list = sysRoleService.list(query);
		if(list==null||list.size()<=0) {
			result.error500("未找到角色信息");
		}else {
			result.setResult(list);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	  *   添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    //@RequiresPermissions("system:role:add")
	public Result<SysRole> add(@RequestBody SysRole role) {
		Result<SysRole> result = new Result<SysRole>();
		try {
			String roleCode = RandomUtil.randomString(10);
			//开启多租户隔离,角色id自动生成10位
			if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
				role.setRoleCode(roleCode);
			}
			role.setCreateTime(new Date());
			sysRoleService.save(role);
			addRoleIndex(role.getId(), roleCode);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	private void addRoleIndex(String roleId,String roleCode){
		//给角色分配默认访问首页 wxp 20240203
		SysRoleIndex sysRoleIndex = new SysRoleIndex();
		sysRoleIndex.setRoleCode(roleCode);
		sysRoleIndex.setUrl("tenant/wellcome");
		sysRoleIndex.setComponent("layouts/RouteView");
		sysRoleIndex.setRoute(false);
		sysRoleIndex.setPriority(1);
		sysRoleIndex.setStatus("1");
		sysRoleIndex.setCreateBy("admin");
		sysRoleIndex.setCreateTime(new Date());
		sysRoleIndex.setSysOrgCode("A01");
		sysRoleIndexService.save(sysRoleIndex);

		SysRolePermission rolePermission = new SysRolePermission();
		rolePermission.setRoleId(roleId);
		rolePermission.setPermissionId("1763039069446680577");
		rolePermission.setOperateDate(new Date());
		sysRolePermissionService.save(rolePermission);
	}
	
	/**
	  *  编辑
	 * @param role
	 * @return
	 */
    //@RequiresPermissions("system:role:edit")
	@RequestMapping(value = "/edit",method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<SysRole> edit(@RequestBody SysRole role) {
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(role.getId());
		if(sysrole==null) {
			result.error500("未找到对应角色！");
		}else {
			role.setUpdateTime(new Date());

			//------------------------------------------------------------------
			//如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
			if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
				//获取当前用户
				LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
				Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
				String username = "admin";
				if (!tenantId.equals(role.getTenantId()) && !username.equals(sysUser.getUsername())) {
					baseCommonService.addLog("未经授权，修改非本租户下的角色ID：" + role.getId() + "，操作人：" + sysUser.getUsername(), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
					return Result.error("修改角色失败,当前角色不在此租户中。");
				}
			}
			//------------------------------------------------------------------
			
			boolean ok = sysRoleService.updateById(role);
			if(ok) {
				result.success("修改成功!");
			}
		}
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
    //@RequiresPermissions("system:role:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@Transactional(rollbackFor = Exception.class)
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
    	//如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
    	if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			//获取当前用户
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
			Long getRoleCount = sysRoleService.getRoleCountByTenantId(id, tenantId);
			String username = "admin";
			if(getRoleCount == 0 && !username.equals(sysUser.getUsername())){
				baseCommonService.addLog("未经授权，删除非本租户下的角色ID：" + id + "，操作人：" + sysUser.getUsername(), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
				return Result.error("删除角色失败,当前角色不在此租户中。");
			}
		}
		Long count = sysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
		if (count > 0) {
			throw new RuntimeException("暂不能删除，该角色下配置有用户。");
		}
		SysRole sysRole = sysRoleService.getById(id);
		sysRoleService.deleteRole(id);
		//删除对应首页配置
		sysRoleIndexService.remove(new QueryWrapper<SysRoleIndex>().lambda().eq(SysRoleIndex::getRoleCode, sysRole.getRoleCode()));
		return Result.ok("删除角色成功");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
    //@RequiresPermissions("system:role:deleteBatch")
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		baseCommonService.addLog("删除角色操作，角色ids：" + ids, CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
		Result<SysRole> result = new Result<SysRole>();
		if(oConvertUtils.isEmpty(ids)) {
			result.error500("未选中角色！");
		}else {
			//如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
			if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
				int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
				String[] roleIds = ids.split(SymbolConstant.COMMA);
				LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
				String username = "admin";
				for (String id:roleIds) {
					Long getRoleCount = sysRoleService.getRoleCountByTenantId(id, tenantId);
					//如果存在角色id为0，即不存在，则删除角色
					if(getRoleCount == 0 && !username.equals(sysUser.getUsername()) ){
						baseCommonService.addLog("未经授权，删除非本租户下的角色ID：" + id + "，操作人：" + sysUser.getUsername(), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_4);
						return Result.error("批量删除角色失败,存在角色不在此租户中，禁止批量删除");
					}
				}
			}
			sysRoleService.deleteBatchRole(ids.split(","));
			result.success("删除角色成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<SysRole> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(id);
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysrole);
			result.setSuccess(true);
		}
		return result;
	}



	/**
	 * 查询全部系统角色（不做租户隔离）
	 *
	 * @return
	 */
	@RequiresPermissions("system:role:queryallNoByTenant")
	@RequestMapping(value = "/queryallNoByTenant", method = RequestMethod.GET)
	public Result<List<SysRole>> queryallNoByTenant() {
		Result<List<SysRole>> result = new Result<>();
		LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<SysRole>();
		List<SysRole> list = sysRoleService.list(query);
		if(list==null||list.size()<=0) {
			result.error500("未找到角色信息");
		}else {
			result.setResult(list);
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	  * 校验角色编码唯一
	 */
	@RequestMapping(value = "/checkRoleCode", method = RequestMethod.GET)
	public Result<Boolean> checkUsername(String id,String roleCode) {
		Result<Boolean> result = new Result<>();
        //如果此参数为false则程序发生异常
		result.setResult(true);
		log.info("--验证角色编码是否唯一---id:"+id+"--roleCode:"+roleCode);
		try {
			SysRole role = null;
			if(oConvertUtils.isNotEmpty(id)) {
				role = sysRoleService.getById(id);
			}
			//SysRole newRole = sysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, roleCode));
			SysRole newRole = sysRoleService.getRoleNoTenant(roleCode);
			if(newRole!=null) {
				//如果根据传入的roleCode查询到信息了，那么就需要做校验了。
				if(role==null) {
					//role为空=>新增模式=>只要roleCode存在则返回false
					result.setSuccess(false);
					result.setMessage("角色编码已存在");
					return result;
				}else if(!id.equals(newRole.getId())) {
					//否则=>编辑模式=>判断两者ID是否一致-
					result.setSuccess(false);
					result.setMessage("角色编码已存在");
					return result;
				}
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(false);
			result.setMessage(e.getMessage());
			return result;
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 导出excel
	 * @param request
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(SysRole sysRole,HttpServletRequest request) {
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			sysRole.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		
		// Step.1 组装查询条件
		QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(sysRole, request.getParameterMap());
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<SysRole> pageList = sysRoleService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME,"角色列表");
		mv.addObject(NormalExcelConstants.CLASS,SysRole.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		mv.addObject(NormalExcelConstants.PARAMS,new ExportParams("角色列表数据","导出人:"+user.getRealname(),"导出信息"));
		mv.addObject(NormalExcelConstants.DATA_LIST,pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
			MultipartFile file = entity.getValue();
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				return sysRoleService.importExcelCheckRoleCode(file, params);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return Result.error("文件导入失败！");
	}
	
	/**
	 * 查询数据规则数据
	 */
	@GetMapping(value = "/datarule/{permissionId}/{roleId}")
	public Result<?> loadDatarule(@PathVariable("permissionId") String permissionId,@PathVariable("roleId") String roleId) {
		List<SysPermissionDataRule> list = sysPermissionDataRuleService.getPermRuleListByPermId(permissionId);
		if(list==null || list.size()==0) {
			return Result.error("未找到权限配置信息");
		}else {
			Map<String,Object> map = new HashMap(5);
			map.put("datarule", list);
			LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>()
					.eq(SysRolePermission::getPermissionId, permissionId)
					.isNotNull(SysRolePermission::getDataRuleIds)
					.eq(SysRolePermission::getRoleId,roleId);
			SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
			if(sysRolePermission==null) {
				//return Result.error("未找到角色菜单配置信息");
			}else {
				String drChecked = sysRolePermission.getDataRuleIds();
				if(oConvertUtils.isNotEmpty(drChecked)) {
					map.put("drChecked", drChecked.endsWith(",")?drChecked.substring(0, drChecked.length()-1):drChecked);
				}
			}
			return Result.ok(map);
			//TODO 以后按钮权限的查询也走这个请求 无非在map中多加两个key
		}
	}
	
	/**
	 * 保存数据规则至角色菜单关联表
	 */
	@PostMapping(value = "/datarule")
	public Result<?> saveDatarule(@RequestBody JSONObject jsonObject) {
		try {
			String permissionId = jsonObject.getString("permissionId");
			String roleId = jsonObject.getString("roleId");
			String dataRuleIds = jsonObject.getString("dataRuleIds");
			log.info("保存数据规则>>"+"菜单ID:"+permissionId+"角色ID:"+ roleId+"数据权限ID:"+dataRuleIds);
			LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>()
					.eq(SysRolePermission::getPermissionId, permissionId)
					.eq(SysRolePermission::getRoleId,roleId);
			SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
			if(sysRolePermission==null) {
				return Result.error("请先保存角色菜单权限!");
			}else {
				sysRolePermission.setDataRuleIds(dataRuleIds);
				this.sysRolePermissionService.updateById(sysRolePermission);
			}
		} catch (Exception e) {
			log.error("SysRoleController.saveDatarule()发生异常：" + e.getMessage(),e);
			return Result.error("保存失败");
		}
		return Result.ok("保存成功!");
	}
	
	
	/**
	 * 用户角色授权功能，查询菜单权限树
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<Map<String,Object>> queryTreeList(HttpServletRequest request) {
		Result<Map<String,Object>> result = new Result<>();
		//全部权限ids
		List<String> ids = new ArrayList<>();
		List<SysPermission> list = new ArrayList<>();
		//此接口必须通过租户来隔离查询
		try {
			//直接获取当前用户不适用前端token
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			if (oConvertUtils.isEmpty(loginUser)) {
				return Result.error("请登录系统！");
			}
			if (!"admin".equals(loginUser.getUsername())) {
				list = sysPermissionService.queryByUser(loginUser.getUsername());
			}else {
				LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
				query.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
				query.orderByAsc(SysPermission::getSortNo);
				list = sysPermissionService.list(query);
			}
			for(SysPermission sysPer : list) {
				ids.add(sysPer.getId());
			}
			List<TreeModel> treeList = new ArrayList<>();
			getTreeModelList(treeList, list, null);
			Map<String,Object> resMap = new HashMap(5);
            //全部树节点数据
			resMap.put("treeList", treeList);
            //全部树ids
			resMap.put("ids", ids);
			result.setResult(resMap);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
	
	private void getTreeModelList(List<TreeModel> treeList,List<SysPermission> metaList,TreeModel temp) {
		for (SysPermission permission : metaList) {
			String tempPid = permission.getParentId();
			TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(),permission.getRuleFlag(), permission.isLeaf());
			if(temp==null && oConvertUtils.isEmpty(tempPid)) {
				treeList.add(tree);
				if(!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}else if(temp!=null && tempPid!=null && tempPid.equals(temp.getKey())){
				temp.getChildren().add(tree);
				if(!tree.getIsLeaf()) {
					getTreeModelList(treeList, metaList, tree);
				}
			}
		}
	}

    /**
     * TODO 权限未完成（敲敲云接口，租户应用）
     * 分页获取全部角色列表（包含每个角色的数量）
     * @return
     */
    @RequestMapping(value = "/queryPageRoleCount", method = RequestMethod.GET)
    public Result<IPage<SysUserRoleCountVo>> queryPageRoleCount(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<IPage<SysUserRoleCountVo>> result = new Result<>();
		LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<SysRole>();
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			query.eq(SysRole::getTenantId, oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
        Page<SysRole> page = new Page<>(pageNo, pageSize);
        IPage<SysRole> pageList = sysRoleService.page(page, query);
        List<SysRole> records = pageList.getRecords();
        IPage<SysUserRoleCountVo> sysRoleCountPage = new PageDTO<>();
        List<SysUserRoleCountVo> sysCountVoList = new ArrayList<>();
        //循环角色数据获取每个角色下面对应的角色数量
        for (SysRole role:records) {
            LambdaQueryWrapper<SysUserRole> countQuery = new LambdaQueryWrapper<>();
			countQuery.eq(SysUserRole::getRoleId,role.getId());
            long count = sysUserRoleService.count(countQuery);
            SysUserRoleCountVo countVo = new SysUserRoleCountVo();
            BeanUtils.copyProperties(role,countVo);
            countVo.setCount(count);
            sysCountVoList.add(countVo);
        }
        sysRoleCountPage.setRecords(sysCountVoList);
        sysRoleCountPage.setTotal(pageList.getTotal());
        sysRoleCountPage.setSize(pageList.getSize());
        result.setSuccess(true);
        result.setResult(sysRoleCountPage);
        return result;
    }
}
