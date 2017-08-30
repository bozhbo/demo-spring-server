package com.spring.room.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.spring.logic.room.info.RoomInfo;
import com.spring.room.event.IRoomEvent;

public class RoomLoopThread implements Runnable {
	
	private static final Log logger = LogFactory.getLog(RoomLoopThread.class);
	
	private List<RoomInfo> list = new ArrayList<>();
	
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean addRoomEvent(IRoomEvent roomEvent) {
		try {
			queue.add(roomEvent);
			return true;
		} catch (Exception e) {
			logger.error("RoomLoopThread : addRoomEvent error", e);
		}
		
		return false;
	}

	public int getRoomSize() {
		return list.size();
	}
}
