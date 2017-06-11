package com.platform.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.report.datadef.Data2D;
import com.platform.report.datadef.DefaultData2D;

public class BaseReportDaoImpl implements BaseReportDao  {
	
	private static Logger logger = Logger
			.getLogger(BaseReportDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	/** 
	 * 功能：通过入参的数据库连接和SQL数组，查询SQL数组中各个SQL的数据集，插入Data2D结果集中返回。
	 * 
	 * 参数：isNormalResult:true=结果集行列不转换，false=结果集行列转换;
	 * 		isNumberResult:true=数字结果集，false=非数字结果集【resultSet.getFloat(j+2)是否为数字】
	 * 		sql=要执行的SQL数组
	 * 
	 * sql参数设置：
	 * 		1.必须是SELECT查询语句。
	 * 		2.输出结果第一列为行key
	 * 		3.第二列输出字段的别名将为Data2D的列名，没有别名将使用默认SQL列名。
	 * 
	 * @throws SQLException 
	 */
	
	public Data2D query(boolean isNormalResult,boolean isNumberResult,String... sql) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		Connection con = ((SessionFactoryImplementor)session.getSessionFactory()).getConnectionProvider().getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		DefaultData2D data = new DefaultData2D();
		try {
			statement = con.createStatement();
			for (int i = 0; i < sql.length; i++) {
				if(sql[i] == null || sql[i].trim().equals("")) {
					continue;
				}
				logger.info(sql[i]);
				resultSet = statement.executeQuery(sql[i]);
				ResultSetMetaData rsmd = resultSet.getMetaData() ;
				while (resultSet.next()) {
					for (int j = 0; j < rsmd.getColumnCount()-1; j++) {
						if(isNormalResult){//无需行列转换
							if (isNumberResult) {//数字结果集
								data.addValue(resultSet.getFloat(j+2), resultSet.getString(1), rsmd.getColumnName(j+2));
							}else{//非数字结果集
								data.addValue(resultSet.getString(j+2), resultSet.getString(1), rsmd.getColumnName(j+2));
							}
						}else{
							if (isNumberResult) {//数字结果集
								data.addValue(resultSet.getFloat(j+2), rsmd.getColumnName(j+2),resultSet.getString(1));
							}else{//非数字结果集
								data.addValue(resultSet.getString(j+2), rsmd.getColumnName(j+2),resultSet.getString(1));
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally{
			this.destroyCon(resultSet, statement, con);
		}
		return data;
	}
	
	
	public Data2D prepareStatementQuery(boolean isNormalResult,boolean isNumberResult,String sql,HashMap<String, String> paraMap) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		Connection con = ((SessionFactoryImplementor)session.getSessionFactory()).getConnectionProvider().getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		DefaultData2D data = new DefaultData2D();
		try {
			statement = con.prepareStatement(sql);
			if(paraMap != null && !paraMap.isEmpty()) {
				Set<String> keys = paraMap.keySet();
				Iterator<String> it = keys.iterator();
				while(it.hasNext()) {
					String key = it.next();
					statement.setString(Integer.valueOf(key), paraMap.get(key));
				}
			}
			
			logger.info(sql);
			resultSet = statement.executeQuery();
			ResultSetMetaData rsmd = resultSet.getMetaData() ;
			while (resultSet.next()) {
				for (int j = 0; j < rsmd.getColumnCount()-1; j++) {
					if(isNormalResult){//无需行列转换
						if (isNumberResult) {//数字结果集
							data.addValue(resultSet.getFloat(j+2), resultSet.getString(1), rsmd.getColumnName(j+2));
						}else{//非数字结果集
							data.addValue(resultSet.getString(j+2), resultSet.getString(1), rsmd.getColumnName(j+2));
						}
					}else{
						if (isNumberResult) {//数字结果集
							data.addValue(resultSet.getFloat(j+2), rsmd.getColumnName(j+2),resultSet.getString(1));
						}else{//非数字结果集
							data.addValue(resultSet.getString(j+2), rsmd.getColumnName(j+2),resultSet.getString(1));
						}
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}finally{
			this.destroyCon(resultSet, statement, con);
		}
		return data;
	}
	
	public void destroyCon(ResultSet resultSet,Statement statement,Connection con){
		try {
			if(resultSet!=null){
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(statement!=null){
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(con!=null){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public List createQuery(String hql) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}
}
