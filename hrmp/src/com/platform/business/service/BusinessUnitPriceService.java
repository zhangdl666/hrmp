package com.platform.business.service;

import java.util.List;

import com.platform.business.pojo.UnitPrice;

public interface BusinessUnitPriceService {

	public UnitPrice saveUnitPrice(UnitPrice up);
	
	public UnitPrice getUnitPrice(String deptId,String empTypeId);
	
	public UnitPrice getAdvertisementUnitPrice(String deptId, String months);
	
	public List<UnitPrice> getAdvertisementUnitPriceList(String deptId);
}
