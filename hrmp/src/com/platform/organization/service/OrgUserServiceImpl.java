package com.platform.organization.service;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.bo.OrgUserRoleBo;
import com.platform.organization.dao.OrgUserDao;
import com.platform.organization.dao.OrgUserRoleDao;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.pojo.OrgUserRole;

public class OrgUserServiceImpl implements OrgUserService {
	private final Logger logger = Logger.getLogger(OrgUserServiceImpl.class);
	
	@Autowired
	private OrgUserDao orgUserDao;
	
	@Autowired
	private OrgUserRoleDao OrgUserRoleDao;
	
	public void setOrgUserDao(OrgUserDao orgUserDao) {
		this.orgUserDao = orgUserDao;
	}

	@Override
	public OrgUser getUser(String id) {
		return orgUserDao.getUser(id);
	}

	public OrgUserRoleDao getOrgUserRoleDao() {
		return OrgUserRoleDao;
	}

	public void setOrgUserRoleDao(OrgUserRoleDao orgUserRoleDao) {
		OrgUserRoleDao = orgUserRoleDao;
	}

	public OrgUserDao getOrgUserDao() {
		return orgUserDao;
	}

	@Override
	public boolean delUser(String id) {
		return orgUserDao.delUser(id);
	}

	@Override
	public OrgUser getUserByLoginName(String loginName) {
		return orgUserDao.getUserByLoginName(loginName);
	}

	@Override
	public List<OrgUser> getUserListByRoleId(String roleId) {
		return orgUserDao.getUserListByRoleId(roleId);
	}

	@Override
	public OrgUser saveUser(OrgUser user) {
		user = orgUserDao.saveUser(user);
		return user;
	}
	
	@Override
	public OrgUser saveUser(OrgUser user, String roleIds) {
		user = orgUserDao.saveUser(user);
		// 处理角色信息
		List<OrgUserRoleBo> userRoleList = OrgUserRoleDao.getUserRoleListByUserId(user.getId());
		
		if(roleIds == null || roleIds.equals("")) {
			roleIds = "";
		}
		//先做删除操作
		String oldRoleIds = ",";
		String tempRoleIds = "," + roleIds + ",";
		if(userRoleList != null && userRoleList.size() > 0) {
			for(int i=0; i<userRoleList.size(); i++) {
				OrgUserRoleBo ur = userRoleList.get(i);
				oldRoleIds = oldRoleIds + ur.getRole().getId() + ",";
				if(tempRoleIds.indexOf("," + ur.getRole().getId() + ",") == -1){
					//用户最新的角色列表中没有此角色，因此删除
					logger.info("用户角色调整：删除用户" + user.getLoginName() + " roleID：" + ur.getRole().getId());
					OrgUserRoleDao.deleteUserRole(ur.getId());
				}
			}
		}
		
		//再做添加操作
		String[] s = roleIds.split(",");
		
		for (int i = 0; i < s.length; i++) {
			String rid = s[i];
			if(oldRoleIds.indexOf(rid) == -1) {
				//为用户新增角色
				logger.info("用户角色调整：用户" + user.getLoginName() + " 新增roleID：" + rid);
				OrgUserRole ur = new OrgUserRole();
				ur.setUserId(user.getId());
				ur.setRoleId(rid);
				ur.setCreateTime(Calendar.getInstance().getTime());
				OrgUserRoleDao.saveUserRole(ur);
			}
			
		}
		return user;
	}

	@Override
	public List<OrgUserBo> queryUsers(String userName, String loginName,
			String departmentId, boolean isContainChildDept) {
		return orgUserDao.queryUsers(userName, loginName, departmentId, isContainChildDept);
	}

	@Override
	public Page queryUsers(String userName, String loginName,
			String departmentId, boolean isContainChildDept, Page page) {
		return orgUserDao.queryUsers(userName, loginName, departmentId, isContainChildDept, page);
	}

}
