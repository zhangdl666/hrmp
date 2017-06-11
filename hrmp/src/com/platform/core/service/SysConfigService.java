package com.platform.core.service;

import com.platform.core.pojo.SysConfig;

public interface SysConfigService {
	public SysConfig getSysConfig(String id);

	public SysConfig getSysConfigByCFGId(String cfgId);
}
