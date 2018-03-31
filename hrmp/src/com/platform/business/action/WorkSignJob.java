package com.platform.business.action;

import org.apache.log4j.Logger;

import com.platform.business.service.WorkHireService;

public class WorkSignJob {
	private final Logger logger = Logger.getLogger(WorkSignJob.class);
	public WorkHireService workHireService;
	
	public WorkHireService getWorkHireService() {
		return workHireService;
	}

	public void setWorkHireService(WorkHireService workHireService) {
		this.workHireService = workHireService;
	}

	public void queryWxPayResult(){
		logger.info("΢��֧�������ѯjob��ʼִ�С���");
		workHireService.queryWXPayResultFromWX();
		logger.info("΢��֧�������ѯjobִ�����");
	}
	
	public void closeOverTimePublish(){
		logger.info("�رճ�ʱ�й�job��ʼִ�С���");
		workHireService.closeOverTimePublish();
		logger.info("�رճ�ʱ�й�jobִ�����");
	}
}
