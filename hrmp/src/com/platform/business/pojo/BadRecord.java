package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="BAD_RECORD")
public class BadRecord {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name="RECORD_USER_ID")
	private String recordUserId;
	
	@Column(name="RECORD_TIME")
	private Date recordTime;
	
	@Column(name="RECORD_USER_NAME")
	private String recordUserName;
	
	@Column(name="BAD_USER_ID")
	private String badUserId;
	
	@Column(name="DESCRI")
	private String descri;
	
	@Column(name="WORK_SIGN_ID")
	private String workSignId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecordUserId() {
		return recordUserId;
	}

	public void setRecordUserId(String recordUserId) {
		this.recordUserId = recordUserId;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getRecordUserName() {
		return recordUserName;
	}

	public void setRecordUserName(String recordUserName) {
		this.recordUserName = recordUserName;
	}

	public String getBadUserId() {
		return badUserId;
	}

	public void setBadUserId(String badUserId) {
		this.badUserId = badUserId;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public String getWorkSignId() {
		return workSignId;
	}

	public void setWorkSignId(String workSignId) {
		this.workSignId = workSignId;
	}

}
