package org.jeecg.modules.mo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.common.entity.PatientCard;

/**
 * @Description: his_patient_card
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
public interface IMoPatientCardService extends IService<PatientCard> {
    /*
     *@Description: 设置默认就诊人
     *@Param: [cardId]
     *@Return: void
     *@author: xiaopeng.wu
     *@DateTime: 9:33 2023/12/25
    **/
    void editDefaultClinic(String cardId,String openId);
}
