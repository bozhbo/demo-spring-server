package com.snail.webgame.engine.gate.util;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.IoSession;




/**
 * 身份认证Map，保存客户端的认证信息
 * @author tangjie
 *
 */
public class IdentityMap {
	
	private static  ConcurrentHashMap<Integer,IoSession> map = new ConcurrentHashMap<Integer,IoSession>();
	
	public static IoSession getSession(int roleId)
	{
		return map.get(roleId);
	}
	public static void addSession(int roleId,IoSession session)
	{
		map.put(roleId, session);
	}
	public static void removeSession(int roleId)
	{
		map.remove(roleId);
	}
	public static int getSize()
	{
		return map.size();
	}
	public static boolean isExistRole(int roleId)
	{
		return map.containsKey(roleId);
	}
	
	public static Set<Integer> keySet(){
		return map.keySet();
	}
	
	public static Set<Entry<Integer,IoSession>> entrySet(){
		return map.entrySet();
	}
	
	public static void clear(){
		map.clear();
	}
}
