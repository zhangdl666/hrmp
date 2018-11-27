package com.platform.business.dao;

import java.util.List;

import com.platform.business.pojo.UnitPrice;

public interface BusinessUnitPriceDao {

	public UnitPrice saveUnitPrice(UnitPrice up);
	
	public UnitPrice getUnitPrice(String deptId,String empTypeId);
	
	public UnitPrice getAdvertisementUnitPrice(String deptId, String months);
	
	public List<UnitPrice> getAdvertisementUnitPriceList(String deptId);
	
	public int getFreePublistCount(String userId);
}
