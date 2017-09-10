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
	private int hireNum;
	
	private String status;//noPublish���ݸ壬publishing�������й���closed���ѹرգ�delete��ɾ��
	
	/**
	 * ����ʱ��
	 */
	private String signTime;
	
	
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	/**
	 * �����ѵ��ۣ���λ��Ԫ/�ˣ����ڡ���Ҫ����������ҳ��
	 */
	private double unitPrice;
	
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
	 * ��������
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
