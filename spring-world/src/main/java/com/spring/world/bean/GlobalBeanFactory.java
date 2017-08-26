package com.spring.world.bean;

import com.spring.world.main.WorldServerMain;

public class GlobalBeanFactory {

	@SuppressWarnings("unchecked")
	public static <T> T getBeanByName(String name, Class<T> clazz) {
		Object obj = WorldServerMain.context.getBean(name);
		
		if (obj == null) {
			return WorldServerMain.context.getBean(clazz);
		} else {
			return (T)obj;
		}
	}
}
