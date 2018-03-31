package com.platform.business.dao;

import com.platform.business.pojo.MMobileMessage;

public interface MobileMessageDao {

	public MMobileMessage saveMobileMessage(MMobileMessage message);
	
	public MMobileMessage getMobileMessage(String id);
	
}
