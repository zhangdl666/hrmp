package com.platform.organization.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.platform.organization.bo.OrgRoleMenuBo;
import com.platform.organization.dao.OrgRoleMenuDao;
import com.platform.organization.pojo.OrgRoleMenu;

public class OrgRoleMenuServiceImpl implements OrgRoleMenuService {

	@Autowired
	private OrgRoleMenuDao orgRoleMenuDao;
	
	
	public OrgRoleMenuDao getOrgRoleMenuDao() {
		return orgRoleMenuDao;
	}

	public void setOrgRoleMenuDao(OrgRoleMenuDao orgRoleMenuDao) {
		this.orgRoleMenuDao = orgRoleMenuDao;
	}

	@Override
	public List<OrgRoleMenuBo> getRoleMenuListByRoleId(String roleId) {
		return orgRoleMenuDao.getRoleMenuListByRoleId(roleId);
	}

	@Override
	public List<OrgRoleMenuBo> getRoleMenuListByMenuId(String menuId) {
		return orgRoleMenuDao.getRoleMenuListByMenuId(menuId);
	}

	@Override
	public OrgRoleMenu saveRoleMenu(OrgRoleMenu rm) {
		return orgRoleMenuDao.saveRoleMenu(rm);
	}

	@Override
	public boolean deleteRoleMenu(String id) {
		return orgRoleMenuDao.deleteRoleMenu(id);
	}

	@Override
	public int deleteRoleMenuByRoleId(String roleId) {
		return orgRoleMenuDao.deleteRoleMenuByRoleId(roleId);
	}

	@Override
	public int deleteRoleMenuByMenuId(String menuId) {
		return orgRoleMenuDao.deleteRoleMenuByMenuId(menuId);
	}

	@Override
	public OrgRoleMenu getOrgRoleMenu(String roleId, String menuId) {
		return orgRoleMenuDao.getOrgRoleMenu(roleId, menuId);
	}

}
