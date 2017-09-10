package com.platform.app.service.xmlpo;


public class Work {
	
	private String id;
	
	/**
	 * 单号
	 */
	private String businessNumber;
	
	/**
	 * 发布人
	 */
	private String publisherName;
	
	/**
	 * 发布人所在公司
	 */
	private String publisherCompanyName;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 *用工单位
	 */
	private String workCompany;
	
	/**
	 * 发布时间
	 */
	private String publishTime;
	
	/**
	 * 关闭时间
	 */
	private String closeTime;
	
	/**
	 * 工作地点
	 */
	private String workArea;
	
	/**
	 * 工作描述
	 */
	private String workDescri;
	
	/**
	 * 需要工种
	 */
	private String workKind;
	
	/**
	 * 预计用工开始时间
	 */
	private String planStartTime;
	
	/**
	 * 预计用工结束时间
	 */
	private String planEndTime;
	
	/**
	 * 招工数量
	 */
	private int hireNum;
	
	private String status;//noPublish：草稿，publishing：正在招工，closed：已关闭；delete：删除
	
	/**
	 * 报名时间
	 */
	private String signTime;
	
	
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	/**
	 * 报名费单价，单位：元/人，用于“我要报名”弹出页面
	 */
	private double unitPrice;
	
	/**
	 * 是否能取消报名 
	 * 1：可以取消报名；
	 * 0：不可以取消报名
	 */
	private String canCancelSign;
	
	/**
	 * 支付状态	1：支付成功；0：待支付
	 */
	private String payStatus;
	
	/**
	 * 预支付id
	 */
	private String prepayId;
	
	/**
	 * 报名人数
	 */
	private String num;
	
	/**
	 * 支付金额	单位：元
	 */
	private String payFee;
	
	public String getCanCancelSign() {
		return canCancelSign;
	}

	public void setCanCancelSign(String canCancelSign) {
		this.canCancelSign = canCancelSign;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisherCompanyName() {
		return publisherCompanyName;
	}

	public void setPublisherCompanyName(String publisherCompanyName) {
		this.publisherCompanyName = publisherCompanyName;
	}

	public String getWorkCompany() {
		return workCompany;
	}

	public void setWorkCompany(String workCompany) {
		this.workCompany = workCompany;
	}


	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getWorkDescri() {
		return workDescri;
	}

	public void setWorkDescri(String workDescri) {
		this.workDescri = workDescri;
	}

	public String getWorkKind() {
		return workKind;
	}

	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}


	public int getHireNum() {
		return hireNum;
	}

	public void setHireNum(int hireNum) {
		this.hireNum = hireNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}

	public String getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	
}
