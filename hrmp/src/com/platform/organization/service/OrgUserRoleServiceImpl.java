package com.platform.organization.service;

import java.util.List;

import com.platform.organization.bo.OrgUserRoleBo;
import com.platform.organization.dao.OrgUserRoleDao;
import com.platform.organization.pojo.OrgUserRole;

public class OrgUserRoleServiceImpl implements OrgUserRoleService {

	private OrgUserRoleDao orgUserRoleDao;
	
	public OrgUserRoleDao getOrgUserRoleDao() {
		return orgUserRoleDao;
	}

	public void setOrgUserRoleDao(OrgUserRoleDao orgUserRoleDao) {
		this.orgUserRoleDao = orgUserRoleDao;
	}

	@Override
	public List<OrgUserRoleBo> getUserRoleListByUserId(String userId) {
		return orgUserRoleDao.getUserRoleListByUserId(userId);
	}

	@Override
	public List<OrgUserRoleBo> getUserRoleListByRoleId(String roleId) {
		return orgUserRoleDao.getUserRoleListByRoleId(roleId);
	}

	@Override
	public OrgUserRole saveUserRole(OrgUserRole ur) {
		return orgUserRoleDao.saveUserRole(ur);
	}

	@Override
	public boolean deleteUserRole(String id) {
		return orgUserRoleDao.deleteUserRole(id);
	}

	@Override
	public int deleteUserRoleByUserId(String userId) {
		return orgUserRoleDao.deleteUserRoleByUserId(userId);
	}

	@Override
	public int deleteUserRoleByRoleId(String roleId) {
		return orgUserRoleDao.deleteUserRoleByRoleId(roleId);
	}

	@Override
	public OrgUserRole getOrgUserRole(String userId, String roleId) {
		return orgUserRoleDao.getOrgUserRole(userId, roleId);
	}

}
