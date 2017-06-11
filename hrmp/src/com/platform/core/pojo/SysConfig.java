package com.platform.core.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SYS_CONFIG")
public class SysConfig {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name = "ID")
	private String id;
	
	@Column(name = "CFG_ID")
	private Integer cfgId;
	
	@Column(name = "NAME")
	private String cfgName;
	
	@Column(name = "CFG_DESC")
	private String cfgDesc;
	
	@Column(name = "VAL1")
	private String val1;
	
	@Column(name = "VAL2")
	private String val2;
	
	@Column(name = "VAL3")
	private String val3;
	
	@Column(name = "VAL4")
	private String val4;
	
	@Column(name = "VAL5")
	private String val5;
	
	@Column(name = "VAL6")
	private String val6;
	
	@Column(name = "VAL7")
	private String val7;
	
	@Column(name = "VAL8")
	private String val8;
	
	@Column(name = "VAL9")
	private String val9;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "STATE")
	private Integer state;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getCfgId() {
		return cfgId;
	}
	
	public String getCfgName() {
		return cfgName;
	}
	
	public String getCfgDesc() {
		return cfgDesc;
	}
	
	public String getVal1() {
		return val1;
	}
	public String getVal2() {
		return val2;
	}
	public String getVal3() {
		return val3;
	}
	public String getVal4() {
		return val4;
	}
	public String getVal5() {
		return val5;
	}
	public String getVal6() {
		return val6;
	}
	public String getVal7() {
		return val7;
	}
	public String getVal8() {
		return val8;
	}
	public String getVal9() {
		return val9;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public Integer getState() {
		return state;
	}
	public void setCfgId(Integer cfgId) {
		this.cfgId = cfgId;
	}
	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	public void setVal1(String val1) {
		this.val1 = val1;
	}
	public void setVal2(String val2) {
		this.val2 = val2;
	}
	public void setVal3(String val3) {
		this.val3 = val3;
	}
	public void setVal4(String val4) {
		this.val4 = val4;
	}
	public void setVal5(String val5) {
		this.val5 = val5;
	}
	public void setVal6(String val6) {
		this.val6 = val6;
	}
	public void setVal7(String val7) {
		this.val7 = val7;
	}
	public void setVal8(String val8) {
		this.val8 = val8;
	}
	public void setVal9(String val9) {
		this.val9 = val9;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
