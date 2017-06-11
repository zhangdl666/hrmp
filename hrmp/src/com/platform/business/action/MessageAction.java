package com.platform.business.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.service.BusinessMessageService;
import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgUser;

public class MessageAction extends BaseAction {
	private final Logger logger = Logger.getLogger(MessageAction.class);
	
	private String message;
	private String id;
	private MessageBo messageBo;
	private MessageQueryBo messageQueryBo;
	private List<MessageBo> messageList;
	private BusinessMessageService businessMessageService;

	private Page page;	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageBo getMessageBo() {
		return messageBo;
	}

	public void setMessageBo(MessageBo messageBo) {
		this.messageBo = messageBo;
	}

	public BusinessMessageService getBusinessMessageService() {
		return businessMessageService;
	}

	public void setBusinessMessageService(
			BusinessMessageService businessMessageService) {
		this.businessMessageService = businessMessageService;
	}
	
	public List<MessageBo> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageBo> messageList) {
		this.messageList = messageList;
	}
	
	public MessageQueryBo getMessageQueryBo() {
		return messageQueryBo;
	}

	public void setMessageQueryBo(MessageQueryBo messageQueryBo) {
		this.messageQueryBo = messageQueryBo;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * 未读消息
	 * @return
	 */
	public String myUnreadMessage(){
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(messageQueryBo == null) {
			messageQueryBo = new MessageQueryBo();
		}
		//设置查询条件，用于数据隔离
		OrgUser loginUser = getLoginUser();
		messageQueryBo.setIsRead("0");//未读
		messageQueryBo.setReceiverUserId(loginUser.getId());//接收人为当前登录用户
		
		page = businessMessageService.queryMessages(messageQueryBo, page);
		messageList = page.getResult();
		return SUCCESS;
	}
	
	/**
	 * 已读消息
	 * @return
	 */
	public String myReadMessage(){
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(messageQueryBo == null) {
			messageQueryBo = new MessageQueryBo();
		}
		//设置查询条件，用于数据隔离
		OrgUser loginUser = getLoginUser();
		messageQueryBo.setIsRead("1");//未读
		messageQueryBo.setReceiverUserId(loginUser.getId());//接收人为当前登录用户
		
		page = businessMessageService.queryMessages(messageQueryBo, page);
		messageList = page.getResult();
		return SUCCESS;
	}
	
	public String readMessage(){
		businessMessageService.readMessage(id);
		messageBo = businessMessageService.getMessage(id);
		return SUCCESS;
	}
	
	public String viewMessage(){
		messageBo = businessMessageService.getMessage(id);
		return SUCCESS;
	}
}
