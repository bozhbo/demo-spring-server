package com.snail.webgame.game.cache;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.mina.common.IoSession;

/**
 * 游戏服务器上的各种子系统
 * 
 * @author cici
 * 
 */
public class ServerMap {

	private static Hashtable<String, IoSession> map = new Hashtable<String, IoSession>();

	public static void addServer(String serverName, IoSession session) {
		map.put(serverName, session);
	}

	public static IoSession getServerSession(String serverName) {
		return map.get(serverName);
	}

	public static Set<String> getMapSet() {
		return map.keySet();
	}

	public static void removeServer(String serverName) {
		map.remove(serverName);
	}
	
	public static Map<String, IoSession> getMap() {
		return map;
	}
}
