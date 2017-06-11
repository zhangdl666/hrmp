package com.platform.organization.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.organization.bo.OrgRoleMenuBo;
import com.platform.organization.pojo.OrgMenu;
import com.platform.organization.pojo.OrgRole;
import com.platform.organization.pojo.OrgRoleMenu;
import com.platform.organization.pojo.OrgUserRole;

public class OrgRoleMenuDaoImpl implements OrgRoleMenuDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List<OrgRoleMenuBo> getRoleMenuListByRoleId(String roleId) {
		String hql = "select rm.id,rm.createTime,r,m from OrgRoleMenu rm,OrgRole r,OrgMenu m where rm.roleId = r.id and rm.menuId = m.id and rm.roleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, roleId);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<OrgRoleMenuBo> result = new ArrayList<OrgRoleMenuBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = (Object[])list.get(i);
			OrgRoleMenuBo bo = new OrgRoleMenuBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setCreateTime(o[1]==null?null:(Date)o[1]);
			bo.setRole(o[2]==null?null:(OrgRole)o[2]);
			bo.setMenu(o[3]==null?null:(OrgMenu)o[3]);
			result.add(bo);
		}
		return result;
	}
	@Override
	public List<OrgRoleMenuBo> getRoleMenuListByMenuId(String menuId) {
		String hql = "select rm.id,rm.createTime,r,m from OrgRoleMenu rm,OrgRole r,OrgMenu m where rm.roleId = r.id and rm.menuId = m.id and rm.menuId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, menuId);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<OrgRoleMenuBo> result = new ArrayList<OrgRoleMenuBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = (Object[])list.get(i);
			OrgRoleMenuBo bo = new OrgRoleMenuBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setCreateTime(o[1]==null?null:(Date)o[1]);
			bo.setRole(o[2]==null?null:(OrgRole)o[2]);
			bo.setMenu(o[3]==null?null:(OrgMenu)o[3]);
			result.add(bo);
		}
		return result;
	}
	@Override
	public OrgRoleMenu saveRoleMenu(OrgRoleMenu rm) {
		sessionFactory.getCurrentSession().saveOrUpdate(rm);
		return rm;
	}
	@Override
	public boolean deleteRoleMenu(String id) {
		if(id == null || "".equals(id)) {
			return false;
		}
		String hql = "delete OrgRoleMenu rm where rm.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate()>0);
	}
	@Override
	public int deleteRoleMenuByRoleId(String roleId) {
		if(roleId == null || "".equals(roleId)) {
			return 0;
		}
		String hql = "delete OrgRoleMenu rm where rm.roleId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, roleId);
		
		return query.executeUpdate();
	}
	@Override
	public int deleteRoleMenuByMenuId(String menuId) {
		if(menuId == null || "".equals(menuId)) {
			return 0;
		}
		String hql = "delete OrgRoleMenu rm where rm.menuId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, menuId);
		
		return query.executeUpdate();
	}
	@Override
	public OrgRoleMenu getOrgRoleMenu(String roleId, String menuId) {
		String hql = "from OrgRoleMenu rm where rm.roleId=? and rm.menuId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, roleId);
		query.setString(1, menuId);
		List list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		return (OrgRoleMenu)list.get(0);
	}

}
