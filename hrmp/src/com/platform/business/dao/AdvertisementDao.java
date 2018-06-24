package com.platform.business.dao;

import java.util.List;

import com.platform.business.pojo.Advertisement;
import com.platform.core.bo.Page;

public interface AdvertisementDao {

	public Advertisement getAdvertisement(String id);
	
	public Advertisement saveAdvertisement(Advertisement adver);
	
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
	
	/**
	 * 关闭超时广告
	 */
	public void closeOverTimeAdvertisement();
	
	public void deleteAdvertisement(Advertisement adver);
	
}
