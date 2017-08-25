package com.snail.webgame.engine.game.base.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.game.base.annotation.MessageType;
import com.snail.webgame.engine.game.base.cache.MessageTypeMap;
import com.snail.webgame.engine.game.base.info.common.MessageTypeInfo;
import com.snail.webgame.engine.net.processor.IGameProcessor;

/**
 * 类介绍:Spring帮助类
 * 可通过名称或者Class获得bean对象，也可以直接获得{#ApplicationContext}
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class BaseBeanFactory {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public static ApplicationContext applicationContext = null;
	
	@SuppressWarnings("unchecked")
	public static void init(String path) {
		applicationContext = new ClassPathXmlApplicationContext(path);
		
		if (applicationContext == null) {
			System.exit(1);
		}
		
		try {
			Map<String, IGameProcessor> map = applicationContext.getBeansOfType(IGameProcessor.class);
			
			Set<Entry<String, IGameProcessor>> set = map.entrySet();
			
			for (Entry<String, IGameProcessor> entry : set) {
				Method[] methods = entry.getValue().getClass().getMethods();
				
				for (Method method : methods) {
					Annotation annotation = method.getAnnotation(MessageType.class);
					
					if (annotation instanceof MessageType) {
						MessageType messageType = (MessageType)annotation;
						MessageTypeInfo messageTypeInfo = new MessageTypeInfo();
						
						if (MessageTypeMap.getMessageTypeInfo(messageType.inputMsgType()) != null) {
							logger.error(String.format("Load messageType error, %s is used in %s", Integer.toHexString(messageType.inputMsgType()), MessageTypeMap.getMessageTypeInfo(messageType.inputMsgType()).getProcessor().toString()));
							throw new Exception(String.format("Load messageType error, %s is used in %s", Integer.toHexString(messageType.inputMsgType()), MessageTypeMap.getMessageTypeInfo(messageType.inputMsgType()).getProcessor().toString()));
						} else {
							messageTypeInfo.setMethod(method);
							messageTypeInfo.setProcessor(entry.getValue());
							messageTypeInfo.setMethodParameters(method.getParameterCount());
							entry.getValue().setMsgType(messageType.inputMsgType());
							
							Class<?> clazz[] = method.getParameterTypes();
							
							for (Class<?> class1 : clazz) {
								try {
									if (class1.newInstance() instanceof IRoomBody) {
										entry.getValue().setReq((Class<IRoomBody>) class1);
										continue;
									}
								} catch (Exception e) {
									logger.warn("BaseBeanFactory.init " + class1.getName() + " is change error");
								}
							}
							
							MessageTypeMap.addMessageTypeInfo(messageType.inputMsgType(), messageTypeInfo);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			System.exit(1);
		}
	}

	public static Object getBeanByName(String name) {
		return applicationContext.getBean(name);
	}
	
	public static <T> T getBeanByClass(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
