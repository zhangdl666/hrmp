package com.platform.app.service.xmlpo;

import java.util.Date;

public class Msg {
	
	/**
	 * ��Ϣid
	 */
	private String id;
	
	/**
	 * ����ʱ��
	 */
	private String createTime;
	
	/**
	 * ������
	 */
	private String sendUserName;

	/**
	 * ��Ϣ����
	 */
	private String messageTitle;
	
	/**
	 * ��Ϣ����
	 */
	private String messageContent;
	
	/**
	 * �Ƿ��Ѷ���1���Ѷ���0��δ��
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
