package com.platform.business.dao;

import java.util.List;

import com.platform.business.pojo.BusinessOpinion;

public interface BusinessOpinionDao {

	public BusinessOpinion saveBusinessOpinion(BusinessOpinion op);
	
	public List<BusinessOpinion> getBusinessOpinionList(String businessId);
}
