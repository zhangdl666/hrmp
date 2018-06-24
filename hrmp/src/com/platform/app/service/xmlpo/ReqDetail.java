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
	
	/**
	 * 短信验证码类型	pwd：忘记密码;regedit：用户注册
	 */
	private String identifyCodeType;
	
	private User user;
	
	private String workId;
	
	private String msgId;
	
	private String originalPwd;
	
	private String newPwd;
	
	private String confirmPwd;
	
	private String workKind;
	
	private String empTypeId;
	
	private Work work;
	
	private String keyword;
	
	private Advertisement advertisement;

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

	public String getIdentifyCodeType() {
		return identifyCodeType;
	}

	public void setIdentifyCodeType(String identifyCodeType) {
		this.identifyCodeType = identifyCodeType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEmpTypeId() {
		return empTypeId;
	}

	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Advertisement getAdvertisement() {
		return advertisement;
	}

	public void setAdvertisement(Advertisement advertisement) {
		this.advertisement = advertisement;
	}

}
