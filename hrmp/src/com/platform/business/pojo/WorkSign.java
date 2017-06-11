package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WORK_SIGN")
public class WorkSign {
	public static final String COMFIRM_RESULT_PASS = "pass";
	public static final String COMFIRM_RESULT_NOPASS = "noPass";
	public static final String COMFIRM_RESULT_CANCEL = "cancel";
	public static final String COMFIRM_RESULT_APPROVING = "approving";
	public static final String COMFIRM_RESULT_CALL_OUT = "callOut";
	public static final String COMFIRM_RESULT_CALL_IN = "callIn";

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;

	@Column(name="WORK_HIRE_ID")
	private String workHireId;

	@Column(name="EMP_ID")
	private String empId;

	@Column(name="SIGN_TIME")
	private Date signTime;

	@Column(name="CONFIRM_RESULT")
	private String confirmResult;//pass：通过；noPass：未通过；cancel：取消
	
	@Column(name="CONFIRM_DESCRI")
	private String confirmDescri;
	
	@Column(name="CONFIRM_TIME")
	private Date confirmTime;
	
	@Column(name="NUM")
	private int num;//报名人数
	
	@Column(name="UNITPRICE")
	private double unitPrice;
	
	@Column(name="TOTAL_MONEY")
	private double totalMoney;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkHireId() {
		return workHireId;
	}

	public void setWorkHireId(String workHireId) {
		this.workHireId = workHireId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getConfirmResult() {
		return confirmResult;
	}

	public void setConfirmResult(String confirmResult) {
		this.confirmResult = confirmResult;
	}

	public String getConfirmDescri() {
		return confirmDescri;
	}

	public void setConfirmDescri(String confirmDescri) {
		this.confirmDescri = confirmDescri;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
}
