package com.platform.core.dao;

import com.platform.core.pojo.SysConfig;

public interface SysConfigDao {

	public SysConfig getSysConfig(String id);
	
	public SysConfig getSysConfigByCFGId(String cfgId);
	
}
