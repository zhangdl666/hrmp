package com.platform.business.service;

import java.util.List;

import com.platform.business.dao.AdvertisementDao;
import com.platform.business.pojo.Advertisement;
import com.platform.core.bo.Page;

public class AdvertisementServiceImpl implements AdvertisementService {

	private AdvertisementDao advertisementDao;
	
	
	public AdvertisementDao getAdvertisementDao() {
		return advertisementDao;
	}

	public void setAdvertisementDao(AdvertisementDao advertisementDao) {
		this.advertisementDao = advertisementDao;
	}

	@Override
	public Advertisement getAdvertisement(String id) {
		return advertisementDao.getAdvertisement(id);
	}

	@Override
	public Advertisement saveAdvertisement(Advertisement adver) {
		return advertisementDao.saveAdvertisement(adver);
	}

	@Override
	public Page queryMyAdvertisementList(String userId,
			Advertisement advertisement, String keyword, Page page) {
		return advertisementDao.queryMyAdvertisementList(userId, advertisement, keyword, page);
	}

	@Override
	public Page queryAdvertisementList(String loginName,Advertisement advertisement,
			String keyword, Page page) {
		return advertisementDao.queryAdvertisementList(loginName,advertisement, keyword, page);
	}

	@Override
	public List<Advertisement> getNoPayList() {
		return advertisementDao.getNoPayList();
	}

	@Override
	public void closeOverTimeAdvertisement() {
		advertisementDao.closeOverTimeAdvertisement();
	}

	@Override
	public void deleteAdvertisement(Advertisement adver) {
		advertisementDao.deleteAdvertisement(adver);
	}

}
