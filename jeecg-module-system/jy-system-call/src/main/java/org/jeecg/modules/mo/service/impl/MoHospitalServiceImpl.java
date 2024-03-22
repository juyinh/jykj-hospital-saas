package org.jeecg.modules.mo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.mo.dto.MoHospitalPageDTO;
import org.jeecg.modules.mo.service.IMoHospitalService;
import org.jeecg.modules.mo.vo.MoSysTenantPageVO;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.service.ISysTenantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 就诊预约表
 * @Author: jeecg-boot
 * @Date: 2023-11-29
 * @Version: V1.0
 */
@Service
public class MoHospitalServiceImpl implements IMoHospitalService {
    @Resource
    private ISysTenantService sysTenantService;

    @Override
    public IPage<MoSysTenantPageVO> queryPageList(MoHospitalPageDTO pageDTO, Integer pageNo, Integer pageSize) {
        Page<SysTenant> page = new Page<SysTenant>(pageNo, pageSize);
        QueryWrapper<SysTenant> queryWrapper = new QueryWrapper<SysTenant>();
        queryWrapper.lambda()
                .eq(SysTenant::getTrade, 1) //医疗行业
                .eq(SysTenant::getDelFlag, 0)//未删除
                .eq(SysTenant::getStatus, 1)//未冻结
                .eq(SysTenant::getAuditStatus, 1)//审核通过
                .like(StrUtil.isNotBlank(pageDTO.getName()), SysTenant::getName, pageDTO.getName());
        IPage<SysTenant> pageVOIPage = sysTenantService.page(page, queryWrapper);
        return oConvertUtils.toModelPage(pageVOIPage, MoSysTenantPageVO.class);
    }
}
