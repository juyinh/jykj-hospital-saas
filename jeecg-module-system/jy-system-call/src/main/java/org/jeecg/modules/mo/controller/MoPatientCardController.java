package org.jeecg.modules.mo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
import org.jeecg.modules.common.entity.PatientCard;
import org.jeecg.modules.common.service.IPatientCardService;
import org.jeecg.modules.mo.dto.MoPatientCardAddDTO;
import org.jeecg.modules.mo.dto.MoPatientCardListQueryDTO;
import org.jeecg.modules.mo.service.IMoPatientCardService;
import org.jeecg.modules.mo.vo.MoPatientCardListVO;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: his_patient_card
 * @Author: jeecg-boot
 * @Date: 2023-11-27
 * @Version: V1.0
 */
@Api(tags = "C端-患者卡")
@RestController
@RequestMapping("/mo/patientCard")
@Slf4j
public class MoPatientCardController extends JeecgController<PatientCard, IPatientCardService> {
    @Resource
    private IPatientCardService patientCardService;
    @Resource
    private IMoPatientCardService moPatientCardService;


    @ApiOperation(value = "用户就诊卡-分页列表查询", notes = "用户就诊卡-分页列表查询")
    @GetMapping(value = "/queryPageList")
    public Result<IPage<MoPatientCardListVO>> queryPageList(MoPatientCardListQueryDTO queryDTO,
                                                            @RequestHeader(name = "openId") String openId,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            HttpServletRequest req) {
        if (StrUtil.isBlank(openId)) {
            return Result.error("未找到对应数据");
        }
        PatientCard patientCard = BeanUtil.copyProperties(queryDTO, PatientCard.class);
        patientCard.setOpenId(openId);
        QueryWrapper<PatientCard> queryWrapper = QueryGenerator.initQueryWrapper(patientCard, req.getParameterMap());
        Page<PatientCard> page = new Page<PatientCard>(pageNo, pageSize);
        IPage<PatientCard> pageList = patientCardService.page(page, queryWrapper);
        return Result.OK(oConvertUtils.toModelPage(pageList, MoPatientCardListVO.class));
    }

    @ApiOperation(value = "用户就诊卡-列表查询", notes = "用户就诊卡-列表查询")
    @GetMapping(value = "/queryList")
    public Result<List<MoPatientCardListVO>> queryList(MoPatientCardListQueryDTO query,
                                                       @RequestHeader(name = "openId") String openId,
                                                       HttpServletRequest req) {
        if (StrUtil.isBlank(openId)) {
            return Result.error("未找到对应数据");
        }
        MoPatientCardListQueryDTO queryDTO = BeanUtil.toBean(req.getParameterMap(), MoPatientCardListQueryDTO.class);
        PatientCard patientCard = BeanUtil.copyProperties(queryDTO, PatientCard.class);
        patientCard.setOpenId(openId);
        QueryWrapper<PatientCard> queryWrapper = QueryGenerator.initQueryWrapper(patientCard, req.getParameterMap());
        List<PatientCard> list = patientCardService.list(queryWrapper);
        return Result.OK(oConvertUtils.entityListToModelList(list, MoPatientCardListVO.class));
    }

    @ApiOperation(value = "用户就诊卡-通过id查询", notes = "用户就诊卡-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<PatientCard> queryById(@RequestParam(name = "id", required = true) String id) {
        PatientCard patientCard = patientCardService.getById(id);
        if (patientCard == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(patientCard);
    }

    @ApiOperation(value = "用户就诊卡-添加", notes = "用户就诊卡-添加")
    @PostMapping(value = "/addPatientCard")
    public Result<String> addPatientCard(@RequestBody @Validate MoPatientCardAddDTO cardAddDTO,
                                         @RequestHeader(name = "openId") String openId) {
        if (cardAddDTO.getAge() == null && cardAddDTO.getMonth() == null){
            throw new RuntimeException("年龄和月份不能同时为空！");
        }
        PatientCard patientCard = BeanUtil.copyProperties(cardAddDTO, PatientCard.class);
        patientCard.setOpenId(openId);
        patientCardService.save(patientCard);
        return Result.OK("添加成功！");
    }

    @ApiOperation(value = "用户就诊卡-编辑", notes = "用户就诊卡-编辑")
    @PostMapping(value = "/edit")
    public Result<String> edit(@RequestBody PatientCard patientCard) {
        patientCardService.updateById(patientCard);
        return Result.OK("编辑成功!");
    }

    @ApiOperation(value = "设置默认就诊人", notes = "设置默认就诊人")
    @PutMapping(value = "/editDefaultClinic")
    public Result<String> editDefaultClinic(@RequestParam("cardId") String cardId,
                                            @RequestHeader(name = "openId") String openId) {
        moPatientCardService.editDefaultClinic(cardId, openId);
        return Result.OK("编辑成功!");
    }

    @ApiOperation(value = "用户就诊卡-通过id删除", notes = "用户就诊卡-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        patientCardService.removeById(id);
        return Result.OK("删除成功!");
    }
}
