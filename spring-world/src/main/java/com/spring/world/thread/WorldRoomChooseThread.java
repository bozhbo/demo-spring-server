package com.spring.world.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.event.IRoomEvent;
import com.spring.world.event.ChooseRoomEvent;

public class WorldRoomChooseThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(WorldRoomChooseThread.class);
	
	private RoomTypeEnum roomTypeEnum;
	
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();
	
	private volatile boolean stop;
	
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
	
	
}
