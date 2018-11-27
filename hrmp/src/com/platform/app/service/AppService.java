package com.platform.app.service;

import com.platform.business.pojo.WorkHire;

public interface AppService {
	
	/**
	 * ��ȡ������֤��
	 * @param requestXml
	 * @return
	 */
	public String getIdentifyCode(String requestXml);
	
	/**
	 * ��ȡ�ֹ�˾
	 * @param requestXml
	 * @return
	 */
	public String getCompanyList(String requestXml);
	
	/**
	 * ע��
	 * @param requestXml
	 * @return
	 */
	public String userRegister(String requestXml);
	
	/**
	 * �һ�����
	 * @param requestXml
	 * @return
	 */
	public String retrievePwd(String requestXml);
	
	
	/**
	 * ��ȡ���û����һ�η�����Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getLastWork(String requestXml);
	
	/**
	 * �����й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String publishWork(String requestXml);
	
	/**
	 * ��¼
	 * @param requestXml
	 * @return
	 */
	public String appLogin(String requestXml);
	
	/**
	 * ��ѯ�й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getWorkList(String requestXml);
	
	
	/**
	 * �й�����
	 * @param requestXml
	 * @return
	 */
	public String getWorkDetail(String requestXml);
	
	/**
	 * �����б�
	 * @param requestXml
	 * @return
	 */
	public String getSignEmpList(String requestXml);
	
	/**
	 * ����
	 * @param requestXml
	 * @return
	 */
	public String sign(String requestXml);
	
	/**
	 * ȡ������
	 * @param requestXml
	 * @return
	 */
	public String cancelSign(String requestXml);
	
	/**
	 * ��Ϣ�б�
	 * @param requestXml
	 * @return
	 */
	public String getMessageList(String requestXml);
	
	/**
	 * ��Ϣ����
	 * @param requestXml
	 * @return
	 */
	public String getMessageInfo(String requestXml);
	
	/**
	 * �ҵı���
	 * @param requestXml
	 * @return
	 */
	public String getMySignList(String requestXml);
	
	/**
	 * �޸�����
	 * @param requestXml
	 * @return
	 */
	public String modifyPassword(String requestXml);
	
	/**
	 * ��ѯAndroid�汾��Ϣ
	 * @param requestXml
	 * @return
	 */
	public String queryAndroidVersion(String requestXml);
	
	/**
	 * ��ѯ΢��֧�����
	 * @param requestXml
	 * @return
	 */
	public String queryWXPayResult(String requestXml);
	
	/**
	 * ����΢��֧��
	 * @param requestXml
	 * @return
	 */
	public String wxPay(String requestXml);
	
	/**
	 * ��ȡ�����б����ڷ����й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getWorkKindList(String requestXml);
	
	/**
	 * ��������
	 * @param requestXml
	 * @return
	 */
	public String getSignDetail(String requestXml);
	
	/**
	 * �ҵķ���
	 * @param requestXml
	 * @return
	 */
	public String getMyPublishList(String requestXml);
	
	/**
	 * ��������
	 * @param requestXml
	 * @return
	 */
	public String getPublishDetail(String requestXml);
	
	/**
	 * �رշ���
	 * @param requestXml
	 * @return
	 */
	public String closePublish(String requestXml);
	
	/**
	 * ��ѯ��������
	 * @param requestXml
	 * @return
	 */
	public String querySignCount(String requestXml);
	
	/**
	 * Υ���¼�б�
	 * @param requestXml
	 * @return
	 */
	public String getMyBadRecordList(String requestXml);
	
	/**
	 * �ö�
	 * @param requestXml
	 * @return
	 */
	public String toTopWorkHire(String requestXml);
	
	/**
	 * ����б�
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementList(String requestXml);
	
	/**
	 * �ҷ����Ĺ��
	 * @param requestXml
	 * @return
	 */
	public String getMyAdvertisementList(String requestXml);
	
	/**
	 * �������
	 * @param requestXml
	 * @return
	 */
	public String getAdvertisementDetail(String requestXml);
	
	/**
	 * ����΢��֧������棩
	 * @param requestXml
	 * @return
	 */
	public String wxPayAdvertisement(String requestXml);

	public String publishAdvertisement(String requestXml);
	
	public String getAdvertisementUnitPriceList(String requestXml);
	
	/**
	 * ��ȡ��ʱ�������б�������
	 * @param requestXml
	 * @return
	 */
	public String getLSWorkKindList(String requestXml);
	
	/**
	 * �������
	 * @param requestXml
	 * @return
	 */
	public String continueAdvertisement(String requestXml);
}
