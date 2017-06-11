package com.platform.organization.service;

import java.util.List;

import com.platform.organization.bo.OrgRoleMenuBo;
import com.platform.organization.pojo.OrgRoleMenu;

public interface OrgRoleMenuService {

	public List<OrgRoleMenuBo> getRoleMenuListByRoleId(String roleId);

	public List<OrgRoleMenuBo> getRoleMenuListByMenuId(String menuId);

	public OrgRoleMenu saveRoleMenu(OrgRoleMenu rm);
	
	public OrgRoleMenu getOrgRoleMenu(String roleId,String menuId);

	public boolean deleteRoleMenu(String id);

	public int deleteRoleMenuByRoleId(String roleId);
	
	public int deleteRoleMenuByMenuId(String menuId);
}
