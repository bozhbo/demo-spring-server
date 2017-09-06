package com.spring.room.config;

import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.thread.RoomThread;

public class RoomServerConfig {

	private static RoomThread roomThread;
	
	public static int ROOM_ID = 1;
	
	public static void init() {
		roomThread = new RoomThread(1);
		roomThread.setRoomControlService(GlobalBeanFactory.getBeanByName(RoomControlService.class));
		roomThread.start();
	}

	public static RoomThread getRoomThread() {
		return roomThread;
	}
	
	
}
