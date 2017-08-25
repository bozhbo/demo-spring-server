package com.snail.webgame.engine.game.base.actor.cahe;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.game.base.actor.support.IActor;

/**
 * 
 * 类介绍:活跃对象存储类，用于常住内存的强引用对象存储
 * Actor:在游戏过程中会改变的对象，类似于RoleInfo
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class BaseSystemActors {
	
	private static Logger logger = LoggerFactory.getLogger("logs");
	
	public static Map<String, Map<Integer, IActor>> actorMap = new HashMap<String, Map<Integer, IActor>>();

	public static synchronized void initActor(Class<?> ... paths) {
		if (actorMap.size() > 0) {
			return;
		}
		
		for (Class<?> path : paths) {
			if (actorMap.containsKey(path.getName())) {
				if (logger.isWarnEnabled()) {
					logger.warn("BaseSystemActors[initActor] : add key is in map");
				}
			} else {
				actorMap.put(path.getName(), new ConcurrentHashMap<Integer, IActor>());
			}
		}
	}
	
	public static void addActor(IActor actor) {
		Map<Integer, IActor> map = actorMap.get(actor.getClass().getName());
		
		if (map != null) {
			map.put(actor.getId(), actor);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getActor(Class<T> path, int actorId) {
		Map<Integer, IActor> map = actorMap.get(path.getName());
		
		if (map != null) {
			return (T)map.get(actorId);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T removeActor(Class<T> path, int actorId) {
		Map<Integer, IActor> map = actorMap.get(path.getName());
		
		if (map != null) {
			return (T)map.remove(actorId);
		}
		
		return null;
	}
}
