package com.platform.business.service;

import java.util.List;

import com.platform.business.pojo.BusinessOpinion;
import com.platform.organization.pojo.OrgUser;

public interface BusinessOpinionService {

	public BusinessOpinion saveBusinessOpinion(BusinessOpinion op);

	public List<BusinessOpinion> getBusinessOpinionList(String businessId);
	
	public BusinessOpinion saveBusinessOpinion(String businessId,OrgUser user,String operator,String message);
}
