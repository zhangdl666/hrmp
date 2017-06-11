package com.platform.core.bo;

import java.util.List;

/**
 * 分页page
 * @author zdl
 *
 */
public class Page {

	/**
	 * 总记录数
	 */
	private int totalRowSize;
	
	/**
	 * 当前页
	 */
	private int currentPage;
	
	/**
	 * 每页记录数
	 */
	private int pageSize;

	/**
	 * 查询结果
	 */
	private List result;

	/**
     * 得到总页数
     * @return 总页数
     */
    public int getTotalPage(){
        int totalPage = (totalRowSize % pageSize == 0)? (totalRowSize / pageSize): (totalRowSize / pageSize) + 1;
        return totalPage;
    }
    
    /**
     * 得到当前开始记录号
     * @param pageSize 每页记录数
     * @param currentPage 当前页
     * @return
     */
    public int getCurrentPageOffset()
    {
        int offset = pageSize * (currentPage - 1);
        return offset;
    }
    
    
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRowSize() {
		return totalRowSize;
	}

	public void setTotalRowSize(int totalRowSize) {
		this.totalRowSize = totalRowSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}
	
	public static Page getDefaultPage() {
		Page page = new Page();
		page.setCurrentPage(1);
		page.setPageSize(10);
		return page;
	}
}
