package com.platform.app.service;

public interface AppService {

	/**
	 * 登录
	 * @param requestXml
	 * @return
	 */
	public String appLogin(String requestXml);
	
	/**
	 * 查询招工信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkList(String requestXml);
	
	/**
	 * 招工详情
	 * @param requestXml
	 * @return
	 */
	public String getWorkDetailList(String requestXml);
	
	/**
	 * 招工基本信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkInfoById(String requestXml);
	
	/**
	 * 报名列表
	 * @param requestXml
	 * @return
	 */
	public String getSignEmpList(String requestXml);
	
	/**
	 * 报名
	 * @param requestXml
	 * @return
	 */
	public String sign(String requestXml);
	
	/**
	 * 取消报名
	 * @param requestXml
	 * @return
	 */
	public String cancelSign(String requestXml);
	
	/**
	 * 消息列表
	 * @param requestXml
	 * @return
	 */
	public String getMessageList(String requestXml);
	
	/**
	 * 消息详情
	 * @param requestXml
	 * @return
	 */
	public String getMessageInfo(String requestXml);
	
	/**
	 * 我的报名
	 * @param requestXml
	 * @return
	 */
	public String getMySignList(String requestXml);
	
	/**
	 * 修改密码
	 * @param requestXml
	 * @return
	 */
	public String modifyPassword(String requestXml);
	
	/**
	 * 查询Android版本信息
	 * @param requestXml
	 * @return
	 */
	public String queryAndroidVersion(String requestXml);
	
	/**
	 * 查询微信支付结果
	 * @param requestXml
	 * @return
	 */
	public String queryWXPayResult(String requestXml);
}
