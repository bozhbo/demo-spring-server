package com.spring.room.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.IoSession;

import com.spring.common.ServerName;

public class GateServerCache {

	private static Map<String, IoSession> gateServerMap = new ConcurrentHashMap<>();
	
	public void addIoSession(String gateServerName, IoSession session) {
		gateServerMap.put(gateServerName, session);
	}
	
	public void removeIoSession(String gateServerName) {
		gateServerMap.remove(gateServerName);
	}
	
	public IoSession getIoSession(String gateServerName) {
		return gateServerMap.get(gateServerName);
	}
	
	public IoSession getIoSession(int gateId) {
		return gateServerMap.get(ServerName.GATE_SERVER_NAME + "-" + gateId);
	}
}
