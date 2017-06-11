package com.platform.report.service;

import java.util.List;
import java.util.Map;

import com.platform.business.bo.WorkSignBo;
import com.platform.report.po.Report;

public interface WorkHireReportService {

	/**
	 * ������Ϣ����
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public Report statSignsByCompany(Map<String, String> params) throws Exception;
	
	/**
	 * ������ϸ
	 * @param params
	 * @return
	 */
	public List<WorkSignBo> viewWorkHireDetails(Map<String, String> params);
}
