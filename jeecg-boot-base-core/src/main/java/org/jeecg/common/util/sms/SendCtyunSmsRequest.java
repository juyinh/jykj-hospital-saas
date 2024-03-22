package org.jeecg.common.util.sms;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2024/2/21 11:19
 **/
public class SendCtyunSmsRequest {

    /**
     * 接收短信的手机号码，格式：国内短信：无任何前缀的11位手机号码
     */
    private String phoneNumber;

    /**
     * 短信签名名称
     */
    private String signName;

    /**
     * 短信模板ID
     */
    private String templateCode;

    /**
     * 短信模板变量对应的实际值，JSON格式。说明：如果JSON中需要带换行符，请参照标准的JSON协议处理
     */
    private String templateParam;

    /**
     * 上行短信扩展码，上行短信，指发送给通信服务提供商的短信，用于定制某种服务、完成查询，或是办理某种业务等，需要收费的按运营商普通短信资费进行扣费
     */
    private String extendCode;

    /**
     * 客户自带短信标识，在状态报告中会原样返回
     */
    private String sessionId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getExtendCode() {
        return extendCode;
    }

    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

