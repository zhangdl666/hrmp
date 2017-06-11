package com.platform.business.service;

import java.util.Calendar;
import java.util.List;

import com.platform.business.dao.BusinessOpinionDao;
import com.platform.business.pojo.BusinessOpinion;
import com.platform.organization.pojo.OrgUser;

public class BusinessOpinionServiceImpl implements BusinessOpinionService {

	private BusinessOpinionDao businessOpinionDao;
	
	public BusinessOpinionDao getBusinessOpinionDao() {
		return businessOpinionDao;
	}

	public void setBusinessOpinionDao(BusinessOpinionDao businessOpinionDao) {
		this.businessOpinionDao = businessOpinionDao;
	}

	@Override
	public BusinessOpinion saveBusinessOpinion(BusinessOpinion op) {
		return businessOpinionDao.saveBusinessOpinion(op);
	}

	@Override
	public List<BusinessOpinion> getBusinessOpinionList(String businessId) {
		return businessOpinionDao.getBusinessOpinionList(businessId);
	}

	@Override
	public BusinessOpinion saveBusinessOpinion(String businessId, OrgUser user,
			String operator, String message) {
		BusinessOpinion op = new BusinessOpinion();
		op.setBusinessId(businessId);
		op.setCreateTime(Calendar.getInstance().getTime());
		op.setUserId(user.getId());
		op.setUserName(user.getUserName());
		op.setOperater(operator);
		op.setMessage(message);
		return businessOpinionDao.saveBusinessOpinion(op);
	}

}
