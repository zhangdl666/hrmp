
package com.platform.business.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="M_IDENTIFYCODE")
public class MIdentifyCode {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="IDENTIFYCODE")
	private String identifyCode;
	
	@Column(name="IDENTIFYCODE_TYPE")
	private String identifyCodeType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdentifyCode() {
		return identifyCode;
	}

	public void setIdentifyCode(String identifyCode) {
		this.identifyCode = identifyCode;
	}

	public String getIdentifyCodeType() {
		return identifyCodeType;
	}

	public void setIdentifyCodeType(String identifyCodeType) {
		this.identifyCodeType = identifyCodeType;
	}

}
