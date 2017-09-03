package com.spring.world.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.event.IRoomEvent;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.RoomService;
import com.spring.world.event.ChooseRoomEvent;
import com.spring.world.room.service.RoomClientService;

public class WorldRoomChooseThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(WorldRoomChooseThread.class);
	
	private RoomTypeEnum roomTypeEnum;
	
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();
	
	private volatile boolean stop;
	
	private RoomService roomService;
	
	private RoomClientService roomClientService;
	
	public WorldRoomChooseThread(RoomTypeEnum roomTypeEnum) {
		super("RoomType-" + roomTypeEnum.getValue());
		this.roomTypeEnum = roomTypeEnum;
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				IRoomEvent roomEvent = queue.poll(1, TimeUnit.SECONDS);
				
				if (roomEvent != null) {
					if (roomEvent instanceof ChooseRoomEvent) {
						RoomInfo roomInfo = this.roomService.randomJoinRoom(roomTypeEnum, ((ChooseRoomEvent)roomEvent).getRoleInfo());
					
						if (roomInfo != null) {
							roomClientService.deployRoleInfo(roomInfo, ((ChooseRoomEvent)roomEvent).getRoleInfo());
						} else {
							logger.warn("");
						}
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
		stop = true;
	}

	public boolean isStop() {
		return stop;
	}
	
	public void cancel() {
		stop = true;
	}

	public RoomTypeEnum getRoomTypeEnum() {
		return roomTypeEnum;
	}
	
	public boolean addRoomEvent(IRoomEvent roomEvent) {
		try {
			queue.add(roomEvent);
			return true;
		} catch (Exception e) {
			logger.error("WorldRoomChooseThread : addRoomEvent error", e);
		}
		
		return false;
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}
	
	
}
