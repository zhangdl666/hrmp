package com.platform.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkSign;
import com.platform.organization.pojo.OrgUser;
import com.platform.report.dao.BaseReportDao;
import com.platform.report.datadef.Data2D;
import com.platform.report.po.Report;

public class WorkHireReportServiceImpl implements WorkHireReportService {

	private BaseReportDao baseReportDao;
	
	public BaseReportDao getBaseReportDao() {
		return baseReportDao;
	}

	public void setBaseReportDao(BaseReportDao baseReportDao) {
		this.baseReportDao = baseReportDao;
	}

	@Override
	public Report statSignsByCompany(Map<String, String> params) throws Exception {
		StringBuffer sb = new StringBuffer();
		String startTime = params.get("startTime");// 开始时间 ，格式：yyyy-MM-dd
		String endTime = params.get("endTime");// 截止时间，格式：yyyy-MM-dd
		
		sb.append(" select h.publisher_company_name 公司,count(s.id) 用工数量 from work_hire h,work_sign s");
		sb.append(" where h.id = s.work_hire_id");
		sb.append(" and s.confirm_result != 'noPass'");
		sb.append(" and s.confirm_result != 'cancel'");
		sb.append(" and s.confirm_result != 'approving'");
		if (startTime != null && !startTime.trim().equals("")) {
			sb.append(" And s.confirm_time > TO_DATE('");
			sb.append(startTime);
			sb.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		
		if (endTime != null && !endTime.trim().equals("")) {
			sb.append(" And s.confirm_time < TO_DATE('");
			sb.append(endTime);
			sb.append(" 23:59:59','yyyy-mm-dd hh24:mi:ss')");

		}
		
		sb.append(" group by h.publisher_company_name");
		
		try {
			Data2D dataSet = baseReportDao.query(true, true, sb.toString());
			Report report = new Report();
			report.setDataSet(dataSet);
			report.setViewDetail(true);
			return report;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<WorkSignBo> viewWorkHireDetails(Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		String startTime = params.get("startTime");// 开始时间 ，格式：yyyy-MM-dd
		String endTime = params.get("endTime");// 截止时间，格式：yyyy-MM-dd
		String rowName = params.get("rowName");
		
		sb.append(" select wh,ws,u from WorkHireView wh,WorkSign ws,OrgUser u where wh.id = ws.workHireId and ws.empId = u.id");
		sb.append(" and ws.confirmResult != 'noPass'");
		sb.append(" and ws.confirmResult != 'cancel'");
		sb.append(" and ws.confirmResult != 'approving'");
		if (startTime != null && !startTime.trim().equals("")) {
			sb.append(" And ws.confirmTime > TO_DATE('");
			sb.append(startTime);
			sb.append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		
		if (endTime != null && !endTime.trim().equals("")) {
			sb.append(" And ws.confirmTime < TO_DATE('");
			sb.append(endTime);
			sb.append(" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		
		if(rowName != null && !rowName.trim().equals("")) {
			sb.append(" And wh.publisherCompanyName = '");
			sb.append(rowName);
			sb.append("'");
		}
		
		List<Object[]> list = baseReportDao.createQuery(sb.toString());
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<WorkSignBo> result = new ArrayList<WorkSignBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			WorkHireView wh = (WorkHireView)o[0];
			WorkSign ws = (WorkSign)o[1];
			OrgUser u = (OrgUser)o[2];
			WorkSignBo bo = new WorkSignBo();
			bo.setWorkSign(ws);
			bo.setEmp(u);
			bo.setWorkHire(wh);
			result.add(bo);
		}
		return result;
		
	}

}
