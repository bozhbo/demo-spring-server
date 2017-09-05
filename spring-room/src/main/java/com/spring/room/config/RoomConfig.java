package com.spring.room.config;

import com.spring.room.bean.GlobalBeanFactory;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.thread.RoomThread;

public class RoomConfig {

	private static RoomThread roomThread;
	
	public static void init() {
		roomThread = new RoomThread(8);
		roomThread.setRoomControlService(GlobalBeanFactory.getBeanByName(RoomControlService.class));
		roomThread.start();
	}
}
