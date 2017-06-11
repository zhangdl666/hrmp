package com.platform.report.po;

/**
 * <p>
 * Table 标题
 * </p>
 * 
 */
public class Title {

	/**
	 * 列key或行key
	 */
	private String name;

	/**
	 * display name
	 */
	private String displayName;

	/**
	 * width
	 */
	private String width;

	/**
	 * 展示类型：Number、Percent、Text
	 */
	private String showType;

	/**
	 * 小数点位�?
	 */
	private int decimals;

	/**
	 * 是否计算行或�?
	 */
	private boolean isCalcu;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * css class
	 */
	private String cssClass;

	/**
	 * css style
	 */
	private String cssStyle;

	/**
	 * 是否允许查看清单
	 */
	private boolean viewDetail;

	/**
	 * get 标题css class样式
	 * 
	 * @return
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * set 标题css class样式
	 * 
	 * @return
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * get 标题css style样式
	 * 
	 * @return
	 */
	public String getCssStyle() {
		return cssStyle;
	}

	/**
	 * set 标题css style样式
	 * 
	 * @return
	 */
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	/**
	 * rowspan，无�?��户赋�?
	 */
	private String rowspan;

	/**
	 * colspan，无�?��户赋�?
	 */
	private String colspan;

	/**
	 * get 标题名称（key�?
	 * 
	 * @return
	 */
	public String getName() {

		return name;
	}

	/**
	 * set 标题名称（key�?
	 * 
	 * @return
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * get 标题显示名称
	 * 
	 * @return
	 */
	public String getDisplayName() {

		return displayName;
	}

	/**
	 * set 标题显示名称
	 * 
	 * @return
	 */
	public void setDisplayName(String displayName) {

		this.displayName = displayName;
	}

	/**
	 * get width
	 * @return
	 */
	public String getWidth() {

		return width;
	}

	/**
	 * set width
	 * @return
	 */
	public void setWidth(String width) {

		this.width = width;
	}

	/**
	 * get 合并行数
	 * @return
	 */
	public String getRowspan() {

		return rowspan;
	}

	/**
	 * set 合并行数
	 * 自动计算，无�?���?
	 * @return
	 */
	public void setRowspan(String rowspan) {

		this.rowspan = rowspan;
	}

	/**
	 * get 合并列数
	 * @return
	 */
	public String getColspan() {

		return colspan;
	}

	/**
	 * set 合并列数
	 * 自动计算，无�?���?
	 * @return
	 */
	public void setColspan(String colspan) {

		this.colspan = colspan;
	}

	/**
	 * get 内容显示格式
	 * @return
	 */
	public String getShowType() {

		return showType;
	}

	/**
	 * set 内容显示格式
	 * 可�?值：Table.SHOW_TYPE_NUMBER、Table.SHOW_TYPE_PERCENT、Table.SHOW_TYPE_TEXT
	 * @return
	 */
	public void setShowType(String showType) {

		this.showType = showType;
	}

	/**
	 * get 内容显示精度（针对数字内容）
	 * @return
	 */
	public int getDecimals() {

		return decimals;
	}

	/**
	 * set 内容显示精度（针对数字内容）
	 * @return
	 */
	public void setDecimals(int decimals) {

		this.decimals = decimals;
	}

	/**
	 * 是否计算行（列）
	 * @return
	 */
	public boolean isCalcu() {

		return isCalcu;
	}

	/**
	 * 设置是否计算行（列）
	 * true：是；false：否
	 * @return
	 */
	public void setCalcu(boolean isCalcu) {

		this.isCalcu = isCalcu;
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
	 * get 内容
	 * @return
	 */
	public String getContent() {

		return content;
	}

	/**
	 * set 内容
	 * @return
	 */
	public void setContent(String content) {

		this.content = content;
	}
}
