package com.platform.business.service;

import com.platform.business.dao.BusinessNumberDao;

public class BusinessNumberServiceImpl implements BusinessNumberService {

	private BusinessNumberDao businessNumberDao;
	
	public BusinessNumberDao getBusinessNumberDao() {
		return businessNumberDao;
	}

	public void setBusinessNumberDao(BusinessNumberDao businessNumberDao) {
		this.businessNumberDao = businessNumberDao;
	}

	@Override
	public String getNumber(String start, String sequenceName) {
		return businessNumberDao.getNumber(start, sequenceName);
	}

	@Override
	public String getNumber(String start) {
		return businessNumberDao.getNumber(start);
	}

	
}
