package com.snail.webgame.engine.gate.cache;

import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.IoSession;

public class SequenceMap {

	private static ConcurrentHashMap<Integer, IoSession> map = new ConcurrentHashMap<Integer, IoSession>();

	public static IoSession getSession(int sequenceId) {
		return map.get(sequenceId);
	}

	public static void addSession(int sequenceId, IoSession session) {
		map.put(sequenceId, session);
	}

	public static void removeSession(int sequenceId) {
		map.remove(sequenceId);
	}

	public static boolean exist(int sequenceId) {
		return map.containsKey(sequenceId);
	}

	public static int getSize() {
		return map.size();
	}

	public static Set<Entry<Integer, IoSession>> entrySet() {
		return map.entrySet();
	}
}
