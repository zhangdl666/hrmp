package com.platform.report.po;

import java.util.List;


/**
 *  <p>
 *  行标�?
 *  </p>
 * 
 */
public class RowTitle extends Title {
    
    /**
     * 标题层级 ，start from 1
     */
    private int level;
    
    /**
     * 父行标题名称
     */
    private String parentRowName;

    /**
     * 计算行表达式
     */
    private List<Title> expressions;

    /**
     * get 计算行表达式
     */
    public List<Title> getExpressions(){
    
        return expressions;
    }

    /**
     * set 计算行表达式
     */
    public void setExpressions(List<Title> expressions){
    
        this.expressions = expressions;
    }


    /**
     * get 标题层级，start from 1
     * 
     */
    public int getLevel(){
    
        return level;
    }


    /**
     * set 标题层级，start from 1
     * 自动计算，无�?���?
     */
    public void setLevel(int level){
    
        this.level = level;
    }


    /**
     * get 父行标题名称
     */
    public String getParentRowName(){
    
        return parentRowName;
    }


    /**
     * set 父行标题名称
     */
    public void setParentRowName(String parentRowName){
    
        this.parentRowName = parentRowName;
    }
    
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RowTitle other = (RowTitle) obj;
		if (this.getName().equals(other.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	
}

