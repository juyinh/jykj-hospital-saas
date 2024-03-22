package org.jeecg.modules.mo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.common.entity.PatientCard;
import org.jeecg.modules.common.mapper.PatientCardMapper;
import org.jeecg.modules.common.service.IPatientCardService;
import org.jeecg.modules.mo.service.IMoPatientCardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: his_patient_card
 * @Author: jeecg-boot
 * @Date:   2023-11-27
 * @Version: V1.0
 */
@Service
public class MoPatientCardServiceImpl extends ServiceImpl<PatientCardMapper, PatientCard> implements IMoPatientCardService {

    @Override
    public void editDefaultClinic(String cardId, String openId) {
        List<PatientCard> patientCardList = this.list(new QueryWrapper<PatientCard>().lambda()
                .eq(PatientCard::getOpenId, openId)
                .eq(PatientCard::getIsDefault, 1));
        if (patientCardList.size() > 0) {
            for (PatientCard patientCard : patientCardList) {
                patientCard.setIsDefault(0);
                this.updateById(patientCard);
            }
        }
        PatientCard patientCard = new PatientCard();
        patientCard.setId(cardId);
        patientCard.setIsDefault(1);
        this.updateById(patientCard);
    }
}
