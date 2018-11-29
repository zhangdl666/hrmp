package com.platform.business.service;

import java.util.List;

import com.platform.business.pojo.Advertisement;
import com.platform.core.bo.Page;

public interface AdvertisementService {

	public Advertisement getAdvertisement(String id);
	
	public Advertisement saveAdvertisement(Advertisement adver);
	
	/**
	 * 点击量 + 1
	 * @param id
	 */
	public void click(String id);
	
	/**
	 * 查询我发布的广告列表
	 * @param userId
	 * @param advertisement
	 * @param page
	 * @return
	 */
	public Page queryMyAdvertisementList(String userId,Advertisement advertisement,String keyword,Page page);
	
	/**
	 * 查询有效广告列表
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryAdvertisementList(String loginName,Advertisement advertisement,String keyword, Page page);
	
	/**
	 * 查找所有未支付订单
	 * @return
	 */
	public List<Advertisement> getNoPayList();
	
	public void queryWXPayResultFromWX();
	
	/**
	 * 关闭超时广告
	 */
	public void closeOverTimeAdvertisement();
	
	public void deleteAdvertisement(Advertisement adver);
}
