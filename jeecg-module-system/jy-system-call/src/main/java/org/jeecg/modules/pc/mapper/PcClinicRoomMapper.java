package org.jeecg.modules.pc.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.entity.ClinicRoom;

/**
 * @Description: his_clinic_room
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
public interface PcClinicRoomMapper extends BaseMapper<ClinicRoom> {
    /**
     * 查询诊室
     * @param clinicRoomId
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    ClinicRoom getClinicRoom(@Param("clinicRoomId")String clinicRoomId);
}
