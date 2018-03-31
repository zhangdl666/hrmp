package com.platform.business.service;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.pojo.Message;
import com.platform.business.pojo.WorkHire;
import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgUser;

public interface BusinessMessageService {
	
	public Message saveMessage(Message message);

	public MessageBo getMessage(String id);
	
	public boolean readMessage(String id);

	public Page queryMessages(MessageQueryBo bo, Page page);
	
	/**
	 * 招工关闭时通知客服人员
	 * @param wh
	 * @param user
	 * @throws Exception 
	 */
	public void notifyAdminAsClosePublish(WorkHire wh,OrgUser user) throws Exception;
}
