package com.snail.mina.protocol.config;


import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.processor.IProcessor;

/**
 * 
 * 类介绍:全局配置缓存管理类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RoomMessageConfig {

	private static Logger logger = LoggerFactory.getLogger("room");
	
	/**
	 * 全局消息体集合
	 */
	private static Map<Integer, Class<? extends IRoomBody>> reqMap = new HashMap<Integer, Class<? extends IRoomBody>>();
	
	/**
	 * 全局消息处理类集合
	 */
	public static Map<Integer, IProcessor> processorMap = new ConcurrentHashMap<Integer, IProcessor>();
	
	/**
	 * 全局消息处理类集合，临时启动使用
	 */
	private static List<IProcessor> processorList = new ArrayList<IProcessor>();
	
	/**
	 * 全局Socket连接Session集合
	 */
	public static Map<String, IoSession> serverMap = new ConcurrentHashMap<String, IoSession>();
	
	/**
	 * 注册保留字段
	 */
	public static Map<String, String> serverReserveMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * 消息进入量统计
	 */
	public static int inputCount = 0;
	
	/**
	 * 消息流出量统计
	 */
	public static int outputCount = 0;
	
	/**
	 * 根据消息号获取消息体
	 * 
	 * @param msgType	消息号
	 * @param buffer	数据buffer
	 * @param order		数据排序
	 * @return	IRoomBody	消息体
	 */
	public static IRoomBody getRoomBody(int msgType, ByteBuffer buffer, ByteOrder order) {
		if (reqMap.containsKey(msgType)) {
			try {
				IRoomBody roomBody = reqMap.get(msgType).newInstance();
				
				if (roomBody != null) {
					roomBody.setMsgType(msgType);
					roomBody.bytes2Req(buffer, order);
				}
				
				return roomBody;
			} catch (InstantiationException e) {
				logger.error("getRoomBody error", e);
			} catch (IllegalAccessException e) {
				logger.error("getRoomBody error", e);
			}
		}
		
		return null;
	}
	
	public static IRoomBody getRoomBody(int msgType) {
		if (reqMap.containsKey(msgType)) {
			try {
				return reqMap.get(msgType).newInstance();
			} catch (InstantiationException e) {
				logger.error("getRoomBody error", e);
			} catch (IllegalAccessException e) {
				logger.error("getRoomBody error", e);
			}
		}
		
		return null;
	}
	
	/**
	 * 添加IProcessor处理类
	 * 
	 * @param processor	IProcessor处理类
	 */
	public static void addProcessor(IProcessor processor) {
		if (processor == null) {
			return;
		}
		
		if (processor.getMsgType() <= 0) {
			logger.warn("Processor : " + processor.getClass().getName() + " messageType is incorrect");
			return;
		}
		
		processorList.add(processor);
	}
	
	/**
	 * 初始化IProcessor，用于所有的addProcessor添加完成后调用此方法
	 */
	public static void initProcessor() {
		for (IProcessor processor : processorList) {
			if (processor.getRoomBodyClass() != null) {
				addRoomBody(processor.getMsgType(), processor.getRoomBodyClass());
			}
			
			addProcessor(processor.getMsgType(), processor);
		}
		
		processorList.clear();
	}
	
	private static void addRoomBody(int msgType, Class<? extends IRoomBody> c) {
		reqMap.put(msgType, c);
	}
	
	private static void addProcessor(int msgType, IProcessor processor) {
		processorMap.put(msgType, processor);
	}
	
	/**
	 * 添加服务器IoSession
	 * 
	 * @param name		服务器名称
	 * @param session	IoSession
	 */
	public static void addIoSession(String name, IoSession session) {
		serverMap.put(name, session);
	}
}
