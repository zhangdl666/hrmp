package com.platform.business.dao;

import com.platform.business.pojo.MIdentifyCode;

public interface BusinessMIdentifyCodeDao {

	public MIdentifyCode saveMIdentifyCode(MIdentifyCode mIdentifyCode);
	
	public MIdentifyCode getMIdentifyCode(String id);
	
	public MIdentifyCode getMIdentifyCodeByMobile(String mobile,String identifyCodeType);
	
	public int deleteMIdentifyCodeByMobile(String mobile,String identifyCodeType);
	
	public MIdentifyCode generateMIdentifyCode(String mobile,String identifyCodeType);
	
}
