package com.platform.business.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.pojo.BusinessOpinion;

public class BusinessOpinionDaoImpl implements BusinessOpinionDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public BusinessOpinion saveBusinessOpinion(BusinessOpinion op) {
		sessionFactory.getCurrentSession().save(op);
		return op;
	}

	@Override
	public List<BusinessOpinion> getBusinessOpinionList(String businessId) {
		String hql = "from BusinessOpinion b where b.businessId = ? order by b.createTime";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, businessId);
		
		return query.list();
	}

}
