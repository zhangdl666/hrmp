package com.platform.app.service.xmlpo;



public class ReqDetail {

	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 分页查询，第几页
	 */
	private String pageNo;
	
	/**
	 * 分页查询，每页记录数
	 */
	private String pageSize;
	
	private SignEmp signEmp;
	
	private String workId;
	
	private String msgId;
	
	private String originalPwd;
	
	private String newPwd;
	
	private String confirmPwd;
	
	private String workKind;

	public String getWorkKind() {
		return workKind;
	}

	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public SignEmp getSignEmp() {
		return signEmp;
	}

	public void setSignEmp(SignEmp signEmp) {
		this.signEmp = signEmp;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getOriginalPwd() {
		return originalPwd;
	}

	public void setOriginalPwd(String originalPwd) {
		this.originalPwd = originalPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

}
