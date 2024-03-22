package org.jeecg.modules.pc.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import org.jeecg.modules.pc.mapper.PcClinicAppointMapper;
import org.jeecg.modules.pc.service.IPcStatisticsService;
import org.jeecg.modules.pc.vo.PatientAgeSexVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: his_patient_case
 * @Author: jeecg-boot
 * @Date:   2023-12-01
 * @Version: V1.0
 */
@Service
public class PcStatisticsServiceImpl  implements IPcStatisticsService {
    @Resource
    private PcClinicAppointMapper pcClinicAppointMapper;


    @Override
    public List<Map<String, Object>> queryPatientWeekSum() {
        String beginTime = DateUtil.format(DateUtil.offsetDay(new Date(), -6), "yyyy-MM-dd");
        String endTime = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String,Object>> clinicAppointList = pcClinicAppointMapper.getPatientAppointClinicSum(beginTime, endTime);
        for (int i = 6; i >= 0; i--) {
            String appointDay = DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
            Boolean flag = true;
            for (Map<String, Object> map : clinicAppointList) {
                if (appointDay.equals(MapUtil.getStr(map, "appointDay"))) {
                    flag = false;
                    result.add(map);
                    continue;
                }
            }
            if (flag) {
                Map<String, Object> map = new HashMap<>();
                map.put("appointDay", appointDay);
                map.put("clinicSum", 0);
                map.put("appointSum", 0);
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public PatientAgeSexVO queryPatientAgeData(Integer period) {
        String beginTime = "";
        String endTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (period.equals(1)) {//本周
            beginTime = DateUtil.format(DateUtil.offsetDay(new Date(), -7), "yyyy-MM-dd HH:mm:ss");
        } else if (period.equals(2)) {//本月
            beginTime = DateUtil.format(DateUtil.offsetMonth(new Date(), -1), "yyyy-MM-dd HH:mm:ss");
        } else if (period.equals(3)) {
            beginTime = DateUtil.format(DateUtil.offsetMonth(new Date(), -12), "yyyy-MM-dd HH:mm:ss");
        }
        PatientAgeSexVO patientAgeSexVO = pcClinicAppointMapper.getAgeData(beginTime, endTime);
        return patientAgeSexVO;
    }

    @Override
    public Map<String, Object> queryPatientSexData(Integer period) {
        String beginTime = "";
        String endTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (period.equals(1)) {//本周
            beginTime = DateUtil.format(DateUtil.offsetDay(new Date(), -7), "yyyy-MM-dd HH:mm:ss");
        } else if (period.equals(2)) {//本月
            beginTime = DateUtil.format(DateUtil.offsetMonth(new Date(), -1), "yyyy-MM-dd HH:mm:ss");
        } else if (period.equals(3)) {
            beginTime = DateUtil.format(DateUtil.offsetMonth(new Date(), -12), "yyyy-MM-dd HH:mm:ss");
        }
        Map<String, Object>  map = pcClinicAppointMapper.getSexData(beginTime, endTime);
        return map;
    }
}
