package com.spring.room.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.room.event.IRoomEvent;
import com.spring.logic.room.info.RoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.event.DeployRoomEvent;

public class RoomLoopThread implements Runnable {
	
	private static final Log logger = LogFactory.getLog(RoomLoopThread.class);
	
	private List<RoomInfo> list = new ArrayList<>();
	
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();
	
	private RoomControlService roomControlService;
	
	private volatile boolean stop = false;
	
	private long SLEEP_TIME = 50;

	@Override
	public void run() {
		long sleepTime = SLEEP_TIME;
		
		while (!stop) {
			try {
				IRoomEvent roomEvent = queue.poll(sleepTime, TimeUnit.MILLISECONDS);
				
				if (roomEvent != null) {
					if (roomEvent instanceof DeployRoomEvent) {
						list.add(((DeployRoomEvent)roomEvent).getRoomInfo());
						((DeployRoomEvent)roomEvent).getRoomInfo().setRoomState(0);
					}
				}
				
				long start = System.currentTimeMillis();
				
				for (RoomInfo roomInfo : list) {
					this.roomControlService.loopRoomInfo(roomInfo);
				}
				
				long use = (System.currentTimeMillis() - start);
				
				if (SLEEP_TIME - use < 0) {
					logger.warn("loop time is too long " + (use - SLEEP_TIME));
					sleepTime = 10;
				} else {
					sleepTime = SLEEP_TIME - use;
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
		stop = true;
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
	
	public boolean isStop() {
		return stop;
	}
	
	public void cancel() {
		stop = true;
	}

	@Autowired
	public void setRoomControlService(RoomControlService roomControlService) {
		this.roomControlService = roomControlService;
	}
	
	
}
