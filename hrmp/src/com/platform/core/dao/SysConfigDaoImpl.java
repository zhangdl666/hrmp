package com.platform.core.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.pojo.Message;
import com.platform.core.bo.Page;
import com.platform.core.pojo.SysConfig;
import com.platform.organization.pojo.OrgUser;

public class SysConfigDaoImpl implements SysConfigDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public SysConfig getSysConfig(String id) {
		String hql = "from SysConfig u where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (SysConfig)query.uniqueResult();
	}

	@Override
	public SysConfig getSysConfigByCFGId(String cfgId) {
		String hql = "from SysConfig u where u.state = 1 and u.cfgId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, cfgId);
		
		return (SysConfig)query.uniqueResult();
	}

}
