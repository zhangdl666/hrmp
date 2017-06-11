package com.platform.security.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.platform.organization.bo.OrgMenuBo;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.service.OrgMenuService;
import com.platform.organization.service.OrgUserService;
import com.platform.security.util.Encrypts;
import com.platform.security.util.RSAUtils;

public class LoginAction extends ActionSupport {
	private final Logger logger = Logger.getLogger(LoginAction.class);

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
	private OrgUserService orgUserService;
	private OrgMenuService orgMenuService;


	public OrgMenuService getOrgMenuService() {
		return orgMenuService;
	}

	public void setOrgMenuService(OrgMenuService orgMenuService) {
		this.orgMenuService = orgMenuService;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String execute() {
		
		return SUCCESS;
	}
	
	public String login() {
		HttpServletRequest req = ServletActionContext.getRequest();
		if(password == null || "".equals(password)) {
			req.setAttribute("message", "用户名或密码不对！");
			return LOGIN;
		}
		
		OrgUser user = orgUserService.getUserByLoginName(username);
		if(user == null) {
			req.setAttribute("message", "用户名或密码不对！");
			return LOGIN;
		}
		
		String inputPwd = RSAUtils.decryptBase64(password);
		String userPwd = Encrypts.decryptPassword(user.getPwd());
		if(!inputPwd.equals(userPwd)) {
			req.setAttribute("message", "用户名或密码不对！");
			return LOGIN;
		}
		
		//登录成功
		logger.info(username + " 登录成功！");
		HttpSession session = req.getSession();
		session.setAttribute("loginUser", user);
		session.setAttribute("currentUsername", user.getUserName());
		List<OrgMenuBo> menuList = orgMenuService.getUserMenu(user.getId());
		session.setAttribute("menuList", menuList);
		return SUCCESS;
	}
	
	public String logout() {
		HttpServletRequest req = ServletActionContext.getRequest();
	    HttpSession sess = req.getSession();
	    OrgUser user = (OrgUser)sess.getAttribute("loginUser");
	    if(user != null) {
	    	logger.info(user.getUserName() + " 退出成功！");
	    }
		sess.removeAttribute("loginUser");
		username = user.getLoginName();
		return SUCCESS;
	}
	
	/**
	 * 首页
	 * @return
	 */
	public String index(){
		return SUCCESS;
	}
}
