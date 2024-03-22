package org.jeecg.modules.mo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.mo.dto.MoHospitalPageDTO;
import org.jeecg.modules.mo.vo.MoSysTenantPageVO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 就诊预约表
 * @Author: jeecg-boot
 * @Date: 2023-11-29
 * @Version: V1.0
 */
public interface IMoHospitalService {

    /*
     *@Description: 查询医院信息
     *@Param: [pageDTO, pageNo, pageSize]
     *@Return: com.baomidou.mybatisplus.core.metadata.IPage<org.jeecg.modules.mo.vo.SysTenantPageVO>
     *@author: xiaopeng.wu
     *@DateTime: 10:19 2023/12/22
    **/
    IPage<MoSysTenantPageVO> queryPageList(MoHospitalPageDTO pageDTO,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);
}
