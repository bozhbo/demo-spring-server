package com.spring.logic.server.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.IoSession;

import com.spring.common.ServerName;

public class GateServerCache {

	private static Map<String, IoSession> gateServerMap = new ConcurrentHashMap<>();
	
	public static void addIoSession(String gateServerName, IoSession session) {
		gateServerMap.put(gateServerName, session);
	}
	
	public static void removeIoSession(String gateServerName) {
		gateServerMap.remove(gateServerName);
	}
	
	public static IoSession getIoSession(String gateServerName) {
		return gateServerMap.get(gateServerName);
	}
	
	public static IoSession getIoSession(int gateId) {
		return gateServerMap.get(ServerName.GATE_SERVER_NAME + "-" + gateId);
	}
	
	public static Set<Entry<String, IoSession>> getSet() {
		return gateServerMap.entrySet();
	}
}
