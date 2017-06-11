package com.platform.business.bo;

import java.util.Date;

import com.platform.organization.pojo.OrgUser;

public class WorkHireVisitBo {
	
	private String id;
	
	private OrgUser visitUser;
	
	private Date visitTime;
	
	private String visitRecord;
	
	private String workHireId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrgUser getVisitUser() {
		return visitUser;
	}

	public void setVisitUser(OrgUser visitUser) {
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
