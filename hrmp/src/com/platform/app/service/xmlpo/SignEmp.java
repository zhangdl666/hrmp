package com.platform.app.service.xmlpo;

import java.util.Date;

public class SignEmp {
	
	private String workId;
	
	private String empId;

	/**
	 * ����ʱ��
	 */
	private String signTime;

	/**
	 * ��������
	 */
	private String empName;
	
	/**
	 * ��������
	 */
	private String num;
	
	/**
	 * �����ѵ��ۣ���λ��Ԫ/��
	 */
	private String unitPrice;
	
	/**
	 * �ܽ��
	 */
	private String totalMoney;


	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

}
