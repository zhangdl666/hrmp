package com.platform.security.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.platform.organization.pojo.OrgUser;

public class SessionFilter extends HttpServlet implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected FilterConfig filterConfig = null;
	private String redirectURL = null;
	private Set<String> notCheckURLList = new HashSet<String>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String url = req.getServletPath();
		String contextPath = req.getContextPath();
		HttpSession session = req.getSession();

		if (url.equals("")) {
			url += "/";
		}
		if(url.equals("/login.jsp") || url.equals("/security/login.action") || url.equals("/security/login.do") 
				|| url.equals("/security/passwordEncrypt.action")
				|| url.equals("security/logout.action")){
			//登录页面或登录请求，直接放行
			chain.doFilter(req, resp);
			return;
		}
		
		OrgUser user = (OrgUser) session.getAttribute("loginUser");
		if (user == null) {// 转入登陆页面
			System.out.println(url + " 无效登录，转向登录页面");
			resp.sendRedirect(contextPath + "/login.jsp");
			return;
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
