package com.platform.organization.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ORG_USER")
public class OrgUser {

	public static final String USER_KIND_BADCREDIT = "badCredit";
	public static final String USER_KIND_VIP = "VIP";
	public static final String USER_KIND_NORMAL = "Normal";
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name = "LOGINNAME")
	private String loginName;
	
	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "PWD")
	private String pwd;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="BIRTHDAY")
	private Date birthday;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="WORK_KIND")
	private String workKind;
	
	@Column(name = "DEPT_ID")
	private String deptId;
	
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@Column(name = "PWD_UPDATE_TIME")
	private Date pwdUpdateTime;
	
	@Column(name = "VALIDSTATUS")
	private String validstatus;
	
	@Column(name="USER_KIND")
	private String userKind;//badCredit：不良信用、VIP：VIP用户，Normal：普通用户
	
	@Column(name = "REMARK")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWorkKind() {
		return workKind;
	}

	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPwdUpdateTime() {
		return pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}

	public String getValidstatus() {
		return validstatus;
	}

	public void setValidstatus(String validstatus) {
		this.validstatus = validstatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserKind() {
		return userKind;
	}

	public void setUserKind(String userKind) {
		this.userKind = userKind;
	}
	
}
