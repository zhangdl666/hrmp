package com.platform.business.service;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.dao.BusinessMessageDao;
import com.platform.business.pojo.Message;
import com.platform.core.bo.Page;

public class BusinessMessageServiceImpl implements BusinessMessageService {
	
	private BusinessMessageDao businessMessageDao;

	public BusinessMessageDao getBusinessMessageDao() {
		return businessMessageDao;
	}

	public void setBusinessMessageDao(BusinessMessageDao businessMessageDao) {
		this.businessMessageDao = businessMessageDao;
	}

	@Override
	public Message saveMessage(Message message) {
		return businessMessageDao.saveMessage(message);
	}

	@Override
	public MessageBo getMessage(String id) {
		return businessMessageDao.getMessage(id);
	}

	@Override
	public Page queryMessages(MessageQueryBo bo, Page page) {
		return businessMessageDao.queryMessages(bo, page);
	}

	@Override
	public boolean readMessage(String id) {
		return businessMessageDao.readMessage(id);
	}

}
