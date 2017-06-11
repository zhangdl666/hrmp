package com.platform.core.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="LOG_INFO")
public class LogInfo {
	
	public static final String OP_RESULT_SUCCESS = "success";
	public static final String OP_RESULT_FAILD = "faild";
	
	public static final Integer LOG_LEVEL_HIGH = 3;
	public static final Integer LOG_LEVEL_MIDDLE = 2;
	public static final Integer LOG_LEVEL_LOW = 1;
	
	public static final String OP_TYPE_LOGIN = "login";
	public static final String OP_TYPE_LOGOUT = "logout";
	/**
	 * 用户添加和权限相关的任何信息，包括添加用户、用户组、权限、角色等
	 */
	public static final String OP_TYPE_ADDPRIVILEGE = "addPrivilege";
	
	/**
	 * 用户删除和权限相关的任何信息，包括添加用户、用户组、权限、角色等
	 */
	public static final String OP_TYPE_DELPRIVILEGE = "delPrivilege";
	
	/**
	 * 用户更新和权限相关的任何信息，包括添加用户、用户组、权限、角色等
	 */
	public static final String OP_TYPE_UPDATEPRIVILEGE = "updatePrivilege";
	
	/**
	 * 业务数据查阅
	 */
	public static final String OP_TYPE_VIEW = "view";
	
	/**
	 * 业务数据更新
	 */
	public static final String OP_TYPE_UPDATE = "update";
	
	/**
	 * 业务数据删除
	 */
	public static final String OP_TYPE_DEL = "del";
	
	/**
	 * 业务数据增加
	 */
	public static final String OP_TYPE_ADD = "add";
	
	/**
	 * 其它类别
	 */
	public static final String OP_TYPE_OTHER = "other";

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name = "LOGTIME")
	private Date logTime;
	
	@Column(name = "LOGINNAME")
	private String loginName;
	
	@Column(name = "IP")
	private String ip;
	
	@Column(name = "BUSINESS_MODULE")
	private String businessModule;
	
	@Column(name = "OP_TYPE")
	private String opType;
	
	@Column(name = "OP_RESULT")
	private String opResult;
	
	@Column(name = "OP_RESULT_DESCRI")
	private String opResultDescri;
	
	@Column(name = "OP_TEXT")
	private String op_text;
	
	@Column(name = "LOG_LEVEL")
	private Integer logLevel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getBusinessModule() {
		return businessModule;
	}

	public void setBusinessModule(String businessModule) {
		this.businessModule = businessModule;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOpResult() {
		return opResult;
	}

	public void setOpResult(String opResult) {
		this.opResult = opResult;
	}

	public String getOpResultDescri() {
		return opResultDescri;
	}

	public void setOpResultDescri(String opResultDescri) {
		this.opResultDescri = opResultDescri;
	}

	public String getOp_text() {
		return op_text;
	}

	public void setOp_text(String op_text) {
		this.op_text = op_text;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}
	
	
}
