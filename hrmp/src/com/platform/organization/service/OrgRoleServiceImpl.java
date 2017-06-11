package com.platform.organization.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgRoleBo;
import com.platform.organization.dao.OrgRoleDao;
import com.platform.organization.dao.OrgRoleMenuDao;
import com.platform.organization.dao.OrgUserRoleDao;
import com.platform.organization.pojo.OrgRole;

public class OrgRoleServiceImpl implements OrgRoleService {
	private final Logger logger = Logger.getLogger(OrgRoleServiceImpl.class);
	
	@Autowired
	private OrgRoleDao orgRoleDao;
	@Autowired
	private OrgUserRoleDao orgUserRoleDao;
	@Autowired
	private OrgRoleMenuDao orgRoleMenuDao;
	
	
	public OrgRoleDao getOrgRoleDao() {
		return orgRoleDao;
	}

	public void setOrgRoleDao(OrgRoleDao orgRoleDao) {
		this.orgRoleDao = orgRoleDao;
	}

	public OrgUserRoleDao getOrgUserRoleDao() {
		return orgUserRoleDao;
	}

	public void setOrgUserRoleDao(OrgUserRoleDao orgUserRoleDao) {
		this.orgUserRoleDao = orgUserRoleDao;
	}

	public OrgRoleMenuDao getOrgRoleMenuDao() {
		return orgRoleMenuDao;
	}

	public void setOrgRoleMenuDao(OrgRoleMenuDao orgRoleMenuDao) {
		this.orgRoleMenuDao = orgRoleMenuDao;
	}

	@Override
	public OrgRole getRole(String id) {
		return orgRoleDao.getRole(id);
	}

	@Override
	public List<OrgRole> getRoleListByUserId(String userId) {
		return orgRoleDao.getRoleListByUserId(userId);
	}

	@Override
	public OrgRole saveRole(OrgRole role) {
		return orgRoleDao.saveRole(role);
	}

	@Override
	public boolean delRole(String id) {
		orgRoleMenuDao.deleteRoleMenuByRoleId(id);
		orgUserRoleDao.deleteUserRoleByRoleId(id);
		return orgRoleDao.delRole(id);
	}

	@Override
	public List<OrgRoleBo> queryRoles(String roleName, String departmentId,
			boolean isContainParentDept, boolean isContainChildDept) {
		return orgRoleDao.queryRoles(roleName, departmentId, isContainParentDept, isContainChildDept);
	}

	@Override
	public Page queryRoles(String roleName, String departmentId,
			boolean isContainParentDept, boolean isContainChildDept, Page page) {
		return orgRoleDao.queryRoles(roleName, departmentId, isContainParentDept, isContainChildDept,page);
	}

	@Override
	public OrgRoleBo getRoleBo(String id) {
		return orgRoleDao.getRoleBo(id);
	}

}
