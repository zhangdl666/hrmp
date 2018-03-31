package com.platform.organization.dao;

import java.math.BigDecimal;
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

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.pojo.OrgDeptView;
import com.platform.organization.pojo.OrgUser;

public class OrgUserDaoImpl implements OrgUserDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public OrgUser getUser(String id) {
		
		String hql = "from OrgUser u where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (OrgUser)query.uniqueResult();
	}
	
	@Override
	public OrgUser getUserByLoginName(String loginName) {
		String hql = "from OrgUser u where u.validstatus = '1' and u.loginName = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, loginName);
		
		return (OrgUser)query.uniqueResult();
	}


	@Override
	public OrgUser saveUser(OrgUser user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
		return user;
	}
	

	@Override
	public boolean delUser(String id) {
		
		String hql = "update OrgUser u set u.validstatus = 0 where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}


	@Override
	public List<OrgUserBo> queryUsers(String userName,String loginName,String departmentId,boolean isContainChildDept) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {u.*},{dept.*}");
		sb.append("   from org_user u,v_org_dept dept ");
		sb.append("  where u.dept_id = dept.id and u.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(userName != null && !"".equals(userName)){
			sb.append(" and u.username like ?");
			params.put(paramIndex, "%" + userName + "%");
			paramIndex = paramIndex + 1;
		}
		if(loginName != null && !"".equals(loginName)){
			sb.append(" and u.loginname like ?");
			params.put(paramIndex, "%" + loginName + "%");
			paramIndex = paramIndex + 1;
		}
		if(departmentId != null && !"".equals(departmentId)){
			if(isContainChildDept) {
				sb.append("    and u.dept_id in (select d.id");
				sb.append("                        from org_dept d");
				sb.append("                       where d.validstatus = '1'");
				sb.append("                       start with d.id = ?");
				sb.append("                      connect by prior id = parentid)");
			}else {
				sb.append("    and u.dept_id = ? ");
			}
			params.put(paramIndex, departmentId);
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by u.loginname");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("u",OrgUser.class).addEntity("dept",OrgDeptView.class);
		setQueryParameter(query,params);
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		List<OrgUserBo> result = new ArrayList<OrgUserBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			OrgUserBo bo = transToBo((OrgUser)o[0]);
			bo.setDept((OrgDeptView)o[1]);
			result.add(bo);
		}
		
		return result;
	}
	
	@Override
	public Page queryUsers(String userName,String loginName,String departmentId,boolean isContainChildDept,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {u.*},{dept.*}");
		sb.append("   from org_user u,v_org_dept dept ");
		sb.append("  where u.dept_id = dept.id and u.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(userName != null && !"".equals(userName)){
			sb.append(" and u.username like ?");
			params.put(paramIndex, "%" + userName + "%");
			paramIndex = paramIndex + 1;
		}
		if(loginName != null && !"".equals(loginName)){
			sb.append(" and u.loginname like ?");
			params.put(paramIndex, "%" + loginName + "%");
			paramIndex = paramIndex + 1;
		}
		if(departmentId != null && !"".equals(departmentId)){
			if(isContainChildDept) {
				sb.append("    and u.dept_id in (select d.id");
				sb.append("                        from org_dept d");
				sb.append("                       where d.validstatus = '1'");
				sb.append("                       start with d.id = ?");
				sb.append("                      connect by prior id = parentid)");
			}else {
				sb.append("    and u.dept_id = ? ");
			}
			params.put(paramIndex, departmentId);
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by u.loginname");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("u",OrgUser.class).addEntity("dept",OrgDeptView.class);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			return page;
		}
		
		List<OrgUserBo> result = new ArrayList<OrgUserBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			OrgUserBo bo = transToBo((OrgUser)o[0]);
			bo.setDept((OrgDeptView)o[1]);
			result.add(bo);
		}
		page.setResult(result);
		
		//取记录总数
		String countSql = "select count(*) " + sql.substring(sql.indexOf("from") - 1);
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
	private OrgUserBo transToBo(OrgUser user){
		if(user == null) {
			return null;
		}
		OrgUserBo bo = new OrgUserBo();
		bo.setId(user.getId());
		bo.setLoginName(user.getLoginName());
		bo.setUserName(user.getUserName());
		bo.setMobile(user.getMobile());
		//bo.setDeptId(user.getDeptId());
		bo.setCreateTime(user.getCreateTime());
		bo.setPwdUpdateTime(user.getPwdUpdateTime());
		bo.setUserKind(user.getUserKind());
		bo.setRemark(user.getRemark());
		return bo;
	}

	@Override
	public List<OrgUser> getUserListByRoleId(String roleId) {
		String hql = "select r from OrgUserRole ur,OrgUser u where ur.userId = u.id and ur.roleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, roleId);
		return query.list();
	}

	@Override
	public List<OrgUserBo> getAdminUserList(String companyId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {u.*},{dept.*}");
		sb.append("   from org_user u,v_org_dept dept ");
		sb.append("  where u.dept_id = dept.id and u.validstatus = '1'");
		
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		
		sb.append("    and u.dept_id in (select d.id");
		sb.append("                        from org_dept d");
		sb.append("                       where d.validstatus = '1'");
		sb.append("                       start with d.id = ?");
		sb.append("                      connect by prior id = parentid)");
		params.put(paramIndex, companyId);
		paramIndex = paramIndex + 1;
		sb.append(" order by u.loginname");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("u",OrgUser.class).addEntity("dept",OrgDeptView.class);
		setQueryParameter(query,params);
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		List<OrgUserBo> result = new ArrayList<OrgUserBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			OrgUserBo bo = transToBo((OrgUser)o[0]);
			bo.setDept((OrgDeptView)o[1]);
			result.add(bo);
		}
		
		return result;
	}

	

}
