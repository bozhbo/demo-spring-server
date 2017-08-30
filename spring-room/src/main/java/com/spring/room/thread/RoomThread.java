package com.spring.room.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.room.event.IRoomEvent;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.event.DeployRoomEvent;

public class RoomThread implements Runnable {
	
	private static final Log logger = LogFactory.getLog(RoomThread.class);
	
	/**
	 * 每个线程最大开启房间数量
	 */
	private static int THREAD_MAX_ROOM_COUNT = 100;
	
	/**
	 * 每个进程最大开启线程数量
	 */
	private static int PROCESS_MAX_THREAD_COUNT = 100;
	
	private List<RoomLoopThread> list = new ArrayList<>();
	
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();
	
	private RoomControlService roomControlService;

	@Override
	public void run() {
		while (true) {
			try {
				IRoomEvent roomEvent = queue.poll(1, TimeUnit.SECONDS);
				
				if (roomEvent != null) {
					if (roomEvent instanceof DeployRoomEvent) {
						if (list.size() >= PROCESS_MAX_THREAD_COUNT) {
							this.roomControlService.deployRoomInfoFailed(((DeployRoomEvent)roomEvent).getRoomInfo());
						} else {
							if (!addRoomInfo(list, roomEvent)) {
								this.roomControlService.deployRoomInfoFailed(((DeployRoomEvent)roomEvent).getRoomInfo());
							} else {
								if (addRoomInfo(list, roomEvent)) {
									this.roomControlService.deployRoomInfoSuccessed(((DeployRoomEvent)roomEvent).getRoomInfo());
								} else {
									this.roomControlService.deployRoomInfoFailed(((DeployRoomEvent)roomEvent).getRoomInfo());
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	private boolean addRoomInfo(List<RoomLoopThread> list, IRoomEvent roomEvent) {
		RoomLoopThread minRoomLoopThread = null;
		int minValue = THREAD_MAX_ROOM_COUNT;
		
		Set<RoomLoopThread> stopSet = new HashSet<>();
		
		for (RoomLoopThread roomLoopThread : list) {
			if (roomLoopThread.isStop()) {
				stopSet.add(roomLoopThread);
				continue;
			}
			
			if (roomLoopThread.getRoomSize() < (THREAD_MAX_ROOM_COUNT / 2)) {
				roomLoopThread.addRoomEvent(roomEvent);
				return true;
			} else {
				if (minValue > roomLoopThread.getRoomSize()) {
					minRoomLoopThread = roomLoopThread;
					minValue = roomLoopThread.getRoomSize();
				}
			}
		}
		
		if (minValue == THREAD_MAX_ROOM_COUNT) {
			return false;
		}
		
		minRoomLoopThread.addRoomEvent(roomEvent);
		
		for (RoomLoopThread roomLoopThread : stopSet) {
			list.remove(roomLoopThread);
		}
		
		return true;
	}
	
	public boolean addRoomEvent(IRoomEvent roomEvent) {
		try {
			queue.add(roomEvent);
			return true;
		} catch (Exception e) {
			logger.error("RoomThread : addRoomEvent error", e);
		}
		
		return false;
	}

	@Autowired
	public void setRoomControlService(RoomControlService roomControlService) {
		this.roomControlService = roomControlService;
	}
}
