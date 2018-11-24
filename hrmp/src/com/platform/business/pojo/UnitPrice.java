package com.platform.business.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="UNITPRICE")
public class UnitPrice {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name="DPET_ID")
	private String deptId;
	
	@Column(name="EMP_TYPE_ID")
	private String empTypeId;
	
	@Column(name="PRICE")
	private Double price;

	@Column(name="MONTHS")
	private String months;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getEmpTypeId() {
		return empTypeId;
	}

	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	
	
}
