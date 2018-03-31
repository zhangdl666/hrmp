package com.platform.business.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.dao.BusinessMIdentifyCodeDao;
import com.platform.business.pojo.MIdentifyCode;

public class BusinessMIdentifyCodeServiceImpl implements BusinessMIdentifyCodeService {

	@Autowired
	private BusinessMIdentifyCodeDao businessMIdentifyCodeDao;
	
	@Override
	public MIdentifyCode saveMIdentifyCode(MIdentifyCode mIdentifyCode) {
		return businessMIdentifyCodeDao.saveMIdentifyCode(mIdentifyCode);
	}

	@Override
	public MIdentifyCode getMIdentifyCode(String id) {
		return businessMIdentifyCodeDao.getMIdentifyCode(id);
	}

	@Override
	public MIdentifyCode getMIdentifyCodeByMobile(String mobile,
			String identifyCodeType) {
		return businessMIdentifyCodeDao.getMIdentifyCodeByMobile(mobile, identifyCodeType);
	}

	@Override
	public int deleteMIdentifyCodeByMobile(String mobile,
			String identifyCodeType) {
		return businessMIdentifyCodeDao.deleteMIdentifyCodeByMobile(mobile, identifyCodeType);
	}

	@Override
	public MIdentifyCode generateMIdentifyCode(String mobile,
			String identifyCodeType) {
		return businessMIdentifyCodeDao.generateMIdentifyCode(mobile, identifyCodeType);
	}

	public BusinessMIdentifyCodeDao getBusinessMIdentifyCodeDao() {
		return businessMIdentifyCodeDao;
	}

	public void setBusinessMIdentifyCodeDao(
			BusinessMIdentifyCodeDao businessMIdentifyCodeDao) {
		this.businessMIdentifyCodeDao = businessMIdentifyCodeDao;
	}

}
