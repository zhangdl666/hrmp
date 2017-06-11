package com.platform.organization.bo;

import java.util.Date;

import com.platform.organization.pojo.OrgMenu;
import com.platform.organization.pojo.OrgRole;

public class OrgRoleMenuBo {

	private String id;
	
	private Date createTime;
	
	private OrgRole role;
	
	private OrgMenu menu;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public OrgRole getRole() {
		return role;
	}

	public void setRole(OrgRole role) {
		this.role = role;
	}

	public OrgMenu getMenu() {
		return menu;
	}

	public void setMenu(OrgMenu menu) {
		this.menu = menu;
	}

}
