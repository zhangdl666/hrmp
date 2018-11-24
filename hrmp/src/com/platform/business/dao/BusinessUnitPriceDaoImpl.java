package com.platform.business.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.pojo.UnitPrice;

public class BusinessUnitPriceDaoImpl implements BusinessUnitPriceDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public UnitPrice saveUnitPrice(UnitPrice up) {
		sessionFactory.getCurrentSession().save(up);
		return up;
	}

	@Override
	public UnitPrice getUnitPrice(String deptId, String empTypeId) {
		String hql = "from UnitPrice b where b.deptId = ? and b.empTypeId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, deptId);
		query.setString(1, empTypeId);
		
		List<UnitPrice> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		return list.get(0);
	}

	@Override
	public UnitPrice getAdvertisementUnitPrice(String deptId, String months) {
		String hql = "from UnitPrice b where b.deptId = ? and b.empTypeId = 'ADVER' and months = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, deptId);
		query.setString(1, months);
		
		List<UnitPrice> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		return list.get(0);
	}
	
	@Override
	public List<UnitPrice> getAdvertisementUnitPriceList(String deptId) {
		String hql = "from UnitPrice b where b.deptId = ? and b.empTypeId = 'ADVER'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, deptId);
		
		List<UnitPrice> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		return list;
	}
}
