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
	
	//����ַ���
	private String nonceStr;
	
	//ʱ���
	private String timestamp;
	
	//ǩ��
	private String sign;

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

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
