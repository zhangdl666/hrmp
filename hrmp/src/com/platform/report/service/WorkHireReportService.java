package com.platform.report.service;

import java.util.List;
import java.util.Map;

import com.platform.business.bo.WorkSignBo;
import com.platform.report.po.Report;

public interface WorkHireReportService {

	/**
	 * 交易信息汇总
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public Report statSignsByCompany(Map<String, String> params) throws Exception;
	
	/**
	 * 交易明细
	 * @param params
	 * @return
	 */
	public List<WorkSignBo> viewWorkHireDetails(Map<String, String> params);
}
