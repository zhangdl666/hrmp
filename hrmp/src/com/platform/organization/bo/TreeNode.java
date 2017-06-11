package com.platform.organization.bo;

/**
 * 树形结构展示
 * 字段来源于ztree treeNode
 * @author zdl
 *
 */
public class TreeNode {

	private String id;
	
	private String pId;
	
	private String name;
	
	private boolean open;
	
	private boolean isParent;
	
	private String iconSkin;
	
	private String iconOpen;
	
	private String iconClose;
	
	private String icon;
	
	private String font;
	
	private String url;
	
	private String target;
	
	private boolean checked;
	
	private boolean nocheck;
	
	private boolean doCheck;
	
	private boolean chkDisabled;
	
	private boolean halfCheck;
	
	private boolean right;
	
	private boolean down;
	
	private boolean up;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean isDoCheck() {
		return doCheck;
	}

	public void setDoCheck(boolean doCheck) {
		this.doCheck = doCheck;
	}

	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public boolean isHalfCheck() {
		return halfCheck;
	}

	public void setHalfCheck(boolean halfCheck) {
		this.halfCheck = halfCheck;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}
	
	
}
