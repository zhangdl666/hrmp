package com.platform.organization.service;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgMenuBo;
import com.platform.organization.pojo.OrgMenu;

public interface OrgMenuService {

	/**
	 * 查询用户可见菜单，用于用户登录后的菜单展现
	 * 
	 * @param userId
	 * @return
	 */
	public List<OrgMenuBo> getUserMenu(String userId);

	public OrgMenu getOrgMenu(String id);
	
	public OrgMenu saveOrgMenu(OrgMenu orgMenu);
	
	public boolean delMenu(String id);

	public List<OrgMenu> queryMenus(String menuName, String url,String parentMenuId);

	public Page queryMenus(String menuName, String url,String parentMenuId, Page page);
}
