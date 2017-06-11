package com.platform.report.po;

import java.io.Serializable;
import java.util.Date;

import com.platform.report.datadef.Data2D;

/**
 *  <p>
 *  报表对象
 *  </p>
 *  @author ZDL 时间 Nov 12, 2012 4:31:43 PM
 *  @version 1.0 
 *  </br>
 *  �?��修改�?�?
 * 
 */
public class Report implements Serializable {

    /**   
     * serialVersionUID : TODO
     */  
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 报表ID
     */
    private String id;
    /**
     * 标题
     */
    public String title;
    /**
     * 描述
     */
    public String description;
    /**
     * 数据统计截止时间
     */
    public Date updateDate;
    
    /**
     * 报表数据�?
     */
    public Data2D dataSet;
    
    /**
     * 是否允许查看清单
     */
    public boolean viewDetail;
    
    public Report(){
        this.viewDetail = false;
    }


    /**
     * get 标题
     * @return
     */
    public String getTitle(){
    
        return title;
    }

    /**
     * set 标题
     * @return
     */
    public void setTitle(String title){
    
        this.title = title;
    }

    /**
     * get 报表描述
     * @return
     */
    public String getDescription(){
    
        return description;
    }

    /**
     * set 报表描述
     * @return
     */
    public void setDescription(String description){
    
        this.description = description;
    }

    /**
     * get 数据统计截止时间
     * @return
     */
    public Date getUpdateDate(){
    
        return updateDate;
    }

    /**
     * set 数据统计截止时间
     * @return
     */
    public void setUpdateDate(Date updateDate){
    
        this.updateDate = updateDate;
    }

    /**
     * get 报表数据�?
     * @return
     */
    public Data2D getDataSet(){
    
        return dataSet;
    }

    /**
     * set 报表数据�?
     * @return
     */
    public void setDataSet(Data2D dataSet){
    
        this.dataSet = dataSet;
    }


    /**
	 * 是否允许查看清单
	 * true：是；false：否
	 * @return
	 */
    public boolean isViewDetail(){
    
        return viewDetail;
    }


    /**
	 * 设置是否允许查看清单
	 * true：是；false：否
	 * @return
	 */
    public void setViewDetail(boolean viewDetail){
    
        this.viewDetail = viewDetail;
    }


    /**
     * get id
     * @return
     */
    public String getId(){
    
        return id;
    }



    /**
     * set id
     * @return
     */
    public void setId(String id){
    
        this.id = id;
    }
}

