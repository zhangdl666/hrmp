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
	 * ��ȡ������֤��
	 * @param requestXml
	 * @return
	 */
	public String getIdentifyCode(String requestXml){
		return appService.getIdentifyCode(requestXml);
	}
	
	/**
	 * ��ȡ�ֹ�˾
	 * @param requestXml
	 * @return
	 */
	public String getCompanyList(String requestXml){
		return appService.getCompanyList(requestXml);
	}
	
	/**
	 * ע��
	 * @param requestXml
	 * @return
	 */
	public String userRegister(String requestXml){
		return appService.userRegister(requestXml);
	}
	
	/**
	 * �һ�����
	 * @param requestXml
	 * @return
	 */
	public String retrievePwd(String requestXml){
		return appService.retrievePwd(requestXml);
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
	 * ��ȡ���û����һ�η�����Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getLastWork(String requestXml){
		return appService.getLastWork(requestXml);
	}
	
	/**
	 * �����й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String publishWork(String requestXml){
		return appService.publishWork(requestXml);
	}
	
	/**
	 * ��ѯ�й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getWorkList(String requestXml){
		return appService.getWorkList(requestXml);
	}
	
	
	/**
	 * �й�����
	 * @param requestXml
	 * @return
	 */
	public String getWorkDetail(String requestXml){
		return appService.getWorkDetail(requestXml);
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
	
	/**
	 * ��ѯ΢��֧�����
	 * @param requestXml
	 * @return
	 */
	public String queryWXPayResult(String requestXml){
		return appService.queryWXPayResult(requestXml);
	}
	
	/**
	 * ����΢��֧��
	 * @param requestXml
	 * @return
	 */
	public String wxPay(String requestXml){
		return appService.wxPay(requestXml);
	}
	
	/**
	 * ��ȡ�����б����ڷ����й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getWorkKindList(String requestXml){
		return appService.getWorkKindList(requestXml);
	}
	
	/**
	 * ��������
	 * @param requestXml
	 * @return
	 */
	public String getSignDetail(String requestXml){
		return appService.getSignDetail(requestXml);
	}
	
	/**
	 * �ҵķ���
	 * @param requestXml
	 * @return
	 */
	public String getMyPublishList(String requestXml){
		return appService.getMyPublishList(requestXml);
	}
	
	/**
	 * ��������
	 * @param requestXml
	 * @return
	 */
	public String getPublishDetail(String requestXml){
		return appService.getPublishDetail(requestXml);
	}
	
	/**
	 * �رշ���
	 * @param requestXml
	 * @return
	 */
	public String closePublish(String requestXml){
		return appService.closePublish(requestXml);
	}
	
	/**
	 * ȡ���رշ���
	 * @param requestXml
	 * @return
	 */
	public String querySignCount(String requestXml){
		return appService.querySignCount(requestXml);
	}
	
	/**
	 * Υ���¼�б�
	 * @param requestXml
	 * @return
	 */
	public String getMyBadRecordList(String requestXml){
		return appService.getMyBadRecordList(requestXml);
	}
	
	/**
	 * �ö�
	 * @param requestXml
	 * @return
	 */
	public String toTop(String requestXml) {
		return appService.toTopWorkHire(requestXml);
	}
	
	/**
	 * ����б�
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementList(String requestXml){
		return appService.getAdvertisementList(requestXml);
	}
	
	/**
	 * �ҷ����Ĺ��
	 * @param requestXml
	 * @return
	 */
	public String getMyAdvertisementList(String requestXml){
		return appService.getMyAdvertisementList(requestXml);
	}
	
	/**
	 * �������
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementDetail(String requestXml){
		return appService.getAdvertisementDetail(requestXml);
	}
	
	/**
	 * ����΢��֧������棩
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
