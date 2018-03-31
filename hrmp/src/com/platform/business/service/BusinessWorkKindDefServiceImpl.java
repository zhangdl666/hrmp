package com.platform.business.service;

import java.util.List;

import com.platform.business.dao.BusinessWorkKindDefDao;
import com.platform.business.pojo.WorkKindDef;

public class BusinessWorkKindDefServiceImpl implements BusinessWorkKindDefService {

	private BusinessWorkKindDefDao businessWorkKindDefDao;

	public BusinessWorkKindDefDao getBusinessWorkKindDefDao() {
		return businessWorkKindDefDao;
	}

	public void setBusinessWorkKindDefDao(
			BusinessWorkKindDefDao businessWorkKindDefDao) {
		this.businessWorkKindDefDao = businessWorkKindDefDao;
	}

	@Override
	public WorkKindDef saveWorkKindDef(WorkKindDef wk) {
		return businessWorkKindDefDao.saveWorkKindDef(wk);
	}

	@Override
	public List<WorkKindDef> getWorkKindList() {
		return businessWorkKindDefDao.getWorkKindList();
	}
	
	

}
