package com.platform.business.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.pojo.Advertisement;
import com.platform.business.pojo.WorkSign;
import com.platform.core.bo.Page;
import com.platform.organization.dao.OrgDeptDao;
import com.platform.organization.dao.OrgUserDao;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgUser;

public class AdvertisementDaoImpl implements AdvertisementDao {

	@Autowired
	private SessionFactory sessionFactory;
	private OrgUserDao orgUserDao;
	private OrgDeptDao orgDeptDao;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public OrgUserDao getOrgUserDao() {
		return orgUserDao;
	}

	public void setOrgUserDao(OrgUserDao orgUserDao) {
		this.orgUserDao = orgUserDao;
	}

	public OrgDeptDao getOrgDeptDao() {
		return orgDeptDao;
	}

	public void setOrgDeptDao(OrgDeptDao orgDeptDao) {
		this.orgDeptDao = orgDeptDao;
	}

	@Override
	public Advertisement getAdvertisement(String id) {
		String hql = "from Advertisement d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);

		return (Advertisement) query.uniqueResult();
	}
	
	@Override
	public void click(String id) {
		String hql = "update Advertisement d set d.clickCount = d.clickCount + 1 where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		query.executeUpdate();

	}

	@Override
	public Advertisement saveAdvertisement(Advertisement adver) {
		if(adver.getBusinessNumber()==null) {
			int num = generateBusinessNum();
			adver.setBusinessNumber(num);
		}
		sessionFactory.getCurrentSession().saveOrUpdate(adver);
		return adver;
	}
	
	private int generateBusinessNum() {
		String hql = "select max(businessNumber) from Advertisement a";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		Number num = (Number) query.uniqueResult();
		if(num==null) {
			return 1;
		}
		return num.intValue() + 1;
	}

	@Override
	public Page queryMyAdvertisementList(String userId,
			Advertisement advertisement,String keyword, Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append("select d from Advertisement d,OrgUser o where d.publisherId = o.id and d.validStatus = '1' ");
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		
		sb.append(" and o.loginName = ?");
		params.put(paramIndex, userId);
		paramIndex = paramIndex + 1;
		
		if (keyword != null && !keyword.equals("")) {
			sb.append(" and (d.title like ? or d.content like ? or d.contactUserPhone like ? or d.contactUser like ? )");
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by d.createTime desc ");
		String hql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParameter(query, params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<Advertisement> list = query.list();

		page.setResult(list);

		// 取记录总数
		String countSql = "select count(d) " + hql.substring(hql.indexOf(" from "));
		Query countQuery = sessionFactory.getCurrentSession().createQuery(
				countSql);
		setQueryParameter(countQuery, params);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());

		return page;
	}

	@Override
	public Page queryAdvertisementList(String loginName,Advertisement advertisement,
			String keyword, Page page) {
		OrgUser loginUser = orgUserDao.getUserByLoginName(loginName);
		if(loginUser == null) {
			return null;
		}
		
		OrgDept company = orgDeptDao.getDirectCompany(loginUser.getDeptId());
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("from Advertisement d where d.validStatus = '1' and d.isClosed = '0' and payStatus = '1' ");
		sb.append(" and d.publisherCompanyId = '" + company.getId() + "'");
		
		if (keyword != null && !keyword.equals("")) {
			sb.append(" and (d.title like ? or d.content like ? or d.contactUserPhone like ? or d.contactUser like ?) ");
			params.put(paramIndex, "%" + keyword + "%");
			paramIndex = paramIndex + 1;
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
			params.put(paramIndex,  "%" + keyword + "%");
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by d.createTime desc ");
		String hql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParameter(query, params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<Advertisement> list = query.list();

		page.setResult(list);

		// 取记录总数
		String countSql = "select count(d) " + hql;
		Query countQuery = sessionFactory.getCurrentSession().createQuery(
				countSql);
		setQueryParameter(countQuery, params);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());

		return page;
	}

	// 设置query参数
	private void setQueryParameter(Query query, Map<Integer, Object> paraMap) {
		if (paraMap == null || paraMap.isEmpty()) {
			return;
		}

		Set<Integer> keys = paraMap.keySet();
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			query.setParameter(key, paraMap.get(key));
		}
	}

	@Override
	public List<Advertisement> getNoPayList() {
		String hql = "from Advertisement d where d.validStatus = '1' and payStatus = '0'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<Advertisement> list = query.list();
		return list;
	}

	@Override
	public void closeOverTimeAdvertisement() {
		String hql = "update Advertisement d set d.isClosed = '1',d.closeTime = now(),d.remark='到期自动关闭' "
				+ "where d.validStatus = '1' and d.isClosed = '0' and d.empDate < date_sub(NOW(),interval a.months month)";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.executeUpdate();
	}
	
	@Override
	public void deleteAdvertisement(Advertisement adver) {
		if(adver==null ||adver.getId()==null) {
			return;
		}
		sessionFactory.getCurrentSession().delete(adver);
	}

}
