package com.platform.business.service;

import java.io.IOException;

import org.json.JSONException;

import com.github.qcloudsms.httpclient.HTTPException;

public interface MobileMessageService {

	/**
	 * 发送注册验证吗
	 * @param mobile
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	public String sendRegiteValidateCode(String mobile,String[] params) throws Exception;
	
	/**
	 * 发送找回密码验证吗
	 * @param mobile
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws HTTPException 
	 * @throws Exception
	 */
	public String sendPwdValidateCode(String mobile,String[] params) throws Exception;
}
