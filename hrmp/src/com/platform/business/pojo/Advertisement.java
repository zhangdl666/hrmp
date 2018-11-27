package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ADVERTISEMENT")
public class Advertisement {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name="BUSINESS_NUMBER")
	private Integer businessNumber;
	
	@Column(name="PUBLISHER_ID")
	private String publisherId;
	
	@Column(name="PUBLISHER_NAME")
	private String publisherName;
	
	@Column(name="PUBLISHER_COMPANY_ID")
	private String publisherCompanyId;
	
	@Column(name="PUBLISHER_COMPANY_NAME")
	private String publisherCompanyName;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	/* 联系人名称 */
	@Column(name="CONTACT_USER")
	private String contactUser;
	
	/* 联系人电话 */
	@Column(name="CONTACT_USER_PHONE")
	private String contactUserPhone;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="MONTHS")
	private int months;
	
	/* 单价 */
	@Column(name="UNITPRICE")
	private double unitPrice;
	
	/* 支付总额 */
	@Column(name="TOTAL_MONEY")
	private double totalMoney;
	
	/* 预支付id */
	@Column(name="PREPAY_ID")
	private String prepayId;
	
	/* 支付结果，1：已支付；0：未支付 */
	@Column(name="PAY_STATUS")
	private String payStatus;
	
	/* 是否有效报名，1；有效；0：无效 */
	@Column(name="VALIDSTATUS")
	private String validStatus;
	
	/* 备注 */
	@Column(name="REMARK")
	private String remark;
	
	/* 是否关闭，1；是；0：否 */
	@Column(name="IS_CLOSED")
	private String isClosed;
	
	@Column(name="CLOSE_TIME")
	private Date closeTime;
	
	@Column(name="CLICK_COUNT")
	private Integer clickCount;
	
	@Column(name="END_TIME")
	private Date endTime;
	
	@Column(name="RELATION_ID")
	private String relationId;
	
	@Column(name="IMAGE")
	private String image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(Integer businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherCompanyId() {
		return publisherCompanyId;
	}

	public void setPublisherCompanyId(String publisherCompanyId) {
		this.publisherCompanyId = publisherCompanyId;
	}

	public String getPublisherCompanyName() {
		return publisherCompanyName;
	}

	public void setPublisherCompanyName(String publisherCompanyName) {
		this.publisherCompanyName = publisherCompanyName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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


	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
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

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
}
