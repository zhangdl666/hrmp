package com.platform.business.service;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.pojo.Message;
import com.platform.core.bo.Page;

public interface BusinessMessageService {
	
	public Message saveMessage(Message message);

	public MessageBo getMessage(String id);
	
	public boolean readMessage(String id);

	public Page queryMessages(MessageQueryBo bo, Page page);
}
