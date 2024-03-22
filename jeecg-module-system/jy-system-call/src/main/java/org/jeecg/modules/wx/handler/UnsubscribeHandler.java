package org.jeecg.modules.wx.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import org.jeecg.modules.common.entity.Patient;
import org.jeecg.modules.common.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/*
 *@Description: 公众号取消关注
 *@Param:
 *@Return:
 *@author: xiaopeng.wu
 *@DateTime: 10:31 2023/12/5
**/
@Component
@Slf4j
public class UnsubscribeHandler extends AbstractHandler {
    @Resource
    private IPatientService iPatientService;
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        logger.info("取消关注用户 OPENID: " + openId);
        // TODO 可以更新本地数据库为取消关注状态
        Patient patient = new Patient();
        patient.setFollowStatus(2);
        iPatientService.update(patient, new QueryWrapper<Patient>().eq("open_id", openId));
        return null;
    }

}
