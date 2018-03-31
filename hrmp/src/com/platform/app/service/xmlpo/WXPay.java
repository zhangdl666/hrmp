package com.platform.app.service.xmlpo;
/**
 * 微信支付参数对象
 * @author Administrator
 *
 */
public class WXPay {

	//应用ID
	private String appId;
	
	//商户号
	private String partnerId;
	
	//预支付交易会话ID
	private String prepayId;
	
	//随机字符串
	private String nonceStr;
	
	//时间戳
	private String timestamp;
	
	//签名
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
