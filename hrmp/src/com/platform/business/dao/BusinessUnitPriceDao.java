package com.platform.business.dao;

import com.platform.business.pojo.UnitPrice;

public interface BusinessUnitPriceDao {

	public UnitPrice saveUnitPrice(UnitPrice up);
	
	public UnitPrice getUnitPrice(String deptId,String empTypeId);
}
