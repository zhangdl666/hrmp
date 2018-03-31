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

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;

	@Column(name="WORK_HIRE_ID")
	private String workHireId;

	/*  */
	@Column(name="EMP_ID")
	private String empId;

	/* 报名时间 */
	@Column(name="SIGN_TIME")
	private Date signTime;

	@Column(name="NUM")
	private int num;//报名人数
	
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
	
}
