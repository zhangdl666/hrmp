package com.platform.organization.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgRoleBo;
import com.platform.organization.bo.OrgRoleMenuBo;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.bo.OrgUserRoleBo;
import com.platform.organization.bo.TreeNode;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgDeptView;
import com.platform.organization.pojo.OrgMenu;
import com.platform.organization.pojo.OrgMenuView;
import com.platform.organization.pojo.OrgRole;
import com.platform.organization.pojo.OrgRoleMenu;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.pojo.OrgUserRole;
import com.platform.organization.service.OrgDeptService;
import com.platform.organization.service.OrgMenuService;
import com.platform.organization.service.OrgRoleMenuService;
import com.platform.organization.service.OrgRoleService;
import com.platform.organization.service.OrgUserRoleService;
import com.platform.organization.service.OrgUserService;
import com.platform.security.util.Encrypts;

public class OrganizationAction extends ActionSupport {

	private OrgUserService orgUserService;
	private OrgRoleService orgRoleService;
	private OrgDeptService orgDeptService;
	private OrgRoleMenuService orgRoleMenuService;
	private OrgUserRoleService orgUserRoleService;
	private OrgMenuService orgMenuService;
	
	private String deptId;
	private String loginName;
	private String userName;
	private String delUserId;
	private String userId;
	private OrgUser user;
	private String checkedIds;
	private String treeNodeData;
	private String roleIds;
	private String roleNames;
	private String message;
	private List<OrgUserBo> userList;
	private Page page;
	
	private String roleName;
	
	private String delRoleId;
	private String roleId;
	private OrgRole role;
	private List<OrgRoleBo> roleList;
	private List<OrgMenuView> menuList;
	private List<OrgRoleMenuBo> roleMenuBoList;
	private List<OrgUserRoleBo> userRoleBoList;
	private String delId;
	
	private String deptName;
	private OrgDept dept;
	private List<OrgDeptView> deptList;

	private String menuId;
	private String menuName;
	private String menuUrl;
	private OrgMenu menu;
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<OrgUserBo> getUserList() {
		return userList;
	}

	public void setUserList(List<OrgUserBo> userList) {
		this.userList = userList;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDelUserId() {
		return delUserId;
	}

	public void setDelUserId(String delUserId) {
		this.delUserId = delUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OrgUser getUser() {
		return user;
	}

	public void setUser(OrgUser user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getCheckedIds() {
		return checkedIds;
	}

	public void setCheckedIds(String checkedIds) {
		this.checkedIds = checkedIds;
	}

	public String getTreeNodeData() {
		return treeNodeData;
	}

	public void setTreeNodeData(String treeNodeData) {
		this.treeNodeData = treeNodeData;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public OrgDeptService getOrgDeptService() {
		return orgDeptService;
	}

	public void setOrgDeptService(OrgDeptService orgDeptService) {
		this.orgDeptService = orgDeptService;
	}

	public String getDelRoleId() {
		return delRoleId;
	}

	public void setDelRoleId(String delRoleId) {
		this.delRoleId = delRoleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public OrgRole getRole() {
		return role;
	}

	public void setRole(OrgRole role) {
		this.role = role;
	}

	public List<OrgRoleBo> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<OrgRoleBo> roleList) {
		this.roleList = roleList;
	}


	public List<OrgMenuView> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<OrgMenuView> menuList) {
		this.menuList = menuList;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public OrgRoleMenuService getOrgRoleMenuService() {
		return orgRoleMenuService;
	}

	public void setOrgRoleMenuService(OrgRoleMenuService orgRoleMenuService) {
		this.orgRoleMenuService = orgRoleMenuService;
	}

	public List<OrgRoleMenuBo> getRoleMenuBoList() {
		return roleMenuBoList;
	}

	public void setRoleMenuBoList(List<OrgRoleMenuBo> roleMenuBoList) {
		this.roleMenuBoList = roleMenuBoList;
	}

	public List<OrgUserRoleBo> getUserRoleBoList() {
		return userRoleBoList;
	}

	public void setUserRoleBoList(List<OrgUserRoleBo> userRoleBoList) {
		this.userRoleBoList = userRoleBoList;
	}

	public OrgUserRoleService getOrgUserRoleService() {
		return orgUserRoleService;
	}

	public void setOrgUserRoleService(OrgUserRoleService orgUserRoleService) {
		this.orgUserRoleService = orgUserRoleService;
	}

	public String getDelId() {
		return delId;
	}

	public void setDelId(String delId) {
		this.delId = delId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public OrgMenuService getOrgMenuService() {
		return orgMenuService;
	}

	public void setOrgMenuService(OrgMenuService orgMenuService) {
		this.orgMenuService = orgMenuService;
	}

	public OrgDept getDept() {
		return dept;
	}

	public void setDept(OrgDept dept) {
		this.dept = dept;
	}

	public List<OrgDeptView> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<OrgDeptView> deptList) {
		this.deptList = deptList;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public OrgMenu getMenu() {
		return menu;
	}

	public void setMenu(OrgMenu menu) {
		this.menu = menu;
	}

	/**
	 * 用户管理
	 * @return
	 */
	public String userManage(){
		return SUCCESS;
	}
	
	/**
	 * 展示公司组织结构树
	 * @return
	 * @throws IOException 
	 */
	public String getDeptTree() throws IOException{
		ServletResponse response = ServletActionContext.getResponse();
		 /* 设置格式为text/json    */
        response.setContentType("text/json");
        /*设置字符集为'UTF-8'*/
        response.setCharacterEncoding("UTF-8"); 
        PrintWriter writer = response.getWriter();
		List<OrgDeptView> deptList = orgDeptService.queryDepts(null, deptId, true);
		if(deptList==null || deptList.size()==0) {
			writer.write("");
			return null;
		}
		
		List<TreeNode> treeNodes = new ArrayList<TreeNode>(); 
		for(int i=0; i<deptList.size(); i++) {
			OrgDeptView dept = deptList.get(i);
			TreeNode tn = new TreeNode();
			tn.setId(dept.getId());
			tn.setName(dept.getDeptName());
			tn.setpId(dept.getParentId());
			tn.setOpen(true);
			treeNodes.add(tn);
		}
		
		JSONArray jsonArray = JSONArray.fromObject(treeNodes);
		writer.write(jsonArray.toString());
		return null;
	}
	
	/**
	 * 依据左侧选择的组织结构树查询用户
	 * @return
	 */
	public String userQuery(){
		if(page.getCurrentPage()==0) {
			page.setCurrentPage(1);
		}
		if(page.getPageSize()==0) {
			page.setPageSize(10);
		}
		page = orgUserService.queryUsers(userName, loginName, deptId, true, page);
		userList = page.getResult();
		return SUCCESS;
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public String delUser() {
		boolean b = orgUserService.delUser(delUserId);
		if(b) {
			message = "删除成功";
		}else {
			message = "删除失败";
		}
		page = orgUserService.queryUsers(userName, loginName, deptId, true, page);
		userList = page.getResult();
		return SUCCESS;
	}
	
	public String viewUser() {
		if(userId != null) {
			user = orgUserService.getUser(userId);
			if(user != null) {
				List<OrgRole> rList = orgRoleService.getRoleListByUserId(userId);
				if(rList != null && rList.size()>0) {
					roleIds = "";
					roleNames = "";
					for(int i=0; i<rList.size(); i++) {
						OrgRole r = rList.get(i);
						roleIds = roleIds + r.getId() + ",";
						roleNames = roleNames + r.getRoleName() + ",";
					}
				}
			}
			
		}else {
			user = new OrgUser();
			user.setDeptId(deptId);
		}
		return SUCCESS;
	}
	
	public String saveUser(){
		if(user.getId() == null){//新建用户
			//密码加密
			String pwd = user.getPwd();
			if(pwd == null || pwd.equals("")) {
				pwd = "1qaz@WSX";
			}
			pwd = Encrypts.encryptPassword(pwd);
			user.setPwd(pwd);
			user.setCreateTime(Calendar.getInstance().getTime());
			user.setPwdUpdateTime(Calendar.getInstance().getTime());
			user.setValidstatus("1");
		}
		user = orgUserService.saveUser(user,roleIds);
		
		message = "保存成功";
		return SUCCESS;
	}
	
	//选择角色
	public String selectRoles(){
		List<OrgRoleBo> roles = orgRoleService.queryRoles(null, deptId, false, false);
		treeNodeData = "";
		List<String> checkIdList = new ArrayList<String>();
		if(checkedIds != null && !"".equals(checkedIds)) {
			String[] s = checkedIds.split(",");
			Collections.addAll(checkIdList, s);
		}
		if(roles!=null && roles.size()>0) {
			
			List<TreeNode> treeNodes = new ArrayList<TreeNode>(); 
			for(int i=0; i<roles.size(); i++) {
				OrgRoleBo role = roles.get(i);
				TreeNode tn = new TreeNode();
				tn.setId(role.getId());
				tn.setName(role.getRoleName());
				tn.setOpen(true);
				if(checkIdList.contains(role.getId())){
					tn.setChecked(true);
				}
				treeNodes.add(tn);
			}
			JSONArray jsonArray = JSONArray.fromObject(treeNodes);
			treeNodeData = jsonArray.toString();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 角色管理
	 * @return
	 */
	public String roleManage(){
		return SUCCESS;
	}
	
	public String roleQuery(){
		if(page.getCurrentPage()==0) {
			page.setCurrentPage(1);
		}
		if(page.getPageSize()==0) {
			page.setPageSize(10);
		}
		page = orgRoleService.queryRoles(roleName, deptId, false, true, page);
		roleList = page.getResult();
		return SUCCESS;
	}
	
	public String delRole() {
		boolean b = orgRoleService.delRole(delRoleId);
		if(b) {
			message = "删除成功";
		}else {
			message = "删除失败";
		}
		page = orgRoleService.queryRoles(roleName, deptId, false, true, page);
		roleList = page.getResult();
		return SUCCESS;
	}
	
	public String viewRole() {
		if(roleId != null) {
			role = orgRoleService.getRole(roleId);
		}else {
			role = new OrgRole();
			role.setDeptId(deptId);
			role.setValidstatus("1");
		}
		return SUCCESS;
	}
	
	public String saveRole(){
		role = orgRoleService.saveRole(role);
		
		message = "保存成功";
		return SUCCESS;
	}
	
	public String delUserRole() {
		boolean b = orgUserRoleService.deleteUserRole(delId);
		if(b) {
			message = "删除成功";
		}else {
			message = "删除失败";
		}
		return ajaxRefreshRelationUser();
	}
	
	public String delRoleMenu() {
		boolean b = orgRoleMenuService.deleteRoleMenu(delId);
		if(b) {
			message = "删除成功";
		}else {
			message = "删除失败";
		}
		return ajaxRefreshRelationMenu();
	}
	
	
	//为角色添加用户
	public String selectUsersForRole(){
		OrgRole role = orgRoleService.getRole(roleId);
		String roleDeptId = role.getDeptId();
		
		//先查询部门数据
		List<OrgDeptView> depts = orgDeptService.queryDepts(null, roleDeptId, true);
		List<OrgUserBo> users = orgUserService.queryUsers(null, null, roleDeptId, true);
		
		List<TreeNode> treeNodes = new ArrayList<TreeNode>(); 
		treeNodeData = "";
		
		//先放入部门节点
		List<String> checkIdList = new ArrayList<String>();
		if(depts!=null && depts.size()>0) {
			for(int i=0;i<depts.size();i++) {
				OrgDeptView d = depts.get(i);
				TreeNode tnDept = new TreeNode();
				tnDept.setId(d.getId());
				tnDept.setName(d.getDeptName());
				tnDept.setpId(d.getParentId());
				tnDept.setParent(true);
				treeNodes.add(tnDept);
			}
		}
		
		//再放入用户节点
		if(checkedIds != null && !"".equals(checkedIds)) {
			String[] s = checkedIds.split(",");
			Collections.addAll(checkIdList, s);
		}
		if(users!=null && users.size()>0) {
			for(int i=0; i<users.size(); i++) {
				OrgUserBo user = users.get(i);
				TreeNode tn = new TreeNode();
				tn.setId(user.getId());
				tn.setName(user.getUserName());
				tn.setpId(user.getDept().getId());
				if(checkIdList.contains(role.getId())){
					tn.setChecked(true);
				}
				treeNodes.add(tn);
			}
			JSONArray jsonArray = JSONArray.fromObject(treeNodes);
			treeNodeData = jsonArray.toString();
		}
		
		return SUCCESS;
	}
	
	public String addUserRole() throws IOException {
		ServletRequest request = ServletActionContext.getRequest();

		OrgRole role = orgRoleService.getRole(roleId);
		if (role == null) {
			message = "操作失败，找不到角色，roleId：" + roleId;
			return null;
		}

		String userIds = request.getParameter("userIds");
		if (userIds == null || userIds.equals("")) {
			message = "操作失败，参数为空，userIds：" + userIds;
			return null;
		}

		String[] s = userIds.split(",");
		for (int i = 0; i < s.length; i++) {
			OrgUserRole ur = orgUserRoleService.getOrgUserRole(s[i], roleId);
			if (ur == null) {
				ur = new OrgUserRole();
				ur.setRoleId(roleId);
				ur.setUserId(s[i]);
				ur.setCreateTime(Calendar.getInstance().getTime());
				orgUserRoleService.saveUserRole(ur);
			}
		}
		message = "success";
		treeNodeData = "success";//若不给此字段赋值，则返回页面无法自动关闭
		return SUCCESS;
	}
	
	public String addRoleMenu() throws IOException {
		ServletRequest request = ServletActionContext.getRequest();

		OrgRole role = orgRoleService.getRole(roleId);
		if (role == null) {
			message = "操作失败，找不到角色，roleId：" + roleId;
			return null;
		}

		String menuIds = request.getParameter("menuIds");
		if (menuIds == null || menuIds.equals("")) {
			message = "操作失败，参数为空，menuIds：" + menuIds;
			return null;
		}

		String[] s = menuIds.split(",");
		for (int i = 0; i < s.length; i++) {
			OrgRoleMenu rm = orgRoleMenuService.getOrgRoleMenu(roleId, s[i]);
			if (rm == null) {
				rm = new OrgRoleMenu();
				rm.setRoleId(roleId);
				rm.setMenuId(s[i]);
				rm.setCreateTime(Calendar.getInstance().getTime());
				orgRoleMenuService.saveRoleMenu(rm);
			}
		}
		message = "success";
		treeNodeData = "success";//若不给此字段赋值，则返回页面无法自动关闭
		return SUCCESS;
	}
	
	//为角色添加菜单
	public String selectMenusForRole(){
		List<OrgMenuView> mList = orgMenuService.queryMenus(null, null, null, true);
		
		List<TreeNode> treeNodes = new ArrayList<TreeNode>(); 
		treeNodeData = "";
		
		List<String> checkIdList = new ArrayList<String>();
		if(checkedIds != null && !"".equals(checkedIds)) {
			String[] s = checkedIds.split(",");
			Collections.addAll(checkIdList, s);
		}
		if(mList!=null && mList.size()>0) {
			for(int i=0; i<mList.size(); i++) {
				OrgMenuView menu = mList.get(i);
				TreeNode tn = new TreeNode();
				tn.setId(menu.getId());
				tn.setName(menu.getMenuName());
				tn.setpId(menu.getParentId());
				if(checkIdList.contains(menu.getId())){
					tn.setChecked(true);
				}
				treeNodes.add(tn);
			}
			JSONArray jsonArray = JSONArray.fromObject(treeNodes);
			treeNodeData = jsonArray.toString();
		}
		
		return SUCCESS;
	}
	
	public String ajaxRefreshRelationUser(){
		userRoleBoList = orgUserRoleService.getUserRoleListByRoleId(roleId);
		return SUCCESS;
	}
	
	public String ajaxRefreshRelationMenu(){
		roleMenuBoList = orgRoleMenuService.getRoleMenuListByRoleId(roleId);
		return SUCCESS;
	}
	
	
	/**
	 * 部门管理
	 * @return
	 */
	public String deptManage(){
		return SUCCESS;
	}
	
	public String deptQuery(){
		if(page.getCurrentPage()==0) {
			page.setCurrentPage(1);
		}
		if(page.getPageSize()==0) {
			page.setPageSize(10);
		}
		page = orgDeptService.queryDepts(deptName, deptId, true,page);
		deptList = page.getResult();
		return SUCCESS;
	}
	
	public String delDept() {
		boolean b = orgDeptService.delDept(delId);
		if(b) {
			message = "删除成功";
		}else {
			message = "删除失败";
		}
		page = orgDeptService.queryDepts(deptName, deptId, true,page);
		deptList = page.getResult();
		return SUCCESS;
	}
	
	public String viewDept() {
		if(deptId != null) {
			dept = orgDeptService.getOrgDept(deptId);
		}else {
			ServletRequest request = ServletActionContext.getRequest();
			String parentDeptId = request.getParameter("parentDeptId");
			dept = new OrgDept();
			dept.setParentId(parentDeptId);
			dept.setCreateTime(Calendar.getInstance().getTime());
			dept.setValidstatus("1");
		}
		return SUCCESS;
	}
	
	public String saveDept(){
		dept = orgDeptService.saveDept(dept);
		
		message = "保存成功";
		return SUCCESS;
	}
	
	/**
	 * 展示菜单树
	 * @return
	 * @throws IOException 
	 */
	public String getMenuTree() throws IOException{
		ServletResponse response = ServletActionContext.getResponse();
		 /* 设置格式为text/json    */
        response.setContentType("text/json");
        /*设置字符集为'UTF-8'*/
        response.setCharacterEncoding("UTF-8"); 
        PrintWriter writer = response.getWriter();
        List<OrgMenuView> mList = orgMenuService.queryMenus(null, null, null, true);
		if(mList==null || mList.size()==0) {
			writer.write("");
			return null;
		}
		
		List<TreeNode> treeNodes = new ArrayList<TreeNode>(); 
		for(int i=0; i<mList.size(); i++) {
			OrgMenuView m = mList.get(i);
			TreeNode tn = new TreeNode();
			tn.setId(m.getId());
			tn.setName(m.getMenuName());
			tn.setpId(m.getParentId());
			tn.setOpen(true);
			treeNodes.add(tn);
		}
		
		JSONArray jsonArray = JSONArray.fromObject(treeNodes);
		writer.write(jsonArray.toString());
		return null;
	}
	
	/**
	 * 菜单管理
	 * @return
	 */
	public String menuManage(){
		return SUCCESS;
	}
	
	public String menuQuery(){
		if(page.getCurrentPage()==0) {
			page.setCurrentPage(1);
		}
		if(page.getPageSize()==0) {
			page.setPageSize(10);
		}
		page = orgMenuService.queryMenus(menuName, menuUrl,menuId,true, page);
		menuList = page.getResult();
		return SUCCESS;
	}
	
	public String delMenu() {
		boolean b = orgMenuService.delMenu(delId);
		if(b) {
			message = "删除成功";
		}else {
			message = "删除失败";
		}
		page = orgMenuService.queryMenus(menuName, menuUrl,menuId,true, page);
		menuList = page.getResult();
		return SUCCESS;
	}
	
	public String viewMenu() {
		if(menuId != null) {
			menu = orgMenuService.getOrgMenu(menuId);
		}else {
			ServletRequest request = ServletActionContext.getRequest();
			String parentDeptId = request.getParameter("parentMenuId");
			menu = new OrgMenu();
			menu.setParentId(parentDeptId);
			menu.setCreateTime(Calendar.getInstance().getTime());
			menu.setValidstatus("1");
		}
		return SUCCESS;
	}
	
	public String saveMenu(){
		menu = orgMenuService.saveOrgMenu(menu);
		
		message = "保存成功";
		return SUCCESS;
	}
	
	public String viewModifyPwdPage() {
		return SUCCESS;
	}
	
	public String modifyPwd() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String originalPwd = request.getParameter("originalPwd");
		String newPwd = request.getParameter("newPwd");
		String confirmPwd = request.getParameter("confirmPwd");
		
		if("".equals(newPwd) || "".equals(confirmPwd)) {
			message = "新密码不能为空";
			return ERROR;
		}
		
		if(!newPwd.equals(confirmPwd)) {
			message = "两次输入的密码不一致";
			return ERROR;
		}
		
	    HttpSession sess = request.getSession();
		OrgUser loginUser = (OrgUser)sess.getAttribute("loginUser");
		String pwd = loginUser.getPwd();
		String pwdMingWen = Encrypts.decryptPassword(pwd);
		if(!pwdMingWen.equals(originalPwd)) {
			message = "旧密码不对";
			return ERROR;
		}
		
		String newPwdMiWen = Encrypts.encryptPassword(newPwd);
		loginUser.setPwd(newPwdMiWen);
		loginUser.setPwdUpdateTime(Calendar.getInstance().getTime());
		orgUserService.saveUser(loginUser);
		return SUCCESS;
	}
}
