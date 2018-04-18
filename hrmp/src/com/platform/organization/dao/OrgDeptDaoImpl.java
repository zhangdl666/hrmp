package com.platform.organization.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgDept;

public class OrgDeptDaoImpl implements OrgDeptDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public OrgDept getOrgDept(String id) {
		String hql = "from OrgDept d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (OrgDept)query.uniqueResult();
	}
	
	@Override
	public OrgDept saveDept(OrgDept dept) {
		sessionFactory.getCurrentSession().saveOrUpdate(dept);
		return dept;
	}

	@Override
	public boolean delDept(String id) {
		String hql = "update OrgDept d set d.validstatus = 0 where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public List<OrgDept> queryDepts(String deptName,String parentDeptId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select dept from OrgDept dept ");
		sb.append(" where dept.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(deptName != null && !"".equals(deptName)){
			sb.append(" and dept.deptName like ?");
			params.put(paramIndex, "%" + deptName + "%");
			paramIndex = paramIndex + 1;
		}
		if(parentDeptId != null && !"".equals(parentDeptId)){
			sb.append("    and dept.parentId = ? ");
			params.put(paramIndex, parentDeptId);
			paramIndex = paramIndex + 1;
			
		}
		sb.append(" order by dept.deptName");
		String sql = sb.toString();
		
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		return query.list();
	}
	
	@Override
	public Page queryDepts(String deptName,String parentDeptId,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select dept from OrgDept dept ");
		sb.append(" where dept.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(deptName != null && !"".equals(deptName)){
			sb.append(" and dept.deptName like ?");
			params.put(paramIndex, "%" + deptName + "%");
			paramIndex = paramIndex + 1;
		}
		if(parentDeptId != null && !"".equals(parentDeptId)){
			sb.append("    and dept.parentId = ? ");
			params.put(paramIndex, parentDeptId);
			paramIndex = paramIndex + 1;
			
		}
		sb.append(" order by dept.deptName");
		String sql = sb.toString();
		
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			page.setTotalRowSize(0);
			return page;
		}
		
		page.setResult(list);
		
		//取记录总数
		String countSql = "select count(dept) " + sql.substring(sql.indexOf("from") - 1);
		Query countQuery = sessionFactory.getCurrentSession().createQuery(countSql);
		setQueryParameter(countQuery, params);
		Number count = (Number)countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}
	
	// 设置query参数
	private void setQueryParameter(Query query, Map<Integer, String> paraMap) {
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
	public OrgDept getDirectCompany(String deptId) {
		
		OrgDept dept = getOrgDept(deptId);
		if(dept == null) {
			 return null;
		}
		
		if(dept.getDeptFlag().equals("com")) {
			return dept;
		}
		
		int i=0;//最多循环10次，避免死循环
		while(true) {
			i++;
			OrgDept parentDept = getOrgDept(dept.getParentId());
			if(parentDept == null) {
				return null;
			}
			if(parentDept.getDeptFlag().equals("com")) {
				return parentDept;
			}
			if(i>10) {
				return null;
			}
		}
	}
	
}
