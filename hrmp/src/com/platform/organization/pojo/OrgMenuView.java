package com.platform.organization.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_ORG_MENU")
public class OrgMenuView {

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name="MENUNAME")
	private String menuName;
	
	@Column(name="PARENT_ID")
	private String parentId;
	
	@Column(name="URL_")
	private String menuUrl;
	
	@Column(name="ORDER_")
	private int menuIndex;
	
	@Column(name="VALIDSTATUS")
	private String validstatus;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="MENU_ID_PATH")
	private String menuIdPath;
	
	@Column(name="MENU_NAME_PATH")
	private String menuNamePath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuIdPath() {
		return menuIdPath;
	}

	public void setMenuIdPath(String menuIdPath) {
		this.menuIdPath = menuIdPath;
	}

	public String getMenuNamePath() {
		return menuNamePath;
	}

	public void setMenuNamePath(String menuNamePath) {
		this.menuNamePath = menuNamePath;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public int getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}

	public String getValidstatus() {
		return validstatus;
	}

	public void setValidstatus(String validstatus) {
		this.validstatus = validstatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
