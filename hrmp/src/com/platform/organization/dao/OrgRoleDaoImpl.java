package com.platform.organization.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgRoleBo;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgRole;

public class OrgRoleDaoImpl implements OrgRoleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public OrgRole getRole(String id) {
		
		String hql = "from OrgRole r where r.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (OrgRole)query.uniqueResult();
	}
	
	@Override
	public OrgRoleBo getRoleBo(String id) {
		
		String hql = "select r,d from OrgRole r,OrgDeptView d where r.deptId = d.id and r.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		List<Object[]> list = query.list();
		if(list == null || list.size() ==0) {
			return null;
		}
		Object[] obj = list.get(0);
		OrgRole role = (OrgRole)obj[0];
		OrgDept dept = (OrgDept)obj[1];
		
		OrgRoleBo bo = new OrgRoleBo();
		bo.setId(role.getId());
		bo.setRoleName(role.getRoleName());
		bo.setValidstatus(role.getValidstatus());
		bo.setDept(dept);
		
		return bo;
	}
	
	@Override
	public OrgRole saveRole(OrgRole role) {
		sessionFactory.getCurrentSession().saveOrUpdate(role);
		return role;
	}
	

	@Override
	public boolean delRole(String id) {
		
		String hql = "update OrgRole r set r.validstatus = 0 where r.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}


	@Override
	public List<OrgRoleBo> queryRoles(String roleName,String departmentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.id,r.rolename,d.id deptid,d.deptname,d.dept_flag,d.parentid,d.createtime,d.validstatus ");
		sb.append("   from org_role r,v_org_dept d");
		sb.append("  where r.dept_id = d.id and r.validstatus = '1'");
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(roleName != null && !"".equals(roleName)){
			sb.append(" and r.ROLENAME like ?");
			params.put(paramIndex, "%" + roleName + "%");
			paramIndex = paramIndex + 1;
		}
		if(departmentId != null && !"".equals(departmentId)){
			
			sb.append("    and r.dept_id = ? ");
			params.put(paramIndex, departmentId);
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by r.ROLENAME");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		setQueryParameter(query,params);
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		List<OrgRoleBo> result = new ArrayList<OrgRoleBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			OrgRoleBo bo = new OrgRoleBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setRoleName(o[1]==null?null:(String)o[1]);
			
			OrgDept dept = new OrgDept();
			dept.setId(o[2]==null?null:(String)o[2]);
			dept.setDeptName(o[3]==null?null:(String)o[3]);
			dept.setDeptFlag(o[4]==null?null:(String)o[4]);
			dept.setParentId(o[5]==null?null:(String)o[5]);
			dept.setCreateTime(o[6]==null?null:(Date)o[6]);
//			dept.setDeptIdPath(o[7]==null?null:(String)o[7]);
//			dept.setDeptNamePath(o[8]==null?null:(String)o[8]);
			dept.setValidstatus(o[7]==null?null:(String)o[7]);
			
			bo.setDept(dept);
			result.add(bo);
		}
		
		return result;
	}
	
	public Page queryRoles(String roleName,String departmentId,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.id,r.rolename,dept.id deptid,dept.deptname,dept.dept_flag,dept.parentid,dept.createtime,dept.validstatus ");
		sb.append("   from org_role r,org_dept dept");
		sb.append("  where r.dept_id = dept.id and r.validstatus = '1'");
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(roleName != null && !"".equals(roleName)){
			sb.append(" and r.ROLENAME like ?");
			params.put(paramIndex, "%" + roleName + "%");
			paramIndex = paramIndex + 1;
		}
		if(departmentId != null && !"".equals(departmentId)){
			sb.append("    and r.dept_id = ? ");
			params.put(paramIndex, departmentId);
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by r.ROLENAME");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			page.setTotalRowSize(0);
			return page;
		}
		
		List<OrgRoleBo> result = new ArrayList<OrgRoleBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			OrgRoleBo bo = new OrgRoleBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setRoleName(o[1]==null?null:(String)o[1]);
			
			OrgDept dept = new OrgDept();
			dept.setId(o[2]==null?null:(String)o[2]);
			dept.setDeptName(o[3]==null?null:(String)o[3]);
			dept.setDeptFlag(o[4]==null?null:(String)o[4]);
			dept.setParentId(o[5]==null?null:(String)o[5]);
			dept.setCreateTime(o[6]==null?null:(Date)o[6]);
//			dept.setDeptIdPath(o[7]==null?null:(String)o[7]);
//			dept.setDeptNamePath(o[8]==null?null:(String)o[8]);
			dept.setValidstatus(o[7]==null?null:(String)o[7]);
			
			bo.setDept(dept);
			result.add(bo);
		}
		
		page.setResult(result);
		
		//ȡ��¼����
		String countSql = "select count(1) " + sql.substring(sql.indexOf("from") - 1);
		Query countQuery = sessionFactory.getCurrentSession().createSQLQuery(countSql);
		setQueryParameter(countQuery, params);
		Number count = (Number)countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}
	
	// ����query����
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
	public List<OrgRole> getRoleListByUserId(String userId) {
		String hql = "select r from OrgUserRole ur,OrgRole r where ur.roleId = r.id and ur.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		return query.list();
	}

}
