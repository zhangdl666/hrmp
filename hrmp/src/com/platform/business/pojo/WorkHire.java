package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WORK_HIRE")
public class WorkHire {
	public static final String WORK_HIRE_STATUS_NOPUBLISH = "noPublish";
	public static final String WORK_HIRE_STATUS_DELETE = "delete";
	public static final String WORK_HIRE_STATUS_PUBLISHING = "publishing";
	public static final String WORK_HIRE_STATUS_CLOSED = "closed";
	public static final String WORK_HIRE_STATUS_CLOSING = "closing";
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
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
	
	@Column(name="CLOSE_DATE")
	private Date closeTime;
	
	/* 招工类型  LS：临时工
	CQ：长期工
	CB：承包施工
	*/
	@Column(name="EMP_TYPE_ID")
	private String empTypeId;
	
	/* 招工时间 */
	@Column(name="EMP_DATE")
	private Date empDate;
	
	/* 性别，male：男
	；female：女；
	all：不限
	 */
	@Column(name="SEX")
	private String sex;
	
	/* 工资备注  GF：管饭
	；GCZ：管吃住；
	BGF：不管饭
	 */
	@Column(name="SALARY_REMARK")
	private String salaryRemark;
	
	/* 承包施工-年龄要求 */
	@Column(name="AGE")
	private String age;
	
	/* 上午，1：选中；0：未选中 */
	@Column(name="AM")
	private String am;
	
	/* 上午开始时间 */
	@Column(name="AM_START")
	private String amStart;
	
	/* 上午截止时间 */
	@Column(name="AM_END")
	private String amEnd;
	
	/* 下午，1：选中；0：未选中 */
	@Column(name="PM")
	private String pm;
	
	/* 下午开始时间 */
	@Column(name="PM_START")
	private String pmStart;
	
	/* 下午截止时间 */
	@Column(name="PM_END")
	private String pmEnd;
	
	/* 晚上，1：选中；0：未选中 */
	@Column(name="NIGHT")
	private String night;
	
	/* 晚上开始时间 */
	@Column(name="NIGHT_START")
	private String nightStart;
	
	/* 晚上截止时间 */
	@Column(name="NIGHT_END")
	private String nightEnd;
	
	/* 工作地点 */
	@Column(name="WORK_AREA")
	private String workArea;
	
	/* 承包施工-条件要求 */
	@Column(name="CONDITION")
	private String condition;
	
	/* 承包施工-付款方式，1：面议；0：其他 */
	@Column(name="PAYMODE")
	private String payMode;
	
	/* 承包施工-付款方式-其他付款方式 */
	@Column(name="PAYMODE_REMARK")
	private String payModeRemark;
	
	@Column(name="WORK_KIND")
	private String workKind;
	
	@Column(name="SALARY")
	private String salary;
	
	@Column(name="WORK_DESCRI")
	private String workDescri;
	
	@Column(name="HIRE_NUM")
	private int hireNum;
	
	@Column(name="STATUS")
	private String status;//noPublish：草稿，publishing：正在招工，closed：已关闭；delete：删除

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
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

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public String getEmpTypeId() {
		return empTypeId;
	}

	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}

	public Date getEmpDate() {
		return empDate;
	}

	public void setEmpDate(Date empDate) {
		this.empDate = empDate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSalaryRemark() {
		return salaryRemark;
	}

	public void setSalaryRemark(String salaryRemark) {
		this.salaryRemark = salaryRemark;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAm() {
		return am;
	}

	public void setAm(String am) {
		this.am = am;
	}

	public String getAmStart() {
		return amStart;
	}

	public void setAmStart(String amStart) {
		this.amStart = amStart;
	}

	public String getAmEnd() {
		return amEnd;
	}

	public void setAmEnd(String amEnd) {
		this.amEnd = amEnd;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getPmStart() {
		return pmStart;
	}

	public void setPmStart(String pmStart) {
		this.pmStart = pmStart;
	}

	public String getPmEnd() {
		return pmEnd;
	}

	public void setPmEnd(String pmEnd) {
		this.pmEnd = pmEnd;
	}

	public String getNight() {
		return night;
	}

	public void setNight(String night) {
		this.night = night;
	}

	public String getNightStart() {
		return nightStart;
	}

	public void setNightStart(String nightStart) {
		this.nightStart = nightStart;
	}

	public String getNightEnd() {
		return nightEnd;
	}

	public void setNightEnd(String nightEnd) {
		this.nightEnd = nightEnd;
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayModeRemark() {
		return payModeRemark;
	}

	public void setPayModeRemark(String payModeRemark) {
		this.payModeRemark = payModeRemark;
	}
	
	
}
