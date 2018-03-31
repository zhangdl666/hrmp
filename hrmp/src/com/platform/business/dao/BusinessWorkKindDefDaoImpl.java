package com.platform.business.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.pojo.WorkKindDef;

public class BusinessWorkKindDefDaoImpl implements BusinessWorkKindDefDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public WorkKindDef saveWorkKindDef(WorkKindDef wk) {
		sessionFactory.getCurrentSession().save(wk);
		return wk;
	}

	@Override
	public List<WorkKindDef> getWorkKindList() {
		String hql = "from WorkKindDef b";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		List<WorkKindDef> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		return list;
	}

}
