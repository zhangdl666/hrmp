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
	public String getEmpTypeId() {
		return empTypeId;
	}

	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
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
	
	private Date publishTimeFrom;
	
	private Date publishTimeEnd;
	
	private String workArea;
	
	private String workDescri;
	
	private String workKind;
	
	private String status;//noPublish���ݸ壬publishing�������й���closed���ѹرգ�delete��ɾ��

	private String signUserId;//������ID  ����������ֵʱ��ʾֻ��ѯ���û��������й���Ϣ
	
	private String notSignUserId;//������ID  ����������ֵʱ��ʾ����˵����û��������й���Ϣ
	
	private String empTypeId;
}
