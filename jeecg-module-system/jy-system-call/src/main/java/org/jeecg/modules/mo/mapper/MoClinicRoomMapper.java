package org.jeecg.modules.mo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.ClinicRoom;

import java.util.Map;

/**
 * @Description: his_clinic_room
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine="true")
public interface MoClinicRoomMapper extends BaseMapper<ClinicRoom> {
    /**
     * 查询诊室数据
     * @param appointId
     * @return
     */
    Map<String, String> getClinicRoomNameByAppoint(@Param("appointId") String appointId);
}
