package com.platform.app.service.xmlpo;
/**
 * ΢��֧����������
 * @author Administrator
 *
 */
public class WXPay {

	//Ӧ��ID
	private String appId;
	
	//�̻���
	private String partnerId;
	
	//Ԥ֧�����׻ỰID
	private String prepayId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
}
