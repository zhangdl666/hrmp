package com.platform.business.service;

public interface BusinessNumberService {

	/**
	 * 
	 * @param start 单号开头字母
	 * @param sequenceName 序列名称
	 * @return
	 */
	public String getNumber(String start,String sequenceName);
	
	/**
	 * 
	 * @param start 单号开头字母
	 * @return
	 */
	public String getNumber(String start);
}
