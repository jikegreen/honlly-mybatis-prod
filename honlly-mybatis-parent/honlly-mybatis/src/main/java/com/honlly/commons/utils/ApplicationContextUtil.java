/**
 * 
 */
package com.honlly.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @date 2015年9月8日 下午5:27:17 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class ApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/*-spring.xml");;

	/**
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 * @author wangk
	 * 创建日期：2015年9月8日
	 * 修改说明：
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		ctx = context;
	}
	
	public static ApplicationContext getContext() {
		return ctx;
	}
	
	/**
	 * 
	 * @Date:2015年9月8日
	 * @author wangk
	 * @param clazz
	 * @return
	 * @Description:
	 */
	public static <T> T getBean(Class<T> clazz){
		return ctx.getBean(clazz);
	}

}
