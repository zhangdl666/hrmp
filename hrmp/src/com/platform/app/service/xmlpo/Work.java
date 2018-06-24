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
	private String hireNum;
	
	/**
	 * 实际已招工数量
	 */
	private String hireNumActural;
	
	private String status;//noPublish：草稿，publishing：正在招工，closed：已关闭；delete：删除
	
	/**
	 * 报名时间
	 */
	private String signTime;
	
	/* 招工类型 */
	private String empTypeId;
	
	/* 招工时间 */
	private String empDate;
	
	/* 性别，male：男
	；female：女；
	all：不限
	 */
	private String sex;
	
	private String salary;
	
	/* 工资备注  GF：管饭
	；GCZ：管吃住；
	BGF：不管饭
	 */
	private String salaryRemark;
	
	/* 承包施工-年龄要求 */
	private String age;
	
	/* 上午，1：选中；0：未选中 */
	private String am;
	
	/* 上午开始时间 */
	private String amStart;
	
	/* 上午截止时间 */
	private String amEnd;
	
	/* 下午，1：选中；0：未选中 */
	private String pm;
	
	/* 下午开始时间 */
	private String pmStart;
	
	/* 下午截止时间 */
	private String pmEnd;
	
	/* 晚上，1：选中；0：未选中 */
	private String night;
	
	/* 晚上开始时间 */
	private String nightStart;
	
	/* 晚上截止时间 */
	private String nightEnd;
	
	/* 承包施工-条件要求 */
	private String condition;
	
	/* 承包施工-付款方式，1：面议；0：其他 */
	private String payMode;
	
	/* 承包施工-付款方式-其他付款方式 */
	private String payModeRemark;
	
	/*是否能关闭发布	1：可以关闭发布；0：不可以关闭发布*/
	private String canClosePublish;
	
	/* 联系人名称 */
	private String contactUser;
	
	/* 联系人电话 */
	private String contactUserPhone;
	
	/**
	 * 报名人，多个用“；”隔开
	 */
	private String signUsers;
	
	
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	/**
	 * 报名费单价，单位：元/人，用于“我要报名”弹出页面
	 */
	private String unitPrice;
	
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
	 * 报名人数（订单中的报名人数）
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


	public String getHireNum() {
		return hireNum;
	}

	public void setHireNum(String hireNum) {
		this.hireNum = hireNum;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getEmpTypeId() {
		return empTypeId;
	}

	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}

	public String getEmpDate() {
		return empDate;
	}

	public void setEmpDate(String empDate) {
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

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCanClosePublish() {
		return canClosePublish;
	}

	public void setCanClosePublish(String canClosePublish) {
		this.canClosePublish = canClosePublish;
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

	public String getSignUsers() {
		return signUsers;
	}

	public void setSignUsers(String signUsers) {
		this.signUsers = signUsers;
	}

	public String getHireNumActural() {
		return hireNumActural;
	}

	public void setHireNumActural(String hireNumActural) {
		this.hireNumActural = hireNumActural;
	}
	
}
