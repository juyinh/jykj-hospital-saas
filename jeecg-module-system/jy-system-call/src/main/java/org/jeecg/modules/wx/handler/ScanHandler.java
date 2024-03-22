package org.jeecg.modules.wx.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.jeecg.modules.wx.builder.TextBuilder;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Component
public class ScanHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("用户扫码关注:{} ", JSON.toJSONString(wxMessage));
        WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);
        if (userWxInfo != null) {
            String inviteCode = null;
            if (StrUtil.isNotEmpty(wxMessage.getEventKey())){
                inviteCode = wxMessage.getEventKey().substring(8);
            }
            //@TODO 这里关注用户信息
        }
        return new TextBuilder().build("hi~欢迎扫码关注！", wxMessage, wxMpService);
        // 扫码事件处理
    }
}
