package com.platform.organization.dao;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgRoleBo;
import com.platform.organization.pojo.OrgRole;

public interface OrgRoleDao {

	public OrgRole getRole(String id);
	
	public OrgRoleBo getRoleBo(String id);
	
	/**
	 * 角色查询
	 * @param roleName 角色名称
	 * @param departmentId 部门id
	 * @param isContainParentDept 是否查询父部门下的角色
	 * @param isContainChildDept 是否查询子部门下的角色
	 * @return
	 */
	public List<OrgRoleBo> queryRoles(String roleName,String departmentId);
	
	/**
	 * 角色分页查询
	 * @param roleName 角色名称
	 * @param departmentId 部门id
	 * @param isContainParentDept 是否查询父部门下的角色
	 * @param isContainChildDept 是否查询子部门下的角色
	 * @param page 分页
	 * @return
	 */
	public Page queryRoles(String roleName,String departmentId,Page page);
	
	public List<OrgRole> getRoleListByUserId(String userId);

	public OrgRole saveRole(OrgRole role);
	
	public boolean delRole(String id);
	
}
