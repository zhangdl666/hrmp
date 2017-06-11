package com.platform.core.service;

import com.platform.core.dao.LogInfoDao;
import com.platform.core.pojo.LogInfo;

public class LogInfoServiceImpl implements LogInfoService {

	private LogInfoDao logInfoDao;
	
	
	public LogInfoDao getLogInfoDao() {
		return logInfoDao;
	}


	public void setLogInfoDao(LogInfoDao logInfoDao) {
		this.logInfoDao = logInfoDao;
	}


	@Override
	public LogInfo saveLogInfo(LogInfo loginfo) {
		return logInfoDao.saveLogInfo(loginfo);
	}

}
