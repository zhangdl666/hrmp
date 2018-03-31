package com.platform.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ApplicationUtil {

	private static ApplicationContext applicationContext;

	/**
	 * ����Spring�����ļ�ʱ�����Spring�����ļ����������Bean��ʵ����ApplicationContextAware�ӿڣ����Զ����ø÷���
	 * 
	 * @param applicationContext
	 * @throws BeansException
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationUtil.applicationContext = applicationContext;
	}

	/**
	 * ��ȡSpring������
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * ��ȡSpring Bean
	 * 
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}
}
