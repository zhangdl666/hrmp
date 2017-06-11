package com.platform.report.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.platform.business.action.BaseAction;
import com.platform.business.bo.WorkSignBo;
import com.platform.report.po.Report;
import com.platform.report.service.WorkHireReportService;

public class WorkHireReportAction extends BaseAction {

	private String year;
	private String month;
	private String tableStr;
	private List<WorkSignBo> signList;
	
	private WorkHireReportService workHireReportService;
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTableStr() {
		return tableStr;
	}

	public void setTableStr(String tableStr) {
		this.tableStr = tableStr;
	}

	public List<WorkSignBo> getSignList() {
		return signList;
	}

	public void setSignList(List<WorkSignBo> signList) {
		this.signList = signList;
	}

	public WorkHireReportService getWorkHireReportService() {
		return workHireReportService;
	}

	public void setWorkHireReportService(WorkHireReportService workHireReportService) {
		this.workHireReportService = workHireReportService;
	}

	public String viewSignsReport() throws Exception{
		//初始化参数
		if(year == null || "".equals(year)) {
			Calendar cal = Calendar.getInstance();
			year = String.valueOf(cal.get(Calendar.YEAR));
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		}
		HashMap<String, String> params = new HashMap<String, String>();
		String startTime = year + "-" + month + "-01";
		params.put("startTime", startTime);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date st = sdf.parse(startTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(st);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			String endTime = sdf.format(calendar.getTime());
			params.put("endTime", endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//查询
		try {
			Report report = workHireReportService.statSignsByCompany(params);
			
			//生成table
			com.platform.report.datadef.Data2D dataSet = report.getDataSet();
			List rowKeys = dataSet.getRowKeys();
			List columnKeys = dataSet.getColumnKeys();
			if(rowKeys == null || rowKeys.size() == 0) {
				tableStr = "未查到符合条件的数据！";
			}else {
				com.platform.report.po.Table table = new com.platform.report.po.Table();
				table.setTableId("xuqiugenzong");
				table.setTableCssClass("table table-bordered");
				table.setTrClassOne("one");
				table.setTrClassTwo("two");
				table.setBorder("0");
				table.setCellpadding("0");
				table.setCellspacing("0");
				
				//列标题
				List<com.platform.report.po.ColumnTitle> columnTitleList = new ArrayList<com.platform.report.po.ColumnTitle>();
				for(int i=0; i<columnKeys.size(); i++) {
					String value = (String)columnKeys.get(i);
					if(value == null) {
						continue;
					}
					
					com.platform.report.po.ColumnTitle columnTitle = new com.platform.report.po.ColumnTitle();
					columnTitle.setName(value);
					columnTitle.setDisplayName(value);
					columnTitle.setViewDetail(true);
					columnTitle.setShowType(com.platform.report.po.Table.SHOW_TYPE_NUMBER);
					columnTitle.setDecimals(0);
					//columnTitle.setCssStyle("text-align:center");
					columnTitleList.add(columnTitle);
				}
				
				table.setColumnTitleList(columnTitleList);
				
				//行标题
				List<com.platform.report.po.RowTitle> rowTitleList = new ArrayList<com.platform.report.po.RowTitle>();
				
				for(int i=0; i< rowKeys.size(); i++) {
					String value = (String)(rowKeys.get(i));
					if(value == null){
						continue;
					}
					com.platform.report.po.RowTitle rowTitle = new com.platform.report.po.RowTitle();
					rowTitle.setName(value);
					rowTitle.setDisplayName(value);
					rowTitle.setParentRowName(null);
					//rowTitle.setCssStyle("text-align:center");
					rowTitle.setViewDetail(true);
					rowTitleList.add(rowTitle);
				}
				//添加合计行
				com.platform.report.po.RowTitle totalRow = new com.platform.report.po.RowTitle();
				totalRow.setName("合计");
				totalRow.setDisplayName("合计");
				//totalRow.setCssStyle("text-align:center");
				totalRow.setCalcu(true);
				totalRow.setViewDetail(true);
				List<com.platform.report.po.Title> expressions = new ArrayList<com.platform.report.po.Title>();
				List<com.platform.report.po.ColumnTitle> cts = table.getTerminalColumnTitles();
				for(int i=0; i<cts.size(); i++) {
					com.platform.report.po.ColumnTitle ct = cts.get(i);
					com.platform.report.po.Title expression = new com.platform.report.po.Title();
					expression.setContent("SUM[" + ct.getName() + "]");
					expression.setCalcu(true);
					expression.setViewDetail(true);
					expression.setDecimals(0);
					expressions.add(expression);
				}
				totalRow.setExpressions(expressions);
				rowTitleList.add(totalRow);
				table.setRowTitleList(rowTitleList);
				
				//生成表格
				com.platform.report.util.ReportShowUtil util = new com.platform.report.util.ReportShowUtil();
				tableStr = util.generateTable(report, table, false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} 
		return SUCCESS;
	}
	
	public String viewSignsReportDetail() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("year", getRequest().getParameter("year"));
		params.put("month", getRequest().getParameter("month"));
		params.put("rowName", getRequest().getParameter("rowName"));
		params.put("columnName", getRequest().getParameter("columnName"));
		signList = workHireReportService.viewWorkHireDetails(params);
		return SUCCESS;
	}
}
