package com.platform.business.bo;

import java.util.Date;

public class WorkHireQueryBo {
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getCreateTimeFrom() {
		return createTimeFrom;
	}

	public void setCreateTimeFrom(Date createTimeFrom) {
		this.createTimeFrom = createTimeFrom;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getWorkCompany() {
		return workCompany;
	}

	public void setWorkCompany(String workCompany) {
		this.workCompany = workCompany;
	}

	public Date getPublishTimeFrom() {
		return publishTimeFrom;
	}

	public void setPublishTimeFrom(Date publishTimeFrom) {
		this.publishTimeFrom = publishTimeFrom;
	}

	public Date getPublishTimeEnd() {
		return publishTimeEnd;
	}

	public void setPublishTimeEnd(Date publishTimeEnd) {
		this.publishTimeEnd = publishTimeEnd;
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

	public Date getPlanStartTimeFrom() {
		return planStartTimeFrom;
	}

	public void setPlanStartTimeFrom(Date planStartTimeFrom) {
		this.planStartTimeFrom = planStartTimeFrom;
	}

	public Date getPlanStartTimeEnd() {
		return planStartTimeEnd;
	}

	public void setPlanStartTimeEnd(Date planStartTimeEnd) {
		this.planStartTimeEnd = planStartTimeEnd;
	}

	public Date getPlanEndTimeFrom() {
		return planEndTimeFrom;
	}

	public void setPlanEndTimeFrom(Date planEndTimeFrom) {
		this.planEndTimeFrom = planEndTimeFrom;
	}

	public Date getPlanEndTimeEnd() {
		return planEndTimeEnd;
	}

	public void setPlanEndTimeEnd(Date planEndTimeEnd) {
		this.planEndTimeEnd = planEndTimeEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getSignUserId() {
		return signUserId;
	}

	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}

	public String getNotSignUserId() {
		return notSignUserId;
	}

	public void setNotSignUserId(String notSignUserId) {
		this.notSignUserId = notSignUserId;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}
	
	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}
	private String id;
	
	private String businessNumber;

	private String publisherId;
	
	private String publisherName;
	
	private String publisherCompanyId;
	
	private String publisherCompanyName;
	
	private Date createTimeFrom;
	
	private Date createTimeEnd;
	
	private String title;
	
	private String workCompany;
	
	private Date publishTimeFrom;
	
	private Date publishTimeEnd;
	
	private String workArea;
	
	private String workDescri;
	
	private String workKind;
	
	private Date planStartTimeFrom;
	
	private Date planStartTimeEnd;
	
	private Date planEndTimeFrom;
	
	private Date planEndTimeEnd;
	
	private String status;//noPublish：草稿，publishing：正在招工，closed：已关闭；delete：删除

	private String signUserId;//报名人ID  ，此属性有值时表示只查询此用户报名的招工信息
	
	private String notSignUserId;//报名人ID  ，此属性有值时表示需过滤掉此用户报名的招工信息
}
