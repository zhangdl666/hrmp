package com.platform.app.service.xmlpo;


public class RspMsg {

	/**
	 * �����˵�¼��
	 */
	private String operater;
	
	/**
	 * ��ݼ���
	 */
	private String identification;
	
	/**
	 * ����ֵ
	 */
	private String rspResult;
	
	/**
	 * ����ֵ������Ϣ
	 */
	private String rspDesc;
	
	/**
	 * ���ر�����
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
