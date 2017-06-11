package com.platform.organization.dao;

import java.math.BigDecimal;
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
import com.platform.organization.pojo.OrgDeptView;

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
	public OrgDeptView getOrgDeptView(String id) {
		String hql = "from OrgDeptView d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (OrgDeptView)query.uniqueResult();
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
	public List<OrgDeptView> queryDepts(String deptName,String parentDeptId,boolean isContainChildDept) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {dept.*} from v_org_dept dept ");
		sb.append(" where dept.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(deptName != null && !"".equals(deptName)){
			sb.append(" and dept.DEPTNAME like ?");
			params.put(paramIndex, "%" + deptName + "%");
			paramIndex = paramIndex + 1;
		}
		if(parentDeptId != null && !"".equals(parentDeptId)){
			if(isContainChildDept) {
				sb.append("    and dept.parentId in (select d.id");
				sb.append("                        from org_dept d");
				sb.append("                       where d.validstatus = '1'");
				sb.append("                       start with d.id = ?");
				sb.append("                      connect by prior id = parentid)");
			}else {
				sb.append("    and dept.parentId = ? ");
			}
			params.put(paramIndex, parentDeptId);
			paramIndex = paramIndex + 1;
			
		}
		sb.append(" order by dept.DEPTNAME");
		String sql = sb.toString();
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("dept",OrgDeptView.class);
		setQueryParameter(query,params);
		return query.list();
	}
	
	@Override
	public Page queryDepts(String deptName,String parentDeptId,boolean isContainChildDept,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {dept.*} from v_org_dept dept ");
		sb.append(" where dept.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(deptName != null && !"".equals(deptName)){
			sb.append(" and dept.DEPTNAME like ?");
			params.put(paramIndex, "%" + deptName + "%");
			paramIndex = paramIndex + 1;
		}
		if(parentDeptId != null && !"".equals(parentDeptId)){
			if(isContainChildDept) {
				sb.append("    and dept.id in (select d.id");
				sb.append("                        from org_dept d");
				sb.append("                       where d.validstatus = '1'");
				sb.append("                       start with d.id = ?");
				sb.append("                      connect by prior id = parentid)");
			}else {
				sb.append("    and dept.id = ? ");
			}
			params.put(paramIndex, parentDeptId);
			paramIndex = paramIndex + 1;
			
		}
		sb.append(" order by dept.DEPTNAME");
		String sql = sb.toString();
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("dept",OrgDeptView.class);
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
		String countSql = "select count(1) " + sql.substring(sql.indexOf("from") - 1);
		Query countQuery = sessionFactory.getCurrentSession().createSQLQuery(countSql);
		setQueryParameter(countQuery, params);
		BigDecimal count = (BigDecimal)countQuery.uniqueResult();
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
		StringBuffer sb = new StringBuffer();
		sb.append(" select {d.*} from org_dept d");
		sb.append(" where d.validstatus = '1' and d.dept_flag = 'com'");
		sb.append(" start with d.id = ? ");
		sb.append(" connect by prior parentid = id");
		sb.append(" and rownum = 1");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("d",OrgDept.class);
		query.setString(0, deptId);
		
		List<OrgDept> list = query.list();
		if(list == null || list.size() ==0) {
			return null;
		}
		
		return list.get(0);
	}

}
