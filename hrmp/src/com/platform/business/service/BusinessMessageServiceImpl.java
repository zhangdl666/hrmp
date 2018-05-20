package com.platform.business.service;

import java.util.Calendar;
import java.util.List;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.dao.BusinessMessageDao;
import com.platform.business.pojo.MMobileMessage;
import com.platform.business.pojo.Message;
import com.platform.business.pojo.WorkHire;
import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgUserBo;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.service.OrgUserService;

public class BusinessMessageServiceImpl implements BusinessMessageService {
	
	private BusinessMessageDao businessMessageDao;
	private OrgUserService orgUserService;
	private MobileMessageService mobileMessageService;

	public BusinessMessageDao getBusinessMessageDao() {
		return businessMessageDao;
	}

	public void setBusinessMessageDao(BusinessMessageDao businessMessageDao) {
		this.businessMessageDao = businessMessageDao;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public MobileMessageService getMobileMessageService() {
		return mobileMessageService;
	}

	public void setMobileMessageService(MobileMessageService mobileMessageService) {
		this.mobileMessageService = mobileMessageService;
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

	@Override
	public void notifyAdminAsClosePublish(WorkHire wh, OrgUser user) throws Exception {
		if(wh == null) {
			return ;
		}
		List<OrgUserBo> adminList = orgUserService.getAdminUserList(wh.getPublisherCompanyId());
		if(adminList == null || adminList.size()==0) {
			return ;
		}
		
		for(int i=0;i<adminList.size();i++) {
			OrgUserBo admin = adminList.get(i);
			Message msg = new Message();
			msg.setCreateTime(Calendar.getInstance().getTime());
			msg.setIsRead("0");
			msg.setMessageContent("�й��쳣�ر�֪ͨ��" + wh.getBusinessNumber());
			msg.setMessageTitle("�й���Ϣ��" + wh.getBusinessNumber() + "���й��˱���������±��رգ���֪��");
			msg.setReceiverUserId(admin.getId());
			msg.setSendUserId(user.getId());
			this.businessMessageDao.saveMessage(msg);
			
			//���Ͷ���
//			mobileMessageService.sendMessage(admin.getLoginName(), "���ʺ�����֪ͨ������" + wh.getBusinessNumber() + "�쳣�رգ���֪����");
		}
		
	}

}
