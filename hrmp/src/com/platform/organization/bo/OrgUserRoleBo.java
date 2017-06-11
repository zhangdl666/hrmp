package com.platform.organization.bo;

import java.util.Date;

import com.platform.organization.pojo.OrgRole;
import com.platform.organization.pojo.OrgUser;

public class OrgUserRoleBo {

	private String id;
	
	private Date createTime;
	
	private OrgRole role;
	
	private OrgUser user;
	
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

	public OrgUser getUser() {
		return user;
	}

	public void setUser(OrgUser user) {
		this.user = user;
	}

}
