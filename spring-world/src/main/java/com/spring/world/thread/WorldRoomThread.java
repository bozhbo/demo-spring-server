package com.spring.world.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorldRoomThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(WorldRoomThread.class);

//	private Map<RoomTypeEnum, Integer> roleCountMap = new HashMap<>();
//	private Map<Integer, Integer> roomMap = new HashMap<>();
//	
//	private RoomService roomService;
//	private RoomClientService roomClientService;
//	
//	public WorldRoomThread() {
//		super("WorldRoomThread-1");
//		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_NEW, 0);
//		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL1, 0);
//		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL2, 0);
//		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL3, 0);
//		roleCountMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL4, 0);
//	}
	
	@Override
	public void run() {
//		while (true) {
//			try {
//				if (RoomServerCache.getSet().size() > 0) {
//					this.roomService.roomResize(roleCountMap, (roomInfo) -> {return this.deployRoomEnd(roomInfo, this.roomClientService.deployRoomInfo(roomInfo));});
//				}
//			} catch (Exception e) {
//				logger.error("WorldRoomThread error", e);
//			} finally {
//				try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					logger.error("WorldRoomThread error", e);
//				}
//			}
//		}
	}
	
//	private int deployRoomEnd(RoomInfo roomInfo, int roomServerId) {
//		if (roomServerId == 0) {
//			this.roomService.closeRoom(roomInfo);
//		} else {
//			roomMap.put(roomInfo.getRoomId(), roomServerId);
//		}
//		
//		return roomServerId;
//	}
	
//	public void setRoomService(RoomService roomService) {
//		this.roomService = roomService;
//	}
//
//	public void setRoomClientService(RoomClientService roomClientService) {
//		this.roomClientService = roomClientService;
//	}
	
}
