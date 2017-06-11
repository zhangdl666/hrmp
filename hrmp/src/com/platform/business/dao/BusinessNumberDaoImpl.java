package com.platform.business.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BusinessNumberDaoImpl implements BusinessNumberDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	private String numberSeed = "00000000";
	private int numberLength = 8;
	private String sequenceName = "SEQ_ID";

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	@Override
	public String getNumber(String start, String sequenceName) {
		String sql = "select " + sequenceName + ".Nextval from dual";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		String num = (String)query.uniqueResult().toString();
		return start + numberSeed.substring(0,numberLength - num.length()) + num;
	}


	@Override
	public String getNumber(String start) {
		return getNumber(start,sequenceName) ;
	}

}
