package com.platform.report.po;


/**
 *  <p>
 *  列标�?
 *  </p>
 * 
 */
public class ColumnTitle extends Title {

    /**
     * 标题层级 ，start from 1
     */
    private int level;
    
    /**
     * 父列标题名称
     */
    private String parentColumnName;
    
    /**
     * 计算列表达式
     */
    private String expression;

    /**
     * get 计算列表达式
     */
    public String getExpression(){
    
        return expression;
    }

    /**
     * set 计算列表达式
     */
    public void setExpression(String expression){
    
        this.expression = expression;
    }


    /**
     * get 标题层级，start from 1
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
     * get 父列标题名称
     */
    public String getParentColumnName(){
    
        return parentColumnName;
    }


    /**
     * set 父列标题名称
     */
    public void setParentColumnName(String parentColumnName){
    
        this.parentColumnName = parentColumnName;
    }

}

