package com.platform.app.service;

public interface AppService {

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
	public String getWorkDetailList(String requestXml);
	
	/**
	 * �й�������Ϣ
	 * @param requestXml
	 * @return
	 */
	public String getWorkInfoById(String requestXml);
	
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
}
