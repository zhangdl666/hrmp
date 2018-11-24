package com.platform.app.service.xmlpo;

import java.util.List;


public class RspDetail {
	
	private String pageNo;
	
	private String totalPage;
	
	/**
	 * 报名人数
	 */
	private String signCount;

	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 用户登录名
	 */
	private String userLoginName;
	
	/**
	 * 用户姓名
	 */
	private String userName;
	
	/**
	 * 安卓客户端版本信息
	 */
	private AndroidVersion androidVersion;
	
	/**
	 * 微信支付结果，1：已支付；0：未支付
	 */
	private String payStatus;
	
	/**
	 * 支付结果描述
	 */
	private String payDescri;
	
	/**
	 * 招工列表
	 */
	private List<Work> workList;
	
	private String businessNumber;
	
	private Work work;
	
	private List<SignEmp> signEmpList;
	
	private List<Msg> msgList;
	
	private Msg msg;
	
	private List<Province> provinceList;
	
	private List<WorkKind> workKindList;
	
	private List<Advertisement> advertisementList;
	
	private List<UnitPrice> advertisementPriceList;
	
	private Advertisement advertisement;
	
	private List<BadRecord> badRecordList;
	
	private WXPay wxPay;
	
	private String remark;
	
	public WXPay getWxPay() {
		return wxPay;
	}

	public void setWxPay(WXPay wxPay) {
		this.wxPay = wxPay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<Province> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<Province> provinceList) {
		this.provinceList = provinceList;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	public List<WorkKind> getWorkKindList() {
		return workKindList;
	}

	public void setWorkKindList(List<WorkKind> workKindList) {
		this.workKindList = workKindList;
	}

	public List<BadRecord> getBadRecordList() {
		return badRecordList;
	}

	public void setBadRecordList(List<BadRecord> badRecordList) {
		this.badRecordList = badRecordList;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getSignCount() {
		return signCount;
	}

	public void setSignCount(String signCount) {
		this.signCount = signCount;
	}

	public List<Advertisement> getAdvertisementList() {
		return advertisementList;
	}

	public void setAdvertisementList(List<Advertisement> advertisementList) {
		this.advertisementList = advertisementList;
	}

	public Advertisement getAdvertisement() {
		return advertisement;
	}

	public void setAdvertisement(Advertisement advertisement) {
		this.advertisement = advertisement;
	}

	public List<UnitPrice> getAdvertisementPriceList() {
		return advertisementPriceList;
	}

	public void setAdvertisementPriceList(List<UnitPrice> advertisementPriceList) {
		this.advertisementPriceList = advertisementPriceList;
	}

}
