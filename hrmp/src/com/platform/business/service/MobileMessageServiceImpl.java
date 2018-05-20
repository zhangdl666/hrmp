package com.platform.business.service;

import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.platform.business.dao.MobileMessageDao;
import com.platform.business.pojo.MMobileMessage;

public class MobileMessageServiceImpl implements MobileMessageService {
	private final Logger logger = Logger.getLogger(MobileMessageServiceImpl.class);

	/**
	 * 腾讯云短信发送
	 */
	private int tencentAppId = 1400093931;
	private String tencentAppKey = "9a71aeb6e74f60ab67dd2939d07a2957";
	public static final int registeSmsTemplate = 123384;
	public static final int pwdSmsTemplate = 123385;
	
	private MobileMessageDao mobileMessageDao;
	@Override
	public String sendRegiteValidateCode(String mobile,String[] params) throws Exception {
	    SmsSingleSender ssender = new SmsSingleSender(tencentAppId, tencentAppKey);
	    SmsSingleSenderResult result = ssender.sendWithParam("86", mobile, registeSmsTemplate, params, "", "", "");
	    //保存入库
	    MMobileMessage mobileMessage = new MMobileMessage();
	    mobileMessage.setMobile(mobile);
	    mobileMessage.setContent(registeSmsTemplate + "");
	    mobileMessage.setCreateTime(Calendar.getInstance().getTime());
	    mobileMessage.setRemark(result.toString());
	    mobileMessageDao.saveMobileMessage(mobileMessage);
	    
	    return result.toString();
	}
	
	public MobileMessageDao getMobileMessageDao() {
		return mobileMessageDao;
	}

	public void setMobileMessageDao(MobileMessageDao mobileMessageDao) {
		this.mobileMessageDao = mobileMessageDao;
	}

	@Override
	public String sendPwdValidateCode(String mobile, String[] params) throws Exception {
		SmsSingleSender ssender = new SmsSingleSender(tencentAppId, tencentAppKey);
	    SmsSingleSenderResult result = ssender.sendWithParam("86", mobile, pwdSmsTemplate, params, "", "", "");
	    //保存入库
	    MMobileMessage mobileMessage = new MMobileMessage();
	    mobileMessage.setMobile(mobile);
	    mobileMessage.setContent(pwdSmsTemplate + "");
	    mobileMessage.setCreateTime(Calendar.getInstance().getTime());
	    mobileMessage.setRemark(result.toString());
	    mobileMessageDao.saveMobileMessage(mobileMessage);
	    
	    return result.toString();
	}

}
