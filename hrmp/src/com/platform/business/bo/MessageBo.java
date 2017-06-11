package com.platform.business.bo;

import java.util.Date;

import com.platform.organization.pojo.OrgUser;

public class MessageBo {

	private String id;

	private Date createTime;

	private OrgUser sendUser;

	private OrgUser receiverUser;

	private String messageTitle;

	private String messageContent;

	private String isRead;

	private Date readTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public OrgUser getSendUser() {
		return sendUser;
	}

	public void setSendUser(OrgUser sendUser) {
		this.sendUser = sendUser;
	}

	public OrgUser getReceiverUser() {
		return receiverUser;
	}

	public void setReceiverUser(OrgUser receiverUser) {
		this.receiverUser = receiverUser;
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

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	
}
