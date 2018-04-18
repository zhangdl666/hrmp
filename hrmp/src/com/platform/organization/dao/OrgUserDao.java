package com.platform.organization.dao;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.pojo.OrgUser;

public interface OrgUserDao {

	public OrgUser getUser(String id);
	
	public OrgUser getUserByLoginName(String loginName);
	
	/**
	 * �û���ѯ
	 * @param userName
	 * @param loginName
	 * @param departmentId
	 * @return
	 */
	public List<OrgUserBo> queryUsers(String userName,String loginName,String departmentId);
	
	/**
	 * �û���ҳ��ѯ
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
	 * ��ȡָ����˾�Ŀͷ���Ա�б�
	 * @param companyId
	 * @return
	 */
	public List<OrgUserBo> getAdminUserList(String companyId);
	
}
