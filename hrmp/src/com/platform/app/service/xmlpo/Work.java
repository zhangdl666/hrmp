package com.platform.app.service.xmlpo;



public class Work {
	
	private String id;
	
	/**
	 * ����
	 */
	private String businessNumber;
	
	/**
	 * ������
	 */
	private String publisherName;
	
	/**
	 * ���������ڹ�˾
	 */
	private String publisherCompanyName;
	
	/**
	 * ����ʱ��
	 */
	private String createTime;
	
	/**
	 * ����
	 */
	private String title;
	
	/**
	 *�ù���λ
	 */
	private String workCompany;
	
	/**
	 * ����ʱ��
	 */
	private String publishTime;
	
	/**
	 * �ر�ʱ��
	 */
	private String closeTime;
	
	/**
	 * �����ص�
	 */
	private String workArea;
	
	/**
	 * ��������
	 */
	private String workDescri;
	
	/**
	 * ��Ҫ����
	 */
	private String workKind;
	
	/**
	 * Ԥ���ù���ʼʱ��
	 */
	private String planStartTime;
	
	/**
	 * Ԥ���ù�����ʱ��
	 */
	private String planEndTime;
	
	/**
	 * �й�����
	 */
	private String hireNum;
	
	/**
	 * ʵ�����й�����
	 */
	private String hireNumActural;
	
	private String status;//noPublish���ݸ壬publishing�������й���closed���ѹرգ�delete��ɾ��
	
	/**
	 * ����ʱ��
	 */
	private String signTime;
	
	/* �й����� */
	private String empTypeId;
	
	/* �й�ʱ�� */
	private String empDate;
	
	/* �Ա�male����
	��female��Ů��
	all������
	 */
	private String sex;
	
	private String salary;
	
	/* ���ʱ�ע  GF���ܷ�
	��GCZ���ܳ�ס��
	BGF�����ܷ�
	 */
	private String salaryRemark;
	
	/* �а�ʩ��-����Ҫ�� */
	private String age;
	
	/* ���磬1��ѡ�У�0��δѡ�� */
	private String am;
	
	/* ���翪ʼʱ�� */
	private String amStart;
	
	/* �����ֹʱ�� */
	private String amEnd;
	
	/* ���磬1��ѡ�У�0��δѡ�� */
	private String pm;
	
	/* ���翪ʼʱ�� */
	private String pmStart;
	
	/* �����ֹʱ�� */
	private String pmEnd;
	
	/* ���ϣ�1��ѡ�У�0��δѡ�� */
	private String night;
	
	/* ���Ͽ�ʼʱ�� */
	private String nightStart;
	
	/* ���Ͻ�ֹʱ�� */
	private String nightEnd;
	
	/* �а�ʩ��-����Ҫ�� */
	private String condition;
	
	/* �а�ʩ��-���ʽ��1�����飻0������ */
	private String payMode;
	
	/* �а�ʩ��-���ʽ-�������ʽ */
	private String payModeRemark;
	
	/*�Ƿ��ܹرշ���	1�����Թرշ�����0�������Թرշ���*/
	private String canClosePublish;
	
	/* ��ϵ������ */
	private String contactUser;
	
	/* ��ϵ�˵绰 */
	private String contactUserPhone;
	
	/**
	 * �����ˣ�����á���������
	 */
	private String signUsers;
	
	
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	/**
	 * �����ѵ��ۣ���λ��Ԫ/�ˣ����ڡ���Ҫ����������ҳ��
	 */
	private String unitPrice;
	
	/**
	 * �Ƿ���ȡ������ 
	 * 1������ȡ��������
	 * 0��������ȡ������
	 */
	private String canCancelSign;
	
	/**
	 * ֧��״̬	1��֧���ɹ���0����֧��
	 */
	private String payStatus;
	
	/**
	 * Ԥ֧��id
	 */
	private String prepayId;
	
	/**
	 * ���������������еı���������
	 */
	private String num;
	
	/**
	 * ֧�����	��λ��Ԫ
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
