package com.platform.core.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.pojo.LogInfo;

public class LogInfoDaoImpl implements LogInfoDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public LogInfo saveLogInfo(LogInfo loginfo) {
		sessionFactory.getCurrentSession().saveOrUpdate(loginfo);
		return loginfo;
	}

}
