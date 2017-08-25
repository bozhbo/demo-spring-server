package com.snail.webgame.game.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.epilot.ccf.core.protocol.Message;

public class TempMsgrMap {

	private final static int limit = 100000;

	private static ConcurrentHashMap<Integer, Message> map = new ConcurrentHashMap<Integer, Message>();

	public static Message getMessage(int sequenceId) {
		return map.get(sequenceId);
	}

	private static int getMinKey() {
		int min = 0;
		for (int key : map.keySet()) {
			if (min == 0 || min > key) {
				min = key;
			}
		}
		return min;
	}

	public static void addMessage(int sequenceId, Message message) {
		if (map.size() > limit) {
			// 超上限去除老数据
			map.remove(getMinKey());
		}
		map.put(sequenceId, message);
	}

	public static void removeMessage(int sequenceId) {
		map.remove(sequenceId);
	}

}
