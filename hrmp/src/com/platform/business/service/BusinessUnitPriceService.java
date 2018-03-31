package com.platform.business.service;

import com.platform.business.pojo.UnitPrice;

public interface BusinessUnitPriceService {

	public UnitPrice saveUnitPrice(UnitPrice up);
	
	public UnitPrice getUnitPrice(String deptId,String empTypeId);
}
