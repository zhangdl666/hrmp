package com.platform.business.service;

import com.platform.business.dao.BusinessUnitPriceDao;
import com.platform.business.pojo.UnitPrice;

public class BusinessUnitPriceServiceImpl implements BusinessUnitPriceService{

	private BusinessUnitPriceDao businessUnitPriceDao;
	
	public BusinessUnitPriceDao getBusinessUnitPriceDao() {
		return businessUnitPriceDao;
	}

	public void setBusinessUnitPriceDao(BusinessUnitPriceDao businessUnitPriceDao) {
		this.businessUnitPriceDao = businessUnitPriceDao;
	}

	@Override
	public UnitPrice saveUnitPrice(UnitPrice up) {
		return businessUnitPriceDao.saveUnitPrice(up);
	}

	@Override
	public UnitPrice getUnitPrice(String deptId, String empTypeId) {
		return businessUnitPriceDao.getUnitPrice(deptId, empTypeId);
	}

}
