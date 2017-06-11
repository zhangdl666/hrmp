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
	 * ��¼
	 * @param requestXml
	 * @return
	 */
	public String appLogin(String requestXml){
		return appService.appLogin(requestXml);
	}
	
	/**
	 * ��ѯ�й���Ϣ
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
	 * �й�������Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getWorkInfoById(String requestXml){
		return appService.getWorkInfoById(requestXml);
	}
	
	/**
	 * �����б�
	 * @param requestXml
	 * @return
	 */
	public String getSignEmpList(String requestXml){
		return appService.getSignEmpList(requestXml);
	}
	
	/**
	 * ����
	 * @param requestXml
	 * @return
	 */
	public String sign(String requestXml){
		return appService.sign(requestXml);
	}
	
	/**
	 * ȡ������
	 * @param requestXml
	 * @return
	 */
	public String cancelSign(String requestXml){
		return appService.cancelSign(requestXml);
	}
	
	/**
	 * ��Ϣ�б�
	 * @param requestXml
	 * @return
	 */
	public String getMessageList(String requestXml){
		return appService.getMessageList(requestXml);
	}
	
	/**
	 * ��Ϣ����
	 * @param requestXml
	 * @return
	 */
	public String getMessageInfo(String requestXml){
		return appService.getMessageInfo(requestXml);
	}
	
	/**
	 * �ҵı���
	 * @param requestXml
	 * @return
	 */
	public String getMySignList(String requestXml){
		return appService.getMySignList(requestXml);
	}
	
	/**
	 * �޸�����
	 * @param requestXml
	 * @return
	 */
	public String modifyPassword(String requestXml){
		return appService.modifyPassword(requestXml);
	}
	
	/**
	 * ��ѯAndroid�汾��Ϣ
	 * @param requestXml
	 * @return
	 */
	public String queryAndroidVersion(String requestXml){
		return appService.queryAndroidVersion(requestXml);
	}
}
