package com.platform.app.service.xmlpo;

import java.util.Date;

public class Msg {
	
	/**
	 * 消息id
	 */
	private String id;
	
	/**
	 * 发送时间
	 */
	private String createTime;
	
	/**
	 * 发送人
	 */
	private String sendUserName;

	/**
	 * 消息标题
	 */
	private String messageTitle;
	
	/**
	 * 消息内容
	 */
	private String messageContent;
	
	/**
	 * 是否已读，1：已读；0：未读
	 */
	private String isRead;
	
	private String readTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	
	
}
