package com.platform.organization.bo;

import java.util.Date;

import com.platform.organization.pojo.OrgDeptView;
public class OrgUserBo {

	private String id;
	
	private String loginName;
	
	private String userName;
	
	private String pwd;
	
	private String gender;
	
	private Date birthday;
	
	private Integer age;
	
	private String mobile;
	
	private String workKind;
	
	private OrgDeptView dept;
	
	private Date createTime;
	
	private Date pwdUpdateTime;
	
	private String validstatus;
	
	private String userKind;//badCredit：不良信用、VIP：VIP用户，Normal：普通用户
	
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public OrgDeptView getDept() {
		return dept;
	}

	public void setDept(OrgDeptView dept) {
		this.dept = dept;
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
