package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WORK_HIRE_VISIT")
public class WorkHireVisit {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name="VISIT_USER")
	private String visitUser;
	
	@Column(name="VISIT_TIME")
	private Date visitTime;
	
	@Column(name="VISIT_RECORD")
	private String visitRecord;
	
	@Column(name="WORK_HIRE_ID")
	private String workHireId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisitUser() {
		return visitUser;
	}

	public void setVisitUser(String visitUser) {
		this.visitUser = visitUser;
	}


	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public String getVisitRecord() {
		return visitRecord;
	}

	public void setVisitRecord(String visitRecord) {
		this.visitRecord = visitRecord;
	}

	public String getWorkHireId() {
		return workHireId;
	}

	public void setWorkHireId(String workHireId) {
		this.workHireId = workHireId;
	}
	
}
