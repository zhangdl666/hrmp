package com.platform.organization.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_ORG_DEPT")
public class OrgDeptView {

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name="DEPTNAME")
	private String deptName;
	
	@Column(name="DEPT_FLAG")
	private String deptFlag;//com：公司，dept：部门
	
	@Column(name="PARENTID")
	private String parentId;
	
	@Column(name="CREATETIME")
	private Date createTime;

	@Column(name="VALIDSTATUS")
	private String validstatus;
	
	@Column(name="DEPT_ID_PATH")
	private String deptIdPath;
	
	@Column(name="DEPT_NAME_PATH")
	private String deptNamePath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptFlag() {
		return deptFlag;
	}

	public void setDeptFlag(String deptFlag) {
		this.deptFlag = deptFlag;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getValidstatus() {
		return validstatus;
	}

	public void setValidstatus(String validstatus) {
		this.validstatus = validstatus;
	}

	public String getDeptIdPath() {
		return deptIdPath;
	}

	public void setDeptIdPath(String deptIdPath) {
		this.deptIdPath = deptIdPath;
	}

	public String getDeptNamePath() {
		return deptNamePath;
	}

	public void setDeptNamePath(String deptNamePath) {
		this.deptNamePath = deptNamePath;
	}
}
