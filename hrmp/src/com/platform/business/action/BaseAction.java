package com.platform.business.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.platform.organization.pojo.OrgUser;

public class BaseAction extends ActionSupport {

	public HttpServletResponse getResponse() {
		HttpServletResponse res = ServletActionContext.getResponse();
		return res;
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest req = ServletActionContext.getRequest();
		return req;
	}

	public OrgUser getLoginUser() {
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpSession sess = req.getSession();
		OrgUser user = (OrgUser) sess.getAttribute("loginUser");
		return user;
	}
}
