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
		logger.info("微信支付结果查询job开始执行……");
		workHireService.queryWXPayResultFromWX();
		logger.info("微信支付结果查询job执行完毕");
	}
	
	public void closeOverTimePublish(){
		logger.info("关闭超时招工job开始执行……");
		workHireService.closeOverTimePublish();
		logger.info("关闭超时招工job执行完毕");
	}
}
