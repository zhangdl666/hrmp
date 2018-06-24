package com.platform.app.service.xmlpo;

import java.util.Date;

public class Advertisement {
	private String id;
	
	private String businessNumber;
	
	private String createTime;
	
	/* 联系人名称 */
	private String contactUser;
	
	/* 联系人电话 */
	private String contactUserPhone;
	
	private String title;
	
	private String content;
	
	private String months;
	
	/* 单价 */
	private String unitPrice;
	
	/* 支付总额 */
	private String payFee;
	
	/* 预支付id */
	private String prepayId;
	
	/* 支付结果，1：已支付；0：未支付 */
	private String payStatus;
	
	/* 是否有效报名，1；有效；0：无效 */
	private String validStatus;
	
	/* 备注 */
	private String remark;
	
	/* 是否关闭，1；是；0：否 */
	private String isClosed;
	
	private String closeTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContactUser() {
		return contactUser;
	}

	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}

	public String getContactUserPhone() {
		return contactUserPhone;
	}

	public void setContactUserPhone(String contactUserPhone) {
		this.contactUserPhone = contactUserPhone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	
	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}
	
	
}
