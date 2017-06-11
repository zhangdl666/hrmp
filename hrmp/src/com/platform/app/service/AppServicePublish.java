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
	 * 登录
	 * @param requestXml
	 * @return
	 */
	public String appLogin(String requestXml){
		return appService.appLogin(requestXml);
	}
	
	/**
	 * 查询招工信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkList(String requestXml){
		return appService.getWorkList(requestXml);
	}
	
	public String getWorkDetailList(String requestXml){
		return appService.getWorkDetailList(requestXml);
	}
	
	/**
	 * 招工基本信息
	 * @param requestXml
	 * @return
	 */
	public String getWorkInfoById(String requestXml){
		return appService.getWorkInfoById(requestXml);
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
}
