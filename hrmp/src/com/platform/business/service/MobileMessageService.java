package com.platform.business.service;

public interface MobileMessageService {

	/**
	 * 
	 * @param mobile 手机号码（最多1000个），多个用英文逗号(,)隔开，不可为空
	 * @param content
	 * @return
	 */
	public String sendMessage(String mobile,String content);
}
