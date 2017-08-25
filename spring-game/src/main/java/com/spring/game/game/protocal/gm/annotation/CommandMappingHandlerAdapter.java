package com.snail.webgame.game.protocal.gm.annotation;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import com.snail.webgame.game.protocal.gm.service.CommandService;

public class CommandMappingHandlerAdapter {

	private static final ConcurrentMap<String, Method> commandMethods = new ConcurrentHashMap<String, Method>();
	/**
	 * 忽略GM命令区分大小写 true-忽略 false-区分
	 */
	private static boolean CMD_IGNORE_CASE = true;
	
	public static void setCmdIgnoreCase(boolean ignoreCase) {
		CMD_IGNORE_CASE = ignoreCase;
	}

	/**
	 * 启动初始加载 CommandMapping value为空 默认为方法名
	 * 
	 * @param clazz
	 */
	public static void init(Class<? extends CommandService> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		if (methods != null && methods.length > 0) {
			String command = "";
			for (Method method : methods) {
				CommandMapping ann = AnnotationUtils.findAnnotation(method, CommandMapping.class);
				if (ann != null) {
					command = ann.value();
					if (StringUtils.isNotBlank(command)) {
						commandMethods.putIfAbsent(CMD_IGNORE_CASE ? command.toUpperCase() : command, method);
					} else {
						commandMethods.putIfAbsent(
								CMD_IGNORE_CASE ? method.getName().toUpperCase() : method.getName(), method);
					}
				}
			}
		}
	}

	/**
	 * 根据命令获取相应方法
	 * 
	 * @param command
	 * @return
	 */
	public static Method getCommandMethod(String command) {
		return commandMethods.get(CMD_IGNORE_CASE ? command.toUpperCase() : command);
	}
}
