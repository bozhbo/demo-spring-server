package com.snail.webgame.engine.game.base.actor.cahe;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.engine.game.base.actor.support.IConstant;

/**
 * 
 * 类介绍:不活跃对象存储类，用于常住内存的强引用对象存储
 * Constant：在游戏过程中不会改变的对象,类似于Skill.xml的加载对象
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class BaseSystemConstant {
	public static Map<String, Map<Integer, IConstant>> stoneMap = new HashMap<String, Map<Integer, IConstant>>();
	
	public static synchronized void initConstant(Class<?> ... paths) {
		if (stoneMap.size() > 0) {
			return;
		}
		
		for (Class<?> path : paths) {
			stoneMap.put(path.getName(), new HashMap<Integer, IConstant>());
		}
	}
	
	public static void addConstant(IConstant constant) {
		Map<Integer, IConstant> map = stoneMap.get(constant.getClass().getName());
		
		if (map != null) {
			map.put(constant.getId(), constant);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getConstant(Class<T> path, int stoneId) {
		Map<Integer, IConstant> map = stoneMap.get(path.getName());
		
		if (map != null) {
			return (T)map.get(stoneId);
		}
		
		return null;
	}
}
