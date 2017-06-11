package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_WORK_HIRE")
public class WorkHireView {
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name="BUSINESS_NUMBER")
	private String businessNumber;
	
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
	
	@Column(name="PUBLISH_DATE")
	private Date publishTime;
	
	@Column(name="WORK_KIND")
	private String workKind;
	
	@Column(name="HIRE_NUM")
	private int hireNum;
	
	@Column(name="SALARY")
	private String salary;
	
	@Column(name="WORK_DESCRI")
	private String workDescri;
	
	@Column(name="WORK_COMPANY_MOBILE")
	private String workCompanyMobile;
	
	@Column(name="STATUS")
	private String status;//noPublish：草稿，publishing：正在招工，closed：已关闭；delete：删除

	@Column(name="ACTUAL_SIGN_NUM")
	private int actualSignNum;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkCompanyMobile() {
		return workCompanyMobile;
	}

	public void setWorkCompanyMobile(String workCompanyMobile) {
		this.workCompanyMobile = workCompanyMobile;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
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

	public int getActualSignNum() {
		return actualSignNum;
	}

	public void setActualSignNum(int actualSignNum) {
		this.actualSignNum = actualSignNum;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}
}
