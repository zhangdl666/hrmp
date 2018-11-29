package com.platform.business.service;

import java.util.List;

import com.platform.business.pojo.Advertisement;
import com.platform.core.bo.Page;

public interface AdvertisementService {

	public Advertisement getAdvertisement(String id);
	
	public Advertisement saveAdvertisement(Advertisement adver);
	
	/**
	 * ����� + 1
	 * @param id
	 */
	public void click(String id);
	
	/**
	 * ��ѯ�ҷ����Ĺ���б�
	 * @param userId
	 * @param advertisement
	 * @param page
	 * @return
	 */
	public Page queryMyAdvertisementList(String userId,Advertisement advertisement,String keyword,Page page);
	
	/**
	 * ��ѯ��Ч����б�
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryAdvertisementList(String loginName,Advertisement advertisement,String keyword, Page page);
	
	/**
	 * ��������δ֧������
	 * @return
	 */
	public List<Advertisement> getNoPayList();
	
	public void queryWXPayResultFromWX();
	
	/**
	 * �رճ�ʱ���
	 */
	public void closeOverTimeAdvertisement();
	
	public void deleteAdvertisement(Advertisement adver);
}
