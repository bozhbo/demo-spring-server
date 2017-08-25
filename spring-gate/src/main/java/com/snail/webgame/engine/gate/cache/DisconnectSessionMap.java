package com.snail.webgame.engine.gate.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.engine.gate.common.DisconnectInfo;

/**
 * 临时断开的客户端连接集合
 * 
 * @author leiqiang
 *
 */
public class DisconnectSessionMap {
	private static ConcurrentHashMap<Integer, DisconnectInfo> map_close = new ConcurrentHashMap<Integer, DisconnectInfo>();

	public static void addDisconnectInfo(DisconnectInfo disconnectInfo) {
		map_close.put(disconnectInfo.getSeq(), disconnectInfo);
	}

	public static DisconnectInfo getDisconnectInfo(int sequenceId) {
		return map_close.get(sequenceId);
	}

	public static void removeDisconnectInfo(int sequenceId) {
		map_close.remove(sequenceId);
	}

	public static int getSize() {
		return map_close.size();
	}

	public static ConcurrentHashMap<Integer, DisconnectInfo> getMap() {
		return map_close;
	}
}
