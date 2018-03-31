package com.platform.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ApplicationUtil {

	private static ApplicationContext applicationContext;

	/**
	 * 加载Spring配置文件时，如果Spring配置文件中所定义的Bean类实现了ApplicationContextAware接口，会自动调用该方法
	 * 
	 * @param applicationContext
	 * @throws BeansException
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationUtil.applicationContext = applicationContext;
	}

	/**
	 * 获取Spring上下文
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取Spring Bean
	 * 
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}
}
