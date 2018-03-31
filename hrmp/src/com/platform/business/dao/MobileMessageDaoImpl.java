package com.platform.business.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.pojo.MMobileMessage;

public class MobileMessageDaoImpl implements MobileMessageDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public MMobileMessage saveMobileMessage(MMobileMessage message) {
		sessionFactory.getCurrentSession().save(message);
		return message;
	}

	@Override
	public MMobileMessage getMobileMessage(String id) {
		String hql = "from MMobileMessage d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		List<MMobileMessage> list = query.list();
		if(list == null || list.size() ==0) {
			return null;
		}
		return list.get(0);
	}

}
