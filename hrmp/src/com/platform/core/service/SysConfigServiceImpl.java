package com.platform.core.service;

import com.platform.core.dao.SysConfigDao;
import com.platform.core.pojo.SysConfig;

public class SysConfigServiceImpl implements SysConfigService {

	private SysConfigDao sysConfigDao;
	public SysConfigDao getSysConfigDao() {
		return sysConfigDao;
	}

	public void setSysConfigDao(SysConfigDao sysConfigDao) {
		this.sysConfigDao = sysConfigDao;
	}

	@Override
	public SysConfig getSysConfig(String id) {
		return sysConfigDao.getSysConfig(id);
	}

	@Override
	public SysConfig getSysConfigByCFGId(String cfgId) {
		return sysConfigDao.getSysConfigByCFGId(cfgId);
	}

}
