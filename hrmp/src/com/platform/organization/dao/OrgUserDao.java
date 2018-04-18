package com.platform.organization.dao;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.pojo.OrgUser;

public interface OrgUserDao {

	public OrgUser getUser(String id);
	
	public OrgUser getUserByLoginName(String loginName);
	
	/**
	 * 用户查询
	 * @param userName
	 * @param loginName
	 * @param departmentId
	 * @return
	 */
	public List<OrgUserBo> queryUsers(String userName,String loginName,String departmentId);
	
	/**
	 * 用户分页查询
	 * @param userName
	 * @param loginName
	 * @param departmentId
	 * @param isContainChildDept
	 * @return
	 */
	public Page queryUsers(String userName,String loginName,String departmentId,Page page);
	
	public List<OrgUser> getUserListByRoleId(String roleId);

	public OrgUser saveUser(OrgUser user);
	
	public boolean delUser(String id);
	
	/**
	 * 获取指定公司的客服人员列表
	 * @param companyId
	 * @return
	 */
	public List<OrgUserBo> getAdminUserList(String companyId);
	
}
