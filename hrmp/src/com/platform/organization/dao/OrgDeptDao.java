package com.platform.organization.dao;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgDeptView;

public interface OrgDeptDao {

	public OrgDept getOrgDept(String id);
	
	public OrgDeptView getOrgDeptView(String id);

	public OrgDept saveDept(OrgDept dept);

	public boolean delDept(String id);

	/**
	 * 部门查询
	 * @param deptName 部门名称
	 * @param parentDeptId 父部门id
	 * @param isContainChildDept 是否查询子部门
	 * @return
	 */
	public List<OrgDeptView> queryDepts(String deptName,String parentDeptId,boolean isContainChildDept);
	
	/**
	 * 部门分页查询
	 * @param deptName 部门名称
	 * @param parentDeptId 父部门id
	 * @param isContainChildDept 是否查询子部门
	 * @param page 分页对象
	 * @return
	 */
	public Page queryDepts(String deptName,String parentDeptId,boolean isContainChildDept,Page page);
	
	/**
	 * 获取指定部门的直属公司，若deptID为公司ID，则返回本公司
	 * @param deptId
	 * @return
	 */
	public OrgDept getDirectCompany(String deptId);
}
