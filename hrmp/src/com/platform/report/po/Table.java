package com.platform.report.po;

import java.util.ArrayList;
import java.util.List;

/**
 *  <p>
 *  功能
 *  </p>
 *  
 *  @author ZDL 时间 Jan 9, 2013 4:06:10 PM
 *  @version 1.0 
 *  </br>
 * 
 */
public class Table {
    
    public static final String SHOW_TYPE_NUMBER = "Number";
    public static final String SHOW_TYPE_PERCENT = "Percent";
    public static final String SHOW_TYPE_TEXT = "Text";
    
    /**
     * 是否显示报表标题
     */
    private boolean showTitle;
    
    /**
     * Table宽度
     */
    private String tableWidth;
    
    /**
     * table css style
     */
    private String tableCssStyle;
    
	/**
     * table css class
     */
    private String tableCssClass;
    
    /**
     * table css cellspacing
     */
    private String cellspacing;
    
    /**
     * table css cellpadding
     */
    private String cellpadding;
    
    /**
     * table css border
     */
    private String border;
    
    /**
     * 奇数行class样式
     */
    private String trClassOne;
    
    /**
     * 偶数行class样式
     */
    private String trClassTwo;
    
    /**
     * 是否可以查看明细
     */
    private boolean viewDetail;
    
    /**
     * 生成的table id
     */
    private String tableId; 
    
    public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	/**
	 * 是否允许查看清单
	 * true：是；false：否
	 * @return
	 */
    public boolean isViewDetail() {
		return viewDetail;
	}

    /**
	 * 设置是否允许查看清单
	 * true：是；false：否
	 * @return
	 */
	public void setViewDetail(boolean viewDetail) {
		this.viewDetail = viewDetail;
	}

	/**
	 * get 表格奇数行class样式
	 * @return
	 */
	public String getTrClassOne() {
		return trClassOne;
	}

	/**
	 * set 表格奇数行class样式
	 * @return
	 */
	public void setTrClassOne(String trClassOne) {
		this.trClassOne = trClassOne;
	}

	/**
	 * get 表格偶数行class样式
	 * @return
	 */
	public String getTrClassTwo() {
		return trClassTwo;
	}

	/**
	 * set 表格偶数行class样式
	 * @return
	 */
	public void setTrClassTwo(String trClassTwo) {
		this.trClassTwo = trClassTwo;
	}

	/**
     * 表格左上角内�?
     */
    private String leftTop;
    
    /**
     * 列标题集
     */
    private List<ColumnTitle> columnTitleList;
    
    /**
     * 行标题集
     */
    private List<RowTitle> rowTitleList;
    

    public Table(){
        this.tableWidth = "100%";
        this.showTitle = true;
        this.columnTitleList = new ArrayList<ColumnTitle>();
        this.rowTitleList = new ArrayList<RowTitle>();
    }

    /**
     * 
     * <p>
     * 添加列标题至列标题集
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:32:07 PM
     * @param column
     */
    public void addColumn(ColumnTitle column){
        this.columnTitleList.add(column);
        initColumnTitleLevel();
        initColumnTitleRowspan();
    }
    
    
    
    /**
     * 
     * <p>
     * 添加行标题至行标题集
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:32:20 PM
     * @param row
     */
    public void addRow(RowTitle row){
        this.rowTitleList.add(row);
        initRowTitleLevel();
        initRowTitleColspan();
    }
    
    /**
     * 
     * <p>
     * 得到指定层级的列标题
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:27:57 PM
     * @param level Start From 1
     * @return
     */
    public List<ColumnTitle> getColumnTitlesByLevel(int level) {
        List<ColumnTitle> list = new ArrayList<ColumnTitle>();
        for(int i=0; i<columnTitleList.size(); i++) {
        	ColumnTitle ct = columnTitleList.get(i);
            if(ct.getLevel() == level){
                list.add(ct);
            }
        }
        if(list.size() == 0) {
            return null;
        }
        return list;
    }
    
  
    
    /**
     * 
     * <p>
     * 得到指定层级的行标题
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:27:57 PM
     * @param level start from 1
     * @return
     */
    public List<RowTitle> getRowTitlesByLevel(int level) {
        List<RowTitle> list = new ArrayList<RowTitle>();
        for(int i=0; i<rowTitleList.size(); i++) {
        	RowTitle rt = rowTitleList.get(i);
            if(rt.getLevel() == level){
                list.add(rt);
            }
        }
        if(list.size() == 0) {
            return null;
        }
        return list;
    }
    
    /**
     * 
     * <p>
     * 得到末端行标�?
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:21:13 PM
     * @return
     */
    public List<RowTitle> getTerminalRowTitles() {
        List<RowTitle> list = new ArrayList<RowTitle>();
        for(int i=0; i<rowTitleList.size(); i++) {
        	RowTitle rt = rowTitleList.get(i);
            List<RowTitle> children = getChildrenRow(rt.getName());
            if(children == null){
                list.add(rt);
            }
        }
        if(list.size() == 0) {
            return null;
        }
        return list;
    }
    
    /**
     * 
     * <p>
     * 得到末端列标�?
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:21:13 PM
     * @return
     */
    public List<ColumnTitle> getTerminalColumnTitles() {
        List<ColumnTitle> list = new ArrayList<ColumnTitle>();
        for(int i=0; i<columnTitleList.size(); i++) {
        	ColumnTitle ct = columnTitleList.get(i);
            List<ColumnTitle> children = getChildrenColumn(ct.getName());
            if(children == null){
                list.add(ct);
            }
        }
        if(list.size() == 0) {
            return null;
        }
        return list;
    }
    
    /**
     * 
     * <p>
     * 得到columnName的子列标题集
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:19:10 PM
     * @param columnName 列标题名称（key�?
     * @return
     */
    public List<ColumnTitle> getChildrenColumn(String columnName) {
        if(columnName == null || columnName.equals("")) {
            return null;
        }
        List<ColumnTitle> list = new ArrayList<ColumnTitle>();
        for(int i=0; i<columnTitleList.size(); i++) {
        	ColumnTitle ct = columnTitleList.get(i);
            if(columnName.equals(ct.getParentColumnName())) {
                list.add(ct);
            }
        }
        if(list.size() == 0) {
            return null;
        }
        return list;
    }
    
    /**
     * 
     * <p>
     * 得到rowName的子行标题集
     * </p>
     * @author ZDL 时间 Jan 11, 2013 4:19:10 PM
     * @param rowName 行标题名称（key�?
     * @return
     */
    public List<RowTitle> getChildrenRow(String rowName) {
        if(rowName == null || rowName.equals("")) {
            return null;
        }
        List<RowTitle> list = new ArrayList<RowTitle>();
        for(int i=0; i<rowTitleList.size(); i++) {
        	RowTitle rt = rowTitleList.get(i);
            if(rowName.equals(rt.getParentRowName())) {
                list.add(rt);
            }
        }
        if(list.size() == 0) {
            return null;
        }
        return list;
    }
    
    /**
     * 
     * <p>
     * 计算表格列数
     * </p>
     * @author ZDL 时间 Jan 11, 2013 3:41:40 PM
     * @return
     */
    public int getTableColumnCount(){
        int rowLevelCount = getRowTitleLevelCount();
        List<ColumnTitle> list = getTerminalColumnTitles();
        if(list == null) {
            return rowLevelCount;
        }
        return rowLevelCount + list.size();
    }
    
    /**
     * 
     * <p>
     * 计算行标题层级数
     * </p>
     * @author ZDL 时间 Jan 11, 2013 3:35:51 PM
     * @return
     */
    public int getRowTitleLevelCount(){
        int c = 0;
        RowTitle rt = null;
        for(int i=0; i<rowTitleList.size(); i++) {
            rt = rowTitleList.get(i);
            if(rt.getLevel() > c) {
                c = rt.getLevel();
            }
        }
        return c;
    }
    
    /**
     * 
     * <p>
     * 计算列标题层级数
     * </p>
     * @author ZDL 时间 Jan 11, 2013 3:37:47 PM
     * @return
     */
    public int getColumnTitleLevelCount(){
        int c = 0;
        ColumnTitle ct = null;
        for(int i=0; i<columnTitleList.size(); i++) {
            ct = columnTitleList.get(i);
            if(ct.getLevel() > c) {
                c = ct.getLevel();
            }
        }
        return c;
    }
    
    /**
     * 初始化行标题Level属�?的�?，Level�?�?��
     */
    private void initRowTitleLevel(){
    	if(rowTitleList == null || rowTitleList.size() == 0) {
    		return;
    	}
    	for(int i=0; i<rowTitleList.size(); i++) {
    		RowTitle ct = rowTitleList.get(i);
    		if(ct.getParentRowName() == null) {
    			ct.setLevel(1);
    		}else {
    			ct.setLevel(getRowTitleLevel(ct.getParentRowName()) + 1);
    		}
    	}
    }
    
    /**
     * 初始化列标题Level属�?的�?，Level�?�?��
     */
    private void initColumnTitleLevel(){
    	if(columnTitleList == null || columnTitleList.size() == 0) {
    		return;
    	}
    	for(int i=0; i<columnTitleList.size(); i++) {
    		ColumnTitle ct = columnTitleList.get(i);
    		if(ct.getParentColumnName() == null) {
    			ct.setLevel(1);
    		}else {
    			ct.setLevel(getColumnTitleLevel(ct.getParentColumnName()) + 1);
    		}
    	}
    }
    
    /**
     * 初始化列标题合并行数
     */
    private void initColumnTitleRowspan() {
    	if(columnTitleList == null || columnTitleList.size() == 0) {
    		return;
    	}
    	for(int i=0; i<columnTitleList.size(); i++) {
    		ColumnTitle ct = columnTitleList.get(i);
    		//rowspan colspan
    		List<ColumnTitle> childTitleList = this.getChildrenColumn(ct.getName());
    		/*
    		 * 若不存在子列，表示此节点列为末端列，无需合并列，但需计算合并行数�?
    		 * 若存在子列，则此节点合并列数为子节点个数，无�?��并行
    		 */
    		if(childTitleList == null || childTitleList.size() == 0) {
    			if(getColumnTitleLevelCount() != ct.getLevel()) {//计算合并行数
    				ct.setRowspan((getColumnTitleLevelCount() - ct.getLevel() + 1) + "");//设置合并行数
    			}
    		}else {
    			ct.setColspan(childTitleList.size() + "");//依据子列标题个数设置合并列数
    		}
    	}
    }
    
    /**
     * 初始化行标题合并列数
     */
    private void initRowTitleColspan() {
    	if(rowTitleList == null || rowTitleList.size() == 0) {
    		return;
    	}
    	for(int i=0; i<rowTitleList.size(); i++) {
    		RowTitle ct = rowTitleList.get(i);
    		//rowspan colspan
    		List<RowTitle> childTitleList = this.getChildrenRow(ct.getName());
    		/*
    		 * 若不存在子行，表示此节点行为末端行，无需合并行，但需计算合并列数�?
    		 * 若存在子行，则此节点合并行数为子节点个数，无�?��并列
    		 */
    		if(childTitleList == null || childTitleList.size() == 0) {
    			if(getRowTitleLevelCount() != ct.getLevel()) {
    				ct.setColspan((getRowTitleLevelCount() - ct.getLevel() + 1) + "");//设置合并行数
    			}
    		}else {
    			ct.setRowspan(childTitleList.size() + "");//依据子列标题个数设置合并列数
    		}
    	}
    }
    
    /**
     * 计算行标题层�?
     * @param titleName 行标题（key�?
     * @return
     */
    private int getRowTitleLevel(String titleName) {
    	int level = -1;
    	for(int i=0; i<rowTitleList.size(); i++) {
    		RowTitle title = rowTitleList.get(i);
    		if(title.getName().equals(titleName)) {
    			if(title.getParentRowName() == null) {
    				return 1;
    			}else {
    				return getRowTitleLevel(title.getParentRowName()) + 1;
    			}
    		}
    	}
    	return level;
    }
    
    /**
     * 计算列标题层�?
     * @param titleName 列标题（key�?
     * @return
     */
    private int getColumnTitleLevel(String titleName) {
    	int level = -1;
    	for(int i=0; i<columnTitleList.size(); i++) {
    		ColumnTitle title = columnTitleList.get(i);
    		if(title.getName().equals(titleName)) {
    			if(title.getParentColumnName() == null) {
    				return 1;
    			}else {
    				return getColumnTitleLevel(title.getParentColumnName()) + 1;
    			}
    		}
    	}
    	return level;
    }
    
    /**
     * 是否显示表格标题
     * true：是；false：否
     * @return
     */
    public boolean isShowTitle(){
        
        return showTitle;
    }
    
    /**
     * 设置是否显示表格标题
     * true：是；false：否
     * @return
     */
    public void setShowTitle(boolean showTitle){
    
        this.showTitle = showTitle;
    }
    
    /**
     * get 表格width
     * @return
     */
    public String getTableWidth(){
    
        return tableWidth;
    }
    
    /**
     * set 表格width
     * @return
     */
    public void setTableWidth(String tableWidth){
    
        this.tableWidth = tableWidth;
    }
    
    /**
     * get 表格css style
     * @return
     */
    public String getTableCssStyle() {
		return tableCssStyle;
	}

    /**
     * set 表格css style
     * @return
     */
	public void setTableCssStyle(String tableCssStyle) {
		this.tableCssStyle = tableCssStyle;
	}

	/**
     * get 表格css class
     * @return
     */
	public String getTableCssClass() {
		return tableCssClass;
	}

	/**
     * set 表格css class
     * @return
     */
	public void setTableCssClass(String tableCssClass) {
		this.tableCssClass = tableCssClass;
	}
    
	/**
     * get 表格左上角内�?
     * @return
     */
    public String getLeftTop(){
        
        return leftTop;
    }
    
    /**
     * set 表格左上角内�?
     * @return
     */
    public void setLeftTop(String leftTop){
    
        this.leftTop = leftTop;
    }
    
    /**
     * get 表格列标题集
     * @return
     */
    public List<ColumnTitle> getColumnTitleList(){
    
        return columnTitleList;
    }
    
    /**
     * set 表格列标题集
     * @return
     */
    public void setColumnTitleList(List<ColumnTitle> columnTitleList){
    
        this.columnTitleList = columnTitleList;
        initColumnTitleLevel();
        initColumnTitleRowspan();
    }
    
    /**
     * get 表格行标题集
     * @return
     */
    public List<RowTitle> getRowTitleList(){
    
        return rowTitleList;
    }
    
    /**
     * set 表格行标题集
     * @return
     */
    public void setRowTitleList(List<RowTitle> rowTitleList){
    
        this.rowTitleList = rowTitleList;
        initRowTitleLevel();
        initRowTitleColspan();
    }

    /**
     * get 表格css cellspacing
     * @return
     */
	public String getCellspacing() {
		return cellspacing;
	}

	/**
     * set 表格css cellspacing
     * @return
     */
	public void setCellspacing(String cellspacing) {
		this.cellspacing = cellspacing;
	}

	/**
     * get 表格css cellpadding
     * @return
     */
	public String getCellpadding() {
		return cellpadding;
	}

	/**
     * set 表格css cellpadding
     * @return
     */
	public void setCellpadding(String cellpadding) {
		this.cellpadding = cellpadding;
	}

	/**
     * get 表格css border
     * @return
     */
	public String getBorder() {
		return border;
	}

	/**
     * set 表格css border
     * @return
     */
	public void setBorder(String border) {
		this.border = border;
	}
}

