package com.platform.app.service;

import com.platform.business.pojo.WorkHire;

public interface AppService {
	
	/**
	 * 获取短信验证码
	 * @param requestXml
	 * @return
	 */
	public String getIdentifyCode(String requestXml);
	
	/**
	 * 获取分公司
	 * @param requestXml
	 * @return
	 */
	public String getCompanyList(String requestXml);
	
	/**
	 * 注册
	 * @param requestXml
	 * @return
	 */
	public String userRegister(String requestXml);
	
	/**
	 * 找回密码
	 * @param requestXml
	 * @return
	 */
	public String retrievePwd(String requestXml);
	
	
	/**
	 * 获取此用户最近一次发布信息
	 * @param requestXml
	 * @return
	 */
	public String getLastWork(String requestXml);
	
	/**
	 * 发布招工信息
	 * @param requestXml
	 * @return
	 */
	public String publishWork(String requestXml);
	
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
	public String getWorkDetail(String requestXml);
	
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
	
	/**
	 * 发起微信支付
	 * @param requestXml
	 * @return
	 */
	public String wxPay(String requestXml);
	
	/**
	 * 获取工种列表，用于发布招工信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkKindList(String requestXml);
	
	/**
	 * 报名详情
	 * @param requestXml
	 * @return
	 */
	public String getSignDetail(String requestXml);
	
	/**
	 * 我的发布
	 * @param requestXml
	 * @return
	 */
	public String getMyPublishList(String requestXml);
	
	/**
	 * 发布详情
	 * @param requestXml
	 * @return
	 */
	public String getPublishDetail(String requestXml);
	
	/**
	 * 关闭发布
	 * @param requestXml
	 * @return
	 */
	public String closePublish(String requestXml);
	
	/**
	 * 查询报名人数
	 * @param requestXml
	 * @return
	 */
	public String querySignCount(String requestXml);
	
	/**
	 * 违规记录列表
	 * @param requestXml
	 * @return
	 */
	public String getMyBadRecordList(String requestXml);
	
	/**
	 * 置顶
	 * @param requestXml
	 * @return
	 */
	public String toTopWorkHire(String requestXml);
	
	/**
	 * 广告列表
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementList(String requestXml);
	
	/**
	 * 我发布的广告
	 * @param requestXml
	 * @return
	 */
	public String getMyAdvertisementList(String requestXml);
	
	/**
	 * 广告详情
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementDetail(String requestXml);
	
	/**
	 * 发起微信支付（广告）
	 * @param requestXml
	 * @return
	 */
	public String wxPayAdvertisement(String requestXml);

	public String publishAdvertisement(String requestXml);
	
	public String getAdvertisementUnitPriceList(String requestXml);
	
	/**
	 * 获取临时工工种列表（二级）
	 * @param requestXml
	 * @return
	 */
	public String getLSWorkKindList(String requestXml);
	
	/**
	 * 广告续费
	 * @param requestXml
	 * @return
	 */
	public String continueAdvertisement(String requestXml);
}
