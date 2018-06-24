package com.platform.organization.service;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.pojo.OrgUser;

public interface OrgUserService {

	public OrgUser getUser(String id);
	
	public OrgUser getUserByLoginName(String loginName);
	
	/**
	 * 用户查询
	 * @param userName
	 * @param loginName
	 * @param departmentId
	 * @param isContainChildDept
	 * @return
	 */
	public List<OrgUserBo> queryUsers(String userName,String loginName,String departmentId);
	
	/**
	 * 查询注册用户数
	 * @return
	 */
	public int getRegisterUserCount();
	
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
	
	public OrgUser saveUser(OrgUser user, String roleIds);
	
	public boolean delUser(String id);
	
	/**
	 * 获取指定公司的客服人员列表
	 * @param companyId
	 * @return
	 */
	public List<OrgUserBo> getAdminUserList(String companyId);
}
