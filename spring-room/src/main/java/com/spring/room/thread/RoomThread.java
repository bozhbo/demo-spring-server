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

import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.event.IRoomEvent;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.event.DeployRoleInfoEvent;
import com.spring.room.event.DeployRoomEvent;

/**
 * 房间全局单线程类
 * 
 * 可处理全局事件保证无锁操作
 * 
 * @author Administrator
 *
 */
public class RoomThread extends Thread {

	private static final Log logger = LogFactory.getLog(RoomThread.class);

	/**
	 * 每个线程最大开启房间数量
	 */
	private static int THREAD_MAX_ROOM_COUNT = 100;
	
	/**
	 * 每个线程最大人数
	 */
	private static int THREAD_MAX_ROLE_COUNT = 1000;
	
	/**
	 * 房间管理线程编号
	 */
	private int lastIndex = 1;

	/**
	 * 房间管理线程，一般与CPU相等
	 */
	private List<RoomLoopThread> list;

	/**
	 * 事件队列
	 */
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();

	/**
	 * 房间业务Service
	 */
	private RoomControlService roomControlService;

	public RoomThread(int loopThreadSize) {
		super("RoomThread-1");
		list = new ArrayList<>(loopThreadSize);

		for (int i = 0; i < loopThreadSize; i++) {
			RoomLoopThread roomLoopThread = new RoomLoopThread();
			roomLoopThread.setName("RoomLoopThread-" + lastIndex++);
			roomLoopThread.start();
			list.add(roomLoopThread);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				IRoomEvent roomEvent = queue.poll(1, TimeUnit.SECONDS);

				if (roomEvent != null) {
					if (roomEvent instanceof DeployRoomEvent) {
						// 游戏发起添加房间
						if (addRoomInfo(list, roomEvent)) {
							this.roomControlService.deployRoomInfoSuccessed(((DeployRoomEvent) roomEvent).getRoomInfo());
						} else {
							this.roomControlService.deployRoomInfoFailed(((DeployRoomEvent) roomEvent).getRoomInfo());
						}
					} else if (roomEvent instanceof DeployRoleInfoEvent) {
						// 游戏发起玩家加入房间
						if (addRoleInfo(list, roomEvent)) {
							this.roomControlService.deployRoleInfoSuccessed(((DeployRoleInfoEvent) roomEvent).getRoomInfo(), ((DeployRoleInfoEvent) roomEvent).getRoleInfo());
						} else {
							this.roomControlService.deployRoleInfoSuccessed(((DeployRoleInfoEvent) roomEvent).getRoomInfo(), ((DeployRoleInfoEvent) roomEvent).getRoleInfo());
						}
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	/**
	 * 添加房间
	 * 
	 * @param list
	 * @param roomEvent
	 * @return	true-成功 false-失败
	 */
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
		
		for (RoomLoopThread roomLoopThread : stopSet) {
			list.remove(roomLoopThread);
			
			RoomLoopThread newRoomLoopThread = new RoomLoopThread();
			newRoomLoopThread.setName("RoomLoopThread-" + lastIndex++);
			newRoomLoopThread.start();
			list.add(newRoomLoopThread);
		}

		if (minValue == THREAD_MAX_ROOM_COUNT) {
			return false;
		}

		minRoomLoopThread.addRoomEvent(roomEvent);

		return true;
	}
	
	/**
	 * 添加角色
	 * 
	 * @param list
	 * @param roomEvent
	 * @return	true-成功 false-失败
	 */
	private boolean addRoleInfo(List<RoomLoopThread> list, IRoomEvent roomEvent) {
		int index = (int)(System.currentTimeMillis() % list.size());
		
		RoomLoopThread roomLoopThread = list.get(index);
		
		if (roomLoopThread.getRoomSize() * RoomConfig.ROOM_MAX_ROLES < THREAD_MAX_ROLE_COUNT) {
			roomLoopThread.addRoomEvent(roomEvent);
			return true;
		}
		
		for (RoomLoopThread tempRoomLoopThread : list) {
			if (tempRoomLoopThread.getRoomSize() * RoomConfig.ROOM_MAX_ROLES < THREAD_MAX_ROLE_COUNT) {
				tempRoomLoopThread.addRoomEvent(roomEvent);
				return true;
			}
		}
		
		return false;
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
