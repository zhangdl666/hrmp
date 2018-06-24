package com.platform.app.service;

import javax.xml.rpc.ServiceException;

import org.springframework.context.ApplicationContext;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

public class AppServicePublish extends ServletEndpointSupport {
	private ApplicationContext context;
	private AppService appService;
	
	protected void onInit() throws ServiceException {
		context = super.getApplicationContext();
		Object obj = context.getBean("appService");
		appService = (AppService) obj;
	}
	
	/**
	 * 获取短信验证码
	 * @param requestXml
	 * @return
	 */
	public String getIdentifyCode(String requestXml){
		return appService.getIdentifyCode(requestXml);
	}
	
	/**
	 * 获取分公司
	 * @param requestXml
	 * @return
	 */
	public String getCompanyList(String requestXml){
		return appService.getCompanyList(requestXml);
	}
	
	/**
	 * 注册
	 * @param requestXml
	 * @return
	 */
	public String userRegister(String requestXml){
		return appService.userRegister(requestXml);
	}
	
	/**
	 * 找回密码
	 * @param requestXml
	 * @return
	 */
	public String retrievePwd(String requestXml){
		return appService.retrievePwd(requestXml);
	}
	
	/**
	 * 登录
	 * @param requestXml
	 * @return
	 */
	public String appLogin(String requestXml){
		return appService.appLogin(requestXml);
	}
	
	/**
	 * 获取此用户最近一次发布信息
	 * @param requestXml
	 * @return
	 */
	public String getLastWork(String requestXml){
		return appService.getLastWork(requestXml);
	}
	
	/**
	 * 发布招工信息
	 * @param requestXml
	 * @return
	 */
	public String publishWork(String requestXml){
		return appService.publishWork(requestXml);
	}
	
	/**
	 * 查询招工信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkList(String requestXml){
		return appService.getWorkList(requestXml);
	}
	
	
	/**
	 * 招工详情
	 * @param requestXml
	 * @return
	 */
	public String getWorkDetail(String requestXml){
		return appService.getWorkDetail(requestXml);
	}
	
	/**
	 * 报名列表
	 * @param requestXml
	 * @return
	 */
	public String getSignEmpList(String requestXml){
		return appService.getSignEmpList(requestXml);
	}
	
	/**
	 * 报名
	 * @param requestXml
	 * @return
	 */
	public String sign(String requestXml){
		return appService.sign(requestXml);
	}
	
	/**
	 * 取消报名
	 * @param requestXml
	 * @return
	 */
	public String cancelSign(String requestXml){
		return appService.cancelSign(requestXml);
	}
	
	/**
	 * 消息列表
	 * @param requestXml
	 * @return
	 */
	public String getMessageList(String requestXml){
		return appService.getMessageList(requestXml);
	}
	
	/**
	 * 消息详情
	 * @param requestXml
	 * @return
	 */
	public String getMessageInfo(String requestXml){
		return appService.getMessageInfo(requestXml);
	}
	
	/**
	 * 我的报名
	 * @param requestXml
	 * @return
	 */
	public String getMySignList(String requestXml){
		return appService.getMySignList(requestXml);
	}
	
	/**
	 * 修改密码
	 * @param requestXml
	 * @return
	 */
	public String modifyPassword(String requestXml){
		return appService.modifyPassword(requestXml);
	}
	
	/**
	 * 查询Android版本信息
	 * @param requestXml
	 * @return
	 */
	public String queryAndroidVersion(String requestXml){
		return appService.queryAndroidVersion(requestXml);
	}
	
	/**
	 * 查询微信支付结果
	 * @param requestXml
	 * @return
	 */
	public String queryWXPayResult(String requestXml){
		return appService.queryWXPayResult(requestXml);
	}
	
	/**
	 * 发起微信支付
	 * @param requestXml
	 * @return
	 */
	public String wxPay(String requestXml){
		return appService.wxPay(requestXml);
	}
	
	/**
	 * 获取工种列表，用于发布招工信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkKindList(String requestXml){
		return appService.getWorkKindList(requestXml);
	}
	
	/**
	 * 报名详情
	 * @param requestXml
	 * @return
	 */
	public String getSignDetail(String requestXml){
		return appService.getSignDetail(requestXml);
	}
	
	/**
	 * 我的发布
	 * @param requestXml
	 * @return
	 */
	public String getMyPublishList(String requestXml){
		return appService.getMyPublishList(requestXml);
	}
	
	/**
	 * 发布详情
	 * @param requestXml
	 * @return
	 */
	public String getPublishDetail(String requestXml){
		return appService.getPublishDetail(requestXml);
	}
	
	/**
	 * 关闭发布
	 * @param requestXml
	 * @return
	 */
	public String closePublish(String requestXml){
		return appService.closePublish(requestXml);
	}
	
	/**
	 * 取消关闭发布
	 * @param requestXml
	 * @return
	 */
	public String querySignCount(String requestXml){
		return appService.querySignCount(requestXml);
	}
	
	/**
	 * 违规记录列表
	 * @param requestXml
	 * @return
	 */
	public String getMyBadRecordList(String requestXml){
		return appService.getMyBadRecordList(requestXml);
	}
	
	/**
	 * 置顶
	 * @param requestXml
	 * @return
	 */
	public String toTop(String requestXml) {
		return appService.toTopWorkHire(requestXml);
	}
	
	/**
	 * 广告列表
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementList(String requestXml){
		return appService.getAdvertisementList(requestXml);
	}
	
	/**
	 * 我发布的广告
	 * @param requestXml
	 * @return
	 */
	public String getMyAdvertisementList(String requestXml){
		return appService.getMyAdvertisementList(requestXml);
	}
	
	/**
	 * 广告详情
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementDetail(String requestXml){
		return appService.getAdvertisementDetail(requestXml);
	}
	
	/**
	 * 发起微信支付（广告）
	 * @param requestXml
	 * @return
	 */
	public String wxPayAdvertisement(String requestXml){
		return appService.wxPayAdvertisement(requestXml);
	}
	
	public String publishAdvertisement(String requestXml){
		return appService.publishAdvertisement(requestXml);
	}
}
