package com.platform.organization.service;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgDept;

public interface OrgDeptService {

	public OrgDept getOrgDept(String id);

	public OrgDept saveDept(OrgDept dept);

	public boolean delDept(String id);

	/**
	 * 部门查询
	 * @param deptName 部门名称
	 * @param parentDeptId 父部门id
	 * @return
	 */
	public List<OrgDept> queryDepts(String deptName,String parentDeptId);
	
	/**
	 * 部门分页查询
	 * @param deptName 部门名称
	 * @param parentDeptId 父部门id
	 * @param page 分页对象
	 * @return
	 */
	public Page queryDepts(String deptName,String parentDeptId,Page page);
	
	/**
	 * 获取指定部门的直属公司，若deptID为公司ID，则返回本公司
	 * @param deptId
	 * @return
	 */
	public OrgDept getDirectCompany(String deptId);
}
