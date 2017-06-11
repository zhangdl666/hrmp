package com.platform.report.dao;

import java.util.HashMap;
import java.util.List;

import com.platform.report.datadef.Data2D;

public interface BaseReportDao {

	public Data2D query(boolean isNormalResult,boolean isNumberResult,String... sql) throws Exception;
	
	public Data2D prepareStatementQuery(boolean isNormalResult,boolean isNumberResult,String sql,HashMap<String, String> paraMap) throws Exception;
	
	public List createQuery(String hql);
}
