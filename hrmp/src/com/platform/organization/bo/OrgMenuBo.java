package com.platform.organization.bo;

import java.util.Date;
import java.util.List;

public class OrgMenuBo {

	private String id;
	
	private String menuName;
	
	private String parentId;
	
	private String menuPath;
	
	private String menuUrl;
	
	private int menuIndex;
	
	private String validstatus;
	
	private Date createTime;
	
	private String remark;

	private List<OrgMenuBo> childMenus;
	
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

	public String getMenuPath() {
		return menuPath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
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

	public List<OrgMenuBo> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<OrgMenuBo> childMenus) {
		this.childMenus = childMenus;
	}

}
