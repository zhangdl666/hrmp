package com.platform.organization.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.organization.bo.OrgRoleMenuBo;
import com.platform.organization.bo.OrgUserRoleBo;
import com.platform.organization.pojo.OrgMenu;
import com.platform.organization.pojo.OrgRole;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.pojo.OrgUserRole;

public class OrgUserRoleDaoImpl implements OrgUserRoleDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List<OrgUserRoleBo> getUserRoleListByUserId(String userId) {
		String hql = "select ur.id,ur.createTime,u,r from OrgUserRole ur,OrgUser u,OrgRole r where ur.userId=u.id and ur.roleId = r.id and ur.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<OrgUserRoleBo> result = new ArrayList<OrgUserRoleBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = (Object[])list.get(i);
			OrgUserRoleBo bo = new OrgUserRoleBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setCreateTime(o[1]==null?null:(Date)o[1]);
			bo.setUser(o[2]==null?null:(OrgUser)o[2]);
			bo.setRole(o[3]==null?null:(OrgRole)o[3]);
			result.add(bo);
		}
		return result;
	}

	@Override
	public List<OrgUserRoleBo> getUserRoleListByRoleId(String roleId) {
		String hql = "select ur.id,ur.createTime,u,r from OrgUserRole ur,OrgUser u,OrgRole r where ur.userId=u.id and ur.roleId = r.id and ur.roleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, roleId);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<OrgUserRoleBo> result = new ArrayList<OrgUserRoleBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = (Object[])list.get(i);
			OrgUserRoleBo bo = new OrgUserRoleBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setCreateTime(o[1]==null?null:(Date)o[1]);
			bo.setUser(o[2]==null?null:(OrgUser)o[2]);
			bo.setRole(o[3]==null?null:(OrgRole)o[3]);
			result.add(bo);
		}
		return result;
	}

	@Override
	public OrgUserRole saveUserRole(OrgUserRole ur) {
		sessionFactory.getCurrentSession().saveOrUpdate(ur);
		return ur;
	}

	@Override
	public boolean deleteUserRole(String id) {
		if(id == null || "".equals(id)) {
			return false;
		}
		String hql = "delete OrgUserRole ur where ur.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate()>0);
	}

	@Override
	public int deleteUserRoleByUserId(String userId) {
		if(userId == null || "".equals(userId)) {
			return 0;
		}
		String hql = "delete OrgUserRole ur where ur.userId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		
		return query.executeUpdate();
	}

	@Override
	public int deleteUserRoleByRoleId(String roleId) {
		if(roleId == null || "".equals(roleId)) {
			return 0;
		}
		String hql = "delete OrgUserRole ur where ur.roleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, roleId);
		
		return query.executeUpdate();
	}
	@Override
	public OrgUserRole getOrgUserRole(String userId, String roleId) {
		String hql = "from OrgUserRole ur where ur.userId=? and ur.roleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userId);
		query.setString(1, roleId);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		return (OrgUserRole)list.get(0);
	}

}
