package com.platform.app.service.xmlpo;

public class ReqMsg {

	/**
	 * �����˵�¼��
	 */
	private String operater;
	
	/**
	 * ��ݼ���
	 */
	private String identification;
	
	/**
	 * ��������
	 */
	private ReqDetail reqDetail;

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

	public ReqDetail getReqDetail() {
		return reqDetail;
	}

	public void setReqDetail(ReqDetail reqDetail) {
		this.reqDetail = reqDetail;
	}
}
