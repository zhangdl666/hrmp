package com.platform.organization.dao;

import java.util.List;

import com.platform.organization.bo.OrgUserRoleBo;
import com.platform.organization.pojo.OrgUserRole;

public interface OrgUserRoleDao {

	public List<OrgUserRoleBo> getUserRoleListByUserId(String userId);

	public List<OrgUserRoleBo> getUserRoleListByRoleId(String roleId);

	public OrgUserRole saveUserRole(OrgUserRole ur);
	
	public OrgUserRole getOrgUserRole(String userId,String roleId);

	public boolean deleteUserRole(String id);

	public int deleteUserRoleByUserId(String userId);
	
	public int deleteUserRoleByRoleId(String roleId);


}
