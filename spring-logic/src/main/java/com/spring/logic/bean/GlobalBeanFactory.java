package com.spring.logic.bean;

import org.springframework.context.ConfigurableApplicationContext;

public class GlobalBeanFactory {
	
	public static ConfigurableApplicationContext context = null;

	@SuppressWarnings("unchecked")
	public static <T> T getBeanByName(String name, Class<T> clazz) {
		Object obj = context.getBean(name);
		
		if (obj == null) {
			return context.getBean(clazz);
		} else {
			return (T)obj;
		}
	}
	
	public static <T> T getBeanByName(Class<T> clazz) {
		return context.getBean(clazz);
	}
}
