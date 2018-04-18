package com.platform.organization.bo;

import com.platform.organization.pojo.OrgDept;

public class OrgRoleBo {

	private String id;
	
	private String roleName;
	
	private OrgDept dept;
	
	private String validstatus;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public OrgDept getDept() {
		return dept;
	}

	public void setDept(OrgDept dept) {
		this.dept = dept;
	}

	public String getValidstatus() {
		return validstatus;
	}

	public void setValidstatus(String validstatus) {
		this.validstatus = validstatus;
	}

}
