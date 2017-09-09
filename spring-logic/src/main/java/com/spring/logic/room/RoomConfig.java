package com.spring.logic.room;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.spring.logic.room.cache.RoomCahce;

public class RoomConfig {

	/**
	 * 房间ID生产器
	 */
	public static AtomicInteger roomIdSeq = new AtomicInteger(1);
	
	/**
	 * 每个房间最多人数
	 * TODO 各类型房间人数不一样
	 */
	public static int ROOM_MAX_ROLES = 5;
	
	/**
	 * 服务器总房间数量(World)
	 */
	public static int ROOM_MAX_COUNT = 10000;
	
	public static Random r = new Random();
	
	public static void init() {
		
	}
	
	public static int createRoomId() {
		return roomIdSeq.incrementAndGet();
	}
	
	public static int getRandom(int limit) {
		return r.nextInt(limit);
	}
}
