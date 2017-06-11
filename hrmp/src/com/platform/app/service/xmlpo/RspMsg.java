package com.platform.app.service.xmlpo;


public class RspMsg {

	/**
	 * 操作人登录名
	 */
	private String operater;
	
	/**
	 * 身份鉴定
	 */
	private String identification;
	
	/**
	 * 返回值
	 */
	private String rspResult;
	
	/**
	 * 返回值描述信息
	 */
	private String rspDesc;
	
	/**
	 * 返回报文体
	 */
	private RspDetail rspDetail;

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getRspResult() {
		return rspResult;
	}

	public void setRspResult(String rspResult) {
		this.rspResult = rspResult;
	}

	public String getRspDesc() {
		return rspDesc;
	}

	public void setRspDesc(String rspDesc) {
		this.rspDesc = rspDesc;
	}

	public RspDetail getRspDetail() {
		return rspDetail;
	}

	public void setRspDetail(RspDetail rspDetail) {
		this.rspDetail = rspDetail;
	}
}
