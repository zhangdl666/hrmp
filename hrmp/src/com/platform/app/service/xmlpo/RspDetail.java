package com.platform.app.service.xmlpo;

import java.util.List;


public class RspDetail {

	/**
	 * �û�id
	 */
	private String userId;
	
	/**
	 * �û���¼��
	 */
	private String userLoginName;
	
	/**
	 * �û�����
	 */
	private String userName;
	
	/**
	 * ��׿�ͻ��˰汾��Ϣ
	 */
	private AndroidVersion androidVersion;
	
	/**
	 * ΢��֧�������1����֧����0��δ֧��
	 */
	private String payStatus;
	
	/**
	 * ֧���������
	 */
	private String payDescri;
	
	/**
	 * �й��б�
	 */
	private List<Work> workList;
	
	private Work work;
	
	private List<SignEmp> signEmpList;
	
	private List<Msg> msgList;
	
	private Msg msg;
	
	private WXPay wxPay;
	
	public WXPay getWxPay() {
		return wxPay;
	}

	public void setWxPay(WXPay wxPay) {
		this.wxPay = wxPay;
	}

	public AndroidVersion getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(AndroidVersion androidVersion) {
		this.androidVersion = androidVersion;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayDescri() {
		return payDescri;
	}

	public void setPayDescri(String payDescri) {
		this.payDescri = payDescri;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Work> getWorkList() {
		return workList;
	}

	public void setWorkList(List<Work> workList) {
		this.workList = workList;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public List<SignEmp> getSignEmpList() {
		return signEmpList;
	}

	public void setSignEmpList(List<SignEmp> signEmpList) {
		this.signEmpList = signEmpList;
	}

	public List<Msg> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<Msg> msgList) {
		this.msgList = msgList;
	}

	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

}
