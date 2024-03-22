package org.jeecg.modules.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 预约状态
 */
public enum AppointStatusEnum {

    /**登录短信模板编码*/
	LOGIN_TEMPLATE_CODE(0,"未预约"),
    /**忘记密码短信模板编码*/
	FORGET_PASSWORD_TEMPLATE_CODE(0,"预约中");


	private Integer Status;

	private String msg;

	private AppointStatusEnum(Integer Status, String msg) {
		this.Status = Status;
		this.msg = msg;
	}


	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

