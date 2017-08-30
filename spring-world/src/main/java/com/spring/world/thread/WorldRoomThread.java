package com.spring.world.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.service.RoomService;
import com.spring.world.io.process.role.login.LoginProcessor;
import com.spring.world.room.service.RoomClientService;

public class WorldRoomThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(WorldRoomThread.class);

	private Map<RoomTypeEnum, Integer> roleCountMap = new HashMap<>();
	
	private RoomService roomService;
	private RoomClientService roomClientService;
	
	public WorldRoomThread() {
		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_NEW, 0);
		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL1, 0);
		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL2, 0);
		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL3, 0);
		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL4, 0);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				this.roomService.roomResize(roleCountMap, (roomInfo) -> {return this.roomClientService.deployRoomInfo(roomInfo);});
			} catch (Exception e) {
				logger.error("WorldRoomThread error", e);
			} finally {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					logger.error("WorldRoomThread error", e);
				}
			}
		}
	}
	
	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

	@Autowired
	public void setRoomClientService(RoomClientService roomClientService) {
		this.roomClientService = roomClientService;
	}
	
}
