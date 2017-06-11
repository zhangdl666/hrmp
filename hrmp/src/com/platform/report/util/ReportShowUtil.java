package com.platform.report.util;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.platform.report.datadef.Data2D;
import com.platform.report.po.ColumnTitle;
import com.platform.report.po.Report;
import com.platform.report.po.RowTitle;
import com.platform.report.po.Table;
import com.platform.report.po.Title;

/**
 *  <p>
 *  报表展现工具
 *  </p>
 * 
 */
public class ReportShowUtil {
    protected final Log log = LogFactory.getLog(this.getClass());

    /**
     * 
     * <p>
     * 生成表格
     * 根据报表dataSet生成默认表格
     * </p>
     * @author ZDL 时间 Nov 15, 2012 9:00:24 AM
     * @param report 报表对象
     * @param showTitle 是否显示报表标题，true：显示；false：不显示
     * @param leftTopTitle 报表左上角内容，可为�?
     * @return
     * @throws Exception 
     */
    public String generateTable(Report report,boolean showTitle,String leftTopTitle) throws Exception {
        if(report == null) {
            return null;
        }
        
        Data2D dataSet = report.getDataSet();
        if(dataSet == null) {
            return null;
        }
        List columns = dataSet.getColumnKeys();
        if(columns == null || columns.size() == 0) {
            return null;
        }
        List rows = dataSet.getRowKeys();
        if(rows == null || rows.size() == 0) {
            return null;
        }
        
        Table table = new Table();
        table.setShowTitle(showTitle);
        table.setLeftTop(leftTopTitle);
        
        //列标�?
        List<ColumnTitle> cList = new ArrayList<ColumnTitle>();
        for(int m=0;m<columns.size();m++){
            ColumnTitle ct = new ColumnTitle();
            ct.setName(columns.get(m).toString());
            ct.setDisplayName(columns.get(m).toString());
            ct.setCalcu(false);
            cList.add(ct);
        }
        table.setColumnTitleList(cList);
        
        //行标�?
        List<RowTitle> rList = new ArrayList<RowTitle>();
        for(int i=0;i<rows.size();i++) {
            RowTitle rt = new RowTitle();
            rt.setName(rows.get(i).toString());
            rt.setDisplayName(rows.get(i).toString());
            rt.setCalcu(false);
            rList.add(rt);
        }
        table.setRowTitleList(rList);
        
        return generateTable(report, table, false);
    }
    
    /**
     * 
     * <p>
     * 生成表格
     * 在报表定义文件中定义报表的列标题、行标题，并依此为依据生成表�?
     * 列标题支持多级，行标题最多支持两�?
     * </p>
     * @author ZDL 时间 Nov 21, 2012 1:53:52 PM
     * @param report 报表对象
     * @param filePath 报表定义文件路径
     * @param showEmptyRow 是否显示空行  true：显示；false：不显示
     * @return
     * @throws Exception 
     */
    public String generateTable(Report report,String filePath,boolean showEmptyRow) throws Exception {
        if(report == null) {
            return null;
        }
        
        Data2D dataSet = report.getDataSet();
        if(dataSet == null) {
            return null;
        }
        TableUtil tableUtil = new TableUtil();
        Table table = tableUtil.parseTable(filePath);
        return generateTable(report, table, showEmptyRow);
    }
    
    /**
     * 
     * <p>
     * 生成表格
     * </p>
     * @author ZDL 时间 Jan 14, 2013 1:52:30 PM
     * @param report 报表对象
     * @param tabFormat 表格对象
     * @param showEmptyRow 是否显示空行  true：显示；false：不显示
     * @return 
     * @throws Exception
     */
    public String generateTable(Report report,Table tabFormat,boolean showEmptyRow) throws Exception {
        String result = null;
        try{ 
            /*无列标题，return null*/
            List<ColumnTitle> columnTitleList = tabFormat.getColumnTitleList();
            if(columnTitleList == null || columnTitleList.size()==0) {
                return null;
            }
            
            /*无行标题，return null*/
            List<RowTitle> rowTitleList = tabFormat.getRowTitleList();
            if(rowTitleList == null || rowTitleList.size()==0) {
                return null;
            }
            
            //1、拼接表�?
            StringBuffer sb = new StringBuffer();
            sb.append("<table "); 

            if(tabFormat.getTableId() != null) {
            	sb.append(" id=\"");
            	sb.append(tabFormat.getTableId());
            	sb.append("\"");
            }
            
            if(tabFormat.getTableWidth() != null) {
            	sb.append(" width=\"");
            	sb.append(tabFormat.getTableWidth());
            	sb.append("\"");
            }
            
            if(tabFormat.getTableCssClass() != null) {
            	sb.append(" class=\"");
            	sb.append(tabFormat.getTableCssClass());
            	sb.append("\"");
            }
            
            if(tabFormat.getTableCssStyle() != null) {
            	sb.append(" style=\"");
            	sb.append(tabFormat.getTableCssStyle());
            	sb.append("\"");
            }
            
            if(tabFormat.getCellspacing() != null) {
            	sb.append(" cellspacing=\"");
            	sb.append(tabFormat.getCellspacing());
            	sb.append("\"");
            }
            
            if(tabFormat.getCellpadding() != null) {
            	sb.append(" cellpadding=\"");
            	sb.append(tabFormat.getCellpadding());
            	sb.append("\"");
            }
            
            if(tabFormat.getBorder() != null) {
            	sb.append(" border=\"");
            	sb.append(tabFormat.getBorder());
            	sb.append("\"");
            }
            
            sb.append(">");
            if(tabFormat.isShowTitle()) {
                /*
                 * 若显示标题，则标题居�?
                 */
            	sb.append("<caption>");
            	sb.append(report.getTitle()==null?"":report.getTitle());
            	sb.append("</caption>");
            }
            log.debug(sb.toString());
            
            //2、拼接列标题
            int columnTitleLevelCount = tabFormat.getColumnTitleLevelCount();
            int rowTitleLevelCount = tabFormat.getRowTitleLevelCount();
            
            sb.append("<thead>");
            for(int i=0; i<columnTitleLevelCount; i++) {
                List<ColumnTitle> cts = tabFormat.getColumnTitlesByLevel(i+1);
                if(i==0) {//第一�?
                    sb.append("<tr><th ");
                    sb.append(" rowspan=\"");
                    sb.append(columnTitleLevelCount);
                    sb.append("\" colspan=\"");
                    sb.append(rowTitleLevelCount);
                    sb.append("\"");
                    
                    //设置左上角样式（使用列标题的样式，以保障标题行样式的�?��性）
                    ColumnTitle ct = tabFormat.getColumnTitleList().get(0);
                    if(ct.getCssClass() != null && !ct.getCssClass().trim().equals("")) {
                    	sb.append(" class=\"");
                    	sb.append(ct.getCssClass());
                    	sb.append("\"");
                    }
                    if(ct.getCssStyle() != null && !ct.getCssStyle().trim().equals("")) {
                    	sb.append(" style=\"");
                    	sb.append(ct.getCssStyle());
                    	sb.append("\"");
                    }
                    
                    sb.append(">");
                    sb.append(tabFormat.getLeftTop()==null?"":tabFormat.getLeftTop());
                    sb.append("</th>");
                }else {
                    sb.append("<tr>");
                }
                
                for(int j=0; j<cts.size(); j++) {
                    ColumnTitle column = cts.get(j);
                    sb.append(generateTdLabel(column, 0));
                }
                sb.append("</tr>");
                log.debug(sb.toString());
            }
            sb.append("</thead>");
            
            //3、拼接数�?
            sb.append("<tbody>");
            List<RowTitle> rtx = tabFormat.getRowTitlesByLevel(1);
            Map<String, Integer> map = new HashMap<String, Integer>();//此map是为了记录行数，以便于实现行class交替效果
            map.put("rowNum", 1);
            for(int j=0; j<rtx.size(); j++) {
                String rowStr = generateRow(report,tabFormat, rtx.get(j), showEmptyRow,map);
                if(rowStr != null) {
                    sb.append(rowStr);
                }
            }
            sb.append("</tbody>");
            
            sb.append("</table>");
            
            result = sb.toString();
        } catch(JDOMException e){
            e.printStackTrace();
            throw e;
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return result;
    }
    
    
    
    
    /*
     * 生成表格行数�?
     */
    private String generateRow(Report report, Table format, RowTitle row,boolean showEmptyRow,Map<String, Integer> map) throws Exception {
        StringBuffer rowStrBuf = new StringBuffer();
        List<RowTitle> subRowList = format.getChildrenRow(row.getName());
        Integer rowNum = map.get("rowNum");//记录当前行号，便于设置行交替css样式
        if(subRowList != null) {
            int subRowEmptyCount = 0;//空行数量
            for(int m=0; m<subRowList.size(); m++) {
                RowTitle subRow = subRowList.get(m);
                String rowTdStr = generateRowTDValue(report,format,subRow,showEmptyRow);
                if(rowTdStr == null) {
                    subRowEmptyCount++;
                }else {
                	rowStrBuf.append("<tr ");
                	if(rowNum % 2 == 1) {//设置奇偶行css样式
                		if(format.getTrClassOne() != null && !format.getTrClassOne().trim().equals("")) {
                			rowStrBuf.append("class=" + format.getTrClassOne());
                		}
                	}else {
                		if(format.getTrClassTwo() != null && !format.getTrClassTwo().trim().equals("")) {
                			rowStrBuf.append("class=" + format.getTrClassTwo());
                		}
                	}
                	rowStrBuf.append(">");
                    rowStrBuf.append(generateTdLabel(subRow, 0));
                    rowStrBuf.append(rowTdStr);
                    rowStrBuf.append("</tr>");
                    rowNum ++ ;
                }
            }
            
            if(subRowEmptyCount == subRowList.size()) {//若空行数量等于子行数量，表示此行为空，返回null
                return null;
            }else {
                String rowFirstTd = generateTdLabel(row, subRowEmptyCount);//第一�?
                
                String rowStr = rowStrBuf.toString();
                
                //将第�?��TD加入rowStr�?
                int index = rowStr.indexOf(">");//截取第一个tr出现的位�?
                rowStr = rowStr.substring(0,index+1) + rowFirstTd + rowStr.substring(index+1);
                map.put("rowNum", rowNum);//记录行号
                return rowStr;
            }
        }else {
            String rowTdStr = generateRowTDValue(report,format,row,showEmptyRow);
            if(rowTdStr == null) {
                return null;
            }else {
            	rowStrBuf.append("<tr ");
            	if(rowNum % 2 == 1) {
            		if(format.getTrClassOne() != null && !format.getTrClassOne().trim().equals("")) {
            			rowStrBuf.append("class=" + format.getTrClassOne());
            		}
            	}else {
            		if(format.getTrClassTwo() != null && !format.getTrClassTwo().trim().equals("")) {
            			rowStrBuf.append("class=" + format.getTrClassTwo());
            		}
            	}
            	rowStrBuf.append(">");
                rowStrBuf.append(generateTdLabel(row, 0));
                rowStrBuf.append(rowTdStr);
                rowStrBuf.append("</tr>");
                rowNum = Integer.valueOf(rowNum.intValue() + 1);
                map.put("rowNum", rowNum);//记录行号
                return rowStrBuf.toString();
            }
        }
    }
    
    /**
     * 
     * <p>
     * 生成标题TD
     * </p>
     * @author ZDL 时间 Dec 7, 2012 10:14:47 AM
     * @param element
     * @param delRowCount 要删除的行数
     * @return
     */
    private String generateTdLabel(com.platform.report.po.Title element,int delRowCount) {
        StringBuffer tdLabelStrBuf = new StringBuffer();
        tdLabelStrBuf.append("<td ");
        int rowspan = 1;
        String rowspanStr = element.getRowspan();
        if(rowspanStr != null && !rowspanStr.trim().equals("")) {
            rowspan = Integer.valueOf(rowspanStr);
        }
        rowspan = rowspan - delRowCount;
        if(rowspan > 1) {
            tdLabelStrBuf.append(" rowspan=\"");
            tdLabelStrBuf.append(rowspan);
            tdLabelStrBuf.append("\"");
        }

        if(element.getColspan() != null) {//合并多列
            tdLabelStrBuf.append(" colspan=\"");
            tdLabelStrBuf.append(element.getColspan());
            tdLabelStrBuf.append("\"");
        }
        if(element.getWidth() != null) {//宽度
            tdLabelStrBuf.append(" width=\"");
            tdLabelStrBuf.append(element.getWidth());
            tdLabelStrBuf.append("\"");
        }
        if(element.getCssClass() != null) {//css class
            tdLabelStrBuf.append(" class=\"");
            tdLabelStrBuf.append(element.getCssClass());
            tdLabelStrBuf.append("\"");
        }
        if(element.getCssStyle() != null) {//css style
            tdLabelStrBuf.append(" style=\"");
            tdLabelStrBuf.append(element.getCssStyle());
            tdLabelStrBuf.append("\"");
        }
        tdLabelStrBuf.append(">");
        tdLabelStrBuf.append(element.getDisplayName());
        tdLabelStrBuf.append("</td>");
        
        return tdLabelStrBuf.toString();
    }
    
    /*
     * 生成表格�?TD>，不包含行标�?TD>
     */
    private String generateRowTDValue(Report report,Table format,RowTitle row,boolean showEmptyRow) throws Exception {
		String rowName = row.getName();
		boolean isCalcuRow = row.isCalcu();
		if (isCalcuRow) {
			return generateCalcuRowTdValue(report, format, row, showEmptyRow);
		}
        List<ColumnTitle> columns = format.getTerminalColumnTitles();
        StringBuffer rowTDStrBuf = new StringBuffer();
        
        boolean emptyRowFlag = true;//空行标识
        for(int n=0; n<columns.size(); n++) {
            ColumnTitle column = columns.get(n);
            String columnName = column.getName();
            boolean isCalcuCoumn = column.isCalcu();
            String valueStr = null;
            
            if(isCalcuCoumn) {
                valueStr = getCalcuColumnValue(report,rowName, column);
            }else {
                valueStr = getValue(report, row, columns.get(n));
            }
            log.debug("row:" + row + "---column" + columns.get(n) + "-----value:" + valueStr);
            //若有�?��数据不为空，则此行不为空
            if(valueStr != null && !valueStr.trim().equals("")) {
                if(emptyRowFlag) {
                    emptyRowFlag = false;
                }
            }
            rowTDStrBuf.append("<td ");
            //添加样式(使用列样�?
            if(column.getCssClass() != null && !column.getCssClass().trim().equals("")) {
            	rowTDStrBuf.append("class=\"");
            	rowTDStrBuf.append(column.getCssClass());
            	rowTDStrBuf.append("\" ");
            }
            
            if(column.getCssStyle() != null && !column.getCssStyle().trim().equals("")) {
            	rowTDStrBuf.append("style=\"");
            	rowTDStrBuf.append(column.getCssStyle().endsWith(";")?column.getCssStyle():column.getCssStyle()+";");//若style内容未以�?”结尾，做一处理，增强程序健壮�?
            	rowTDStrBuf.append("\" ");
            }
            rowTDStrBuf.append(">");
            
            appendValue(rowTDStrBuf,valueStr,report.isViewDetail() && row.isViewDetail() && column.isViewDetail(),rowName,columnName);
            rowTDStrBuf.append("</td>");
        }
        
        //若不显示空行，且当前行数据为空的话，返回 null
        if(!showEmptyRow && emptyRowFlag) {
            return null;
        }
        return rowTDStrBuf.toString();
    }
    
    /**
     * 生成计算行数据TD 字符串（不包含行标题TD�?
     * @param report
     * @param table
     * @param row
     * @param showEmptyRow
     * @return
     * @throws Exception
     */
    private String generateCalcuRowTdValue(Report report, Table table, RowTitle row, boolean showEmptyRow) throws Exception{
        StringBuffer rowTDStrBuf = new StringBuffer();
        List<com.platform.report.po.Title> expressionList = row.getExpressions();
        boolean emptyRowFlag = true;//空行标识
        for(int i=0; i<expressionList.size(); i++) {
        	Title expression = expressionList.get(i);
            String valueStr = getCalcuRowValue(report,table, expression);
            //若有�?��数据不为空，则此行不为空
            if(valueStr != null && !valueStr.trim().equals("")) {
                if(emptyRowFlag) {
                    emptyRowFlag = false;
                }
            }
            rowTDStrBuf.append("<td ");
            //添加样式(使用列样�?
            ColumnTitle column = table.getTerminalColumnTitles().get(i);
            if(column.getCssClass() != null && !column.getCssClass().trim().equals("")) {
            	rowTDStrBuf.append("class=\"");
            	rowTDStrBuf.append(column.getCssClass());
            	rowTDStrBuf.append("\" ");
            }
            if(column.getCssStyle() != null && !column.getCssStyle().trim().equals("")) {
            	rowTDStrBuf.append("style=\"");
            	rowTDStrBuf.append(column.getCssStyle().endsWith(";")?column.getCssStyle():column.getCssStyle()+";");//若style内容未以�?”结尾，做一处理，增强程序健壮�?
            	rowTDStrBuf.append("\" ");
            }
            
            rowTDStrBuf.append(">");
            /*
             * 用对应的列标题名称当做isViewDetail第二个参�?
             * 当此计算行非“合计�?行时，此传参可能会出现问�?
             */
            appendValue(rowTDStrBuf,valueStr,report.isViewDetail() && row.isViewDetail() && expression.isViewDetail(),
            		null,column.getName());
            
            rowTDStrBuf.append("</td>");
        }
      //若不显示空行，且当前行数据为空的话，返回 null
        if(!showEmptyRow && emptyRowFlag) {
            return null;
        }
        return rowTDStrBuf.toString();
    }


    private String getValue(Report report,RowTitle row,ColumnTitle column) throws Exception{
        String rowName = row.getName();
        String columnName = column.getName();
        Data2D dataSet = report.getDataSet();
        int decimals = column.getDecimals();
        
        String showType = column.getShowType();
        
        if(showType == null) {//默认数�?类型
            showType = Table.SHOW_TYPE_TEXT;
        }
        String valueStr = null;
        
        if(Table.SHOW_TYPE_NUMBER.equals(showType)) {
            Number value = dataSet.getNumberValue(rowName, columnName);

            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(decimals);
            if(value != null) {
                valueStr = format.format(value);
            }
        }else if(Table.SHOW_TYPE_PERCENT.equals(showType)) {
            Number value = dataSet.getNumberValue(rowName, columnName);
            if(value == null) {
            	return null;
            }
            value = value.doubleValue() * 100;
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(decimals);
            if(value != null) {
                valueStr = format.format(value) + "%";
            }
            
        }else {
            valueStr = String.valueOf(dataSet.getValue(rowName, columnName));
        }
        return valueStr;
    }
    
    /*
     * 根据表达式计算�?
     */
    private String getCalcuRowValue(Report report,Table table,com.platform.report.po.Title expressionElement) throws Exception{
        String expression = expressionElement.getContent();
        log.debug("原表达式>>>>>>>>" + expression);
        if(expression == null || expression.trim().equals("")) {
            return null;
        }
        
        expression = expression.replace(" ", "");
		while (expression.indexOf("]") != -1) {
			int startIndex = expression.indexOf("[");
			int endIndex = expression.indexOf("]");
			String strBefore = expression.substring(0, startIndex==0?0: startIndex);
			String strMiddle = expression.substring(startIndex + 1, endIndex);
			String strEnd = expression.substring(endIndex + 1);

			Number value = 0;
			if (strBefore.endsWith("SUM")) {
				strBefore = strBefore.substring(0,strBefore.length()-3);
				if (strMiddle.equals("ALL")) {
					List<RowTitle> rowList = table.getRowTitleList();
					List<ColumnTitle> colList = table.getColumnTitleList();
					for (int i = 0; i < rowList.size(); i++) {
						for (int j = 0; j < colList.size(); j++) {
							Number v = report.getDataSet().getNumberValue(
											rowList.get(i).getName(),
											colList.get(j).getName());
							if(v != null) {
								value = value.doubleValue() + v.doubleValue();
							}
						}
					}
				} else {
					List<RowTitle> rowList = table.getRowTitleList();
					for (int i = 0; i < rowList.size(); i++) {
						Number v = report.getDataSet().getNumberValue(rowList.get(i).getName(),strMiddle);
						if(v != null) {
							value = value.doubleValue() + v.doubleValue();
						}
					}
				}
			} else {
				String[] s = strMiddle.split(",");
				if (s.length < 2) {
					throw new Exception("expression 有误:" + expression + "----"
							+ strMiddle);
				}

				value = report.getDataSet().getNumberValue(s[0], s[1]);
			}

			if (value != null) {
				expression = strBefore + value + strEnd;
			} else {
				expression = strBefore + 0 + strEnd;
			}
		}
        log.debug("处理后的表达�?>>>>>>>" + expression);
        
        return calcu(expression,expressionElement.getShowType(),expressionElement.getDecimals());
    }
    
    private String calcu(String expression,String showType,int decimals) throws EvaluationException {
    	// 计算�?
        Evaluator evaluator = new Evaluator();
        
        try{
            double validateValue = evaluator.getNumberResult(expression);
            if(Double.valueOf(validateValue).isNaN() || Double.valueOf(validateValue).isInfinite()){
            	return "\\";
            }
            
            //数�?格式�?
            String valueStr = null;
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(decimals);
            
            if("Percent".equals(showType)) {
                validateValue = validateValue * 100;
                valueStr = format.format(validateValue) + "%";
            }else {
                valueStr = format.format(validateValue);
            }
            
            return valueStr;
        } catch(EvaluationException e){
            e.printStackTrace();
            throw e;
        }
    }
    
    /*
     * 根据表达式计算�?，当发生异常时，return null;
     * 注：此方法异常被捕获，但未做任何处理
     */
    private String getCalcuColumnValue(Report report,String rowName,ColumnTitle column) throws EvaluationException{
        String columnName = column.getName();
        String expression = column.getExpression();
        
        if(expression == null || expression.trim().equals("")) {
            return null;
        }
        
        expression = expression.replace(" ", "");
        while (expression.indexOf("]") != -1) {
			int startIndex = expression.indexOf("[");
			int endIndex = expression.indexOf("]");
			String strBefore = expression.substring(0, startIndex==0?0: startIndex);
			String strMiddle = expression.substring(startIndex + 1, endIndex);
			String strEnd = expression.substring(endIndex + 1);

			Number value = report.getDataSet().getNumberValue(rowName, strMiddle);

			if (value != null) {
				expression = strBefore + value + strEnd;
			} else {
				expression = strBefore + 0 + strEnd;
			}
		}
        log.debug("column:" + columnName + "--expression:" + expression);
        return calcu(expression,column.getShowType(),column.getDecimals());
    }


    private void appendValue(StringBuffer strBuf,String value,boolean viewDetail,String rowName,String columnName) {
        //对空值进行处�?
        if(value == null || value.trim().equals("") || value.equals("null")) {
            return;
        }
        
        //Modify By Zdl Reason：value�?时，不生成超链样�?
        if("0".equals(value)){
        	strBuf.append(value);
        	return ;
        }
        
		// 对于可查看清单的文本，设置其onclick事件
        if(viewDetail) {
            strBuf.append("<a href=\"javascript:viewDetail('");
            if(rowName != null && !rowName.equals("")) {
                strBuf.append(rowName);
            }
            strBuf.append("','");
            
            if(columnName != null && !columnName.equals("")) {
                strBuf.append(columnName);
            }
            strBuf.append("');\">");
        }
        //对空值进行处�?
        strBuf.append(value);
        
        if(viewDetail) {
            strBuf.append("</a>");
        }
    }

    /**
     * 
     * <p>
     * 生成Single Series Chart Xml
     * 图形展现工具：FusionCharts
     * </p>
     * @author ZDL 时间 Nov 26, 2012 11:02:45 AM
     * @param report
     * @return
     * @throws JDOMException 
     * @throws IOException 
     */
    public String generateSingleSeriesChartXml(Report report,String filePath) throws JDOMException, IOException {
        if(report == null) {
            return null;
        }
        
        Data2D dataSet = report.getDataSet();
        if(dataSet == null) {
            return null;
        }
        
        StringBuffer sb = new StringBuffer();
        SAXBuilder builder = new SAXBuilder();
        try{
            Document document = builder.build(new File(filePath));
            Element root = document.getRootElement();
            //标题
            sb.append("<chart rotateYAxisName='0' ");
            appendAttribute(sb,root,"caption");
            appendAttribute(sb,root,"xAxisName");
            appendAttribute(sb,root,"yAxisName");
            appendAttribute(sb,root,"yAxisMinValue");//Y轴最小�?
            appendAttribute(sb,root,"yAxisMaxValue");//Y轴最大�?
            appendAttribute(sb,root, "formatNumberScale");//数�?格式
            appendAttribute(sb, root,"showPercentValues");
            appendAttribute(sb, root,"showValues");
            sb.append(" bgColor='#ECEEF5' showBorder='1'>");
            
            //数据
            Element labels = root.getChild("labels");
            List<Element> labelList = labels.getChildren("label");
            
            String labelType = labels.getAttributeValue("type");
            if(labelType == null || labelType.trim().equals("")) {
                labelType = "column";
            }
            
            Element serieses = root.getChild("serieses");
            Element series = serieses.getChild("series");
            String row = null;
            String col = null;
            
            for(int i=0; i<labelList.size(); i++) {
                if(labelType.equals("row")) {
                    row = labelList.get(i).getChildText("name");
                    col = series.getChildText("name");
                }else {
                    row = series.getChildText("name");
                    col = labelList.get(i).getChildText("name");
                }
                Number value = dataSet.getNumberValue(row,col);
                log.debug("---row:" + row + "---column:" + col + "---value:" + value);
                sb.append("<set label='");
                sb.append(labelList.get(i).getChildText("displayName"));
                sb.append("' value='");
                sb.append(value == null ? "0":value);
                sb.append("' />");
            }
            sb.append("</chart>");
        }catch(JDOMException e){
            
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }
    
    /**
     * 
     * <p>
     * 生成Muti Series Chart Xml
     * 图形展现工具：FusionCharts
     * </p>
     * @author ZDL 时间 Nov 25, 2012 4:51:17 PM
     * @param report
     * @param filePath
     * @return
     * @throws JDOMException 
     * @throws IOException 
     */
    public String generateMutiSeriesChartXml(Report report,String filePath) throws JDOMException, IOException {
        if(report == null) {
            return null;
        }
        
        Data2D dataSet = report.getDataSet();
        if(dataSet == null) {
            return null;
        }
        
        StringBuffer sb = new StringBuffer();
        SAXBuilder builder = new SAXBuilder();
        try{
            Document document = builder.build(new File(filePath));
            Element root = document.getRootElement();
            //标题
            sb.append("<chart rotateYAxisName='0' ");
            appendAttribute(sb,root,"caption");
            appendAttribute(sb,root,"xAxisName");
            appendAttribute(sb,root,"yAxisName");
            appendAttribute(sb,root,"yAxisMinValue");//Y轴最小�?
            appendAttribute(sb,root,"yAxisMaxValue");//Y轴最大�?
            appendAttribute(sb,root, "formatNumberScale");//数�?格式
            appendAttribute(sb, root,"showValues");
            sb.append(" bgColor='#ECEEF5' showBorder='1'>");
            
            //标签
            sb.append("<categories>");
            Element labels = root.getChild("labels");
            List<Element> labelList = labels.getChildren("label");
            for(int i=0; i<labelList.size(); i++) {
                sb.append("<category label='");
                sb.append(labelList.get(i).getChildText("displayName"));
                sb.append("' />");
            }
            sb.append("</categories>");
            
            String labelType = labels.getAttributeValue("type");
            if(labelType == null || labelType.trim().equals("")) {
                labelType = "column";
            }
            
            //数据
            Element serieses = root.getChild("serieses");
            List<Element> seriesList = serieses.getChildren("series");
            for(int j=0; j<seriesList.size(); j++) {
                sb.append("<dataset seriesName='");
                sb.append(seriesList.get(j).getChildText("displayName"));
                sb.append("'>");
                
                String row = null;
                String col = null;
                for(int m=0; m<labelList.size(); m++) {
                    if(labelType.equals("row")) {
                        row = labelList.get(m).getChildText("name");
                        col = seriesList.get(j).getChildText("name");
                    }else {
                        row = seriesList.get(j).getChildText("name");
                        col = labelList.get(m).getChildText("name");
                    }
                    
                    Number value = dataSet.getNumberValue(row,col);
                    log.debug("---row:" + row + "---column:" + col + "---value:" + value);
                    
                    sb.append("<set value='");
                    sb.append(value == null ? "0":value);
                    sb.append("' />");
                }
                sb.append("</dataset>");
            }
            
            sb.append("</chart>");
        }catch(JDOMException e){
            
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }
    
    /**
     * 
     * <p>
     * 生成Combination Series Chart Xml
     * 图形展现工具：FusionCharts
     * </p>
     * @author ZDL 时间 Nov 26, 2012 2:28:19 PM
     * @param report
     * @param filePath
     * @return
     * @throws JDOMException
     * @throws IOException 
     */
    public String generateCombinationSeriesChartXml(Report report,String filePath) throws JDOMException, IOException {
        if(report == null) {
            return null;
        }
        
        Data2D dataSet = report.getDataSet();
        if(dataSet == null) {
            return null;
        }
        
        StringBuffer sb = new StringBuffer();
        SAXBuilder builder = new SAXBuilder();
        try{
            Document document = builder.build(new File(filePath));
            Element root = document.getRootElement();
            //标题
            sb.append("<chart rotateYAxisName='0' ");
            appendAttribute(sb,root,"caption");
            appendAttribute(sb,root, "PYAxisMaxValue");//主Y轴最大�?
            appendAttribute(sb,root, "PYAxisMinValue");//主Y轴最小�?
            appendAttribute(sb,root, "SYAxisMaxValue");//副Y轴最大�?
            appendAttribute(sb,root, "SYAxisMinValue");//副Y轴最小�?
            appendAttribute(sb,root, "formatNumberScale");//数�?格式
            appendAttribute(sb,root,"PYAxisName");
            appendAttribute(sb,root,"SYAxisName");
            appendAttribute(sb,root,"showValues");
            sb.append(" bgColor='#ECEEF5' showBorder='1'>");
            
            //标签
            sb.append("<categories>");
            Element labels = root.getChild("labels");
            List<Element> labelList = labels.getChildren("label");
            for(int i=0; i<labelList.size(); i++) {
                sb.append("<category label='");
                sb.append(labelList.get(i).getChildText("displayName"));
                sb.append("' />");
            }
            sb.append("</categories>");
            
            String labelType = labels.getAttributeValue("type");
            if(labelType == null || labelType.trim().equals("")) {
                labelType = "column";
            }
            
            //数据
            Element serieses = root.getChild("serieses");
            List<Element> seriesList = serieses.getChildren("series");
            for(int j=0; j<seriesList.size(); j++) {
                String showType = seriesList.get(j).getChildText("showType");
                sb.append("<dataset seriesName='");
                sb.append(seriesList.get(j).getChildText("displayName"));
                if(showType != null && showType.equals("Line")) {
                    sb.append("' parentYAxis='S'>");
                }else {
                    sb.append("'>");
                }
                
                String row = null;
                String col = null;
                for(int m=0; m<labelList.size(); m++) {
                    if(labelType.equals("row")) {
                        row = labelList.get(m).getChildText("name");
                        col = seriesList.get(j).getChildText("name");
                    }else {
                        row = seriesList.get(j).getChildText("name");
                        col = labelList.get(m).getChildText("name");
                    }
                    
                    Number value = dataSet.getNumberValue(row,col);
                    log.debug("---row:" + row + "---column:" + col + "---value:" + value);
                    
                    sb.append("<set value='");
                    sb.append(value == null ? "0":value);
                    sb.append("' />");
                }
                sb.append("</dataset>");
            }
            sb.append("</chart>");
        }catch(JDOMException e){
            
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }
    
    private void appendAttribute(StringBuffer strBuf,Element element,String attributeName) {
        String attributeValue = element.getAttributeValue(attributeName);
        if(attributeValue != null && !attributeValue.equals("")) {
            strBuf.append(" ");
            strBuf.append(attributeName);
            strBuf.append("='");
            strBuf.append(attributeValue);
            strBuf.append("'");
        }
    }
    
}

