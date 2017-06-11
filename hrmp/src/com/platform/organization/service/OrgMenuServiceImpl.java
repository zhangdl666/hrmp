package com.platform.organization.service;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgMenuBo;
import com.platform.organization.dao.OrgMenuDao;
import com.platform.organization.pojo.OrgMenu;
import com.platform.organization.pojo.OrgMenuView;

public class OrgMenuServiceImpl implements OrgMenuService {

	private OrgMenuDao orgMenuDao;
	
	
	
	public OrgMenuDao getOrgMenuDao() {
		return orgMenuDao;
	}



	public void setOrgMenuDao(OrgMenuDao orgMenuDao) {
		this.orgMenuDao = orgMenuDao;
	}



	@Override
	public List<OrgMenuBo> getUserMenu(String userId) {
		return orgMenuDao.getUserMenu(userId);
	}



	@Override
	public OrgMenu getOrgMenu(String id) {
		return orgMenuDao.getOrgMenu(id);
	}



	@Override
	public List<OrgMenuView> queryMenus(String menuName, String url,String parentMenuId,boolean isContainChildMenu) {
		return orgMenuDao.queryMenus(menuName, url,parentMenuId,isContainChildMenu);
	}



	@Override
	public Page queryMenus(String menuName, String url,String parentMenuId,boolean isContainChildMenu, Page page) {
		return orgMenuDao.queryMenus(menuName, url,parentMenuId,isContainChildMenu, page);
	}



	@Override
	public boolean delMenu(String id) {
		return orgMenuDao.delMenu(id);
	}



	@Override
	public OrgMenu saveOrgMenu(OrgMenu orgMenu) {
		return orgMenuDao.saveOrgMenu(orgMenu);
	}

}
