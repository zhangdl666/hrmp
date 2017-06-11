package com.platform.business.bo;

import java.util.Date;

public class MessageQueryBo {

	private Date createTimeFrom;
	
	private Date createTimeEnd;

	private String sendUserName;
	
	private String sendUserId;

	private String receiverUserName;
	
	private String receiverUserId;

	private String messageTitle;

	private String messageContent;

	private String isRead;

	private Date readTimeFrom;
	
	private Date readTimeEnd;

	public Date getCreateTimeFrom() {
		return createTimeFrom;
	}

	public void setCreateTimeFrom(Date createTimeFrom) {
		this.createTimeFrom = createTimeFrom;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getReceiverUserName() {
		return receiverUserName;
	}

	public void setReceiverUserName(String receiverUserName) {
		this.receiverUserName = receiverUserName;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
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

	public Date getReadTimeFrom() {
		return readTimeFrom;
	}

	public void setReadTimeFrom(Date readTimeFrom) {
		this.readTimeFrom = readTimeFrom;
	}

	public Date getReadTimeEnd() {
		return readTimeEnd;
	}

	public void setReadTimeEnd(Date readTimeEnd) {
		this.readTimeEnd = readTimeEnd;
	}

}
