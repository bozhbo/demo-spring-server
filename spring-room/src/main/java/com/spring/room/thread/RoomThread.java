package com.spring.room.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.event.IRoomEvent;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.control.service.RoomLogicService;
import com.spring.room.control.service.RoomWorldService;
import com.spring.room.event.DeployRoleInfoEvent;
import com.spring.room.event.DeployRoomEvent;
import com.spring.room.event.RemoveRoleInfoEvent;
import com.spring.room.event.RemoveRoomEvent;

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
	 * 房间对应线程
	 */
	private Map<Integer, RoomLoopThread> roomMap = new ConcurrentHashMap<>();

	/**
	 * 事件队列
	 */
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();

	/**
	 * 房间业务Service
	 */
	private RoomWorldService roomWorldService;

	public RoomThread(int loopThreadSize) {
		super("RoomThread-1");
		list = new ArrayList<>(loopThreadSize);

		for (int i = 0; i < loopThreadSize; i++) {
			RoomLoopThread roomLoopThread = new RoomLoopThread();
			roomLoopThread.setRoomControlService(GlobalBeanFactory.getBeanByName(RoomControlService.class));
			roomLoopThread.setRoomLogicService(GlobalBeanFactory.getBeanByName(RoomLogicService.class));
			roomLoopThread.setRoomWorldService(GlobalBeanFactory.getBeanByName(RoomWorldService.class));
			roomLoopThread.setName("RoomLoopThread-" + lastIndex++);
			roomLoopThread.start();
			list.add(roomLoopThread);
		}
	}

	@Override
	public void run() {
		long roomInfoTime = System.currentTimeMillis();
		
		while (true) {
			try {
				IRoomEvent roomEvent = queue.poll(1, TimeUnit.SECONDS);

				if (roomEvent != null) {
					if (roomEvent instanceof DeployRoomEvent) {
						// world发起添加房间
						if (!addRoomInfo(list, roomEvent)) {
							this.roomWorldService.deployRoomInfoFailed(((DeployRoomEvent) roomEvent).getRoomId());
						}
					} else if (roomEvent instanceof DeployRoleInfoEvent) {
						// world发起玩家加入房间
						if (!addRoleInfo(list, roomEvent)) {
							this.roomWorldService.deployRoleInfoFailed(((DeployRoleInfoEvent) roomEvent).getReq().getRoomId(), ((DeployRoleInfoEvent) roomEvent).getReq().getRoleId());
						}
					} else if (roomEvent instanceof RemoveRoomEvent) {
						// world发起移除房间
						if (!removeRoomInfo(roomEvent)) {
							this.roomWorldService.removeRoomInfoFailed(((RemoveRoomEvent) roomEvent).getRoomId());
						}
					} else if (roomEvent instanceof RemoveRoleInfoEvent) {
						// world发起玩家移除房间
						if (!removeRoleInfo(roomEvent)) {
							this.roomWorldService.removeRoleInfoFailed(((RemoveRoleInfoEvent) roomEvent).getReq().getRoomId(), ((RemoveRoleInfoEvent) roomEvent).getReq().getRoleId());
						}
					}
				}
				
				if (System.currentTimeMillis() - roomInfoTime > 5000) {
					// 定时报告房间信息和玩家信息
					int roomCount = 0;
					int roleCount = 0;
					
					for (RoomLoopThread roomLoopThread : list) {
						roomCount += roomLoopThread.getRoomSize();
						roleCount += roomLoopThread.getAllRoles();
					}
					
					this.roomWorldService.reportRoomServerInfo(roomCount, roleCount);
					
					roomInfoTime = System.currentTimeMillis();
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
				if (roomLoopThread.addRoomEvent(roomEvent)) {
					roomMap.put(((DeployRoomEvent) roomEvent).getRoomId(), roomLoopThread);
					return true;
				} else {
					return false;
				}
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
			newRoomLoopThread.setRoomControlService(GlobalBeanFactory.getBeanByName(RoomControlService.class));
			newRoomLoopThread.setRoomLogicService(GlobalBeanFactory.getBeanByName(RoomLogicService.class));
			newRoomLoopThread.setRoomWorldService(GlobalBeanFactory.getBeanByName(RoomWorldService.class));
			newRoomLoopThread.setName("RoomLoopThread-" + lastIndex++);
			newRoomLoopThread.setMap(roomLoopThread.getMap());
			newRoomLoopThread.start();
			
			Set<Entry<Integer, PlayingRoomInfo>> set = newRoomLoopThread.getMap().entrySet();
			
			for (Entry<Integer, PlayingRoomInfo> entry : set) {
				roomMap.put(entry.getKey(), newRoomLoopThread);
			}
			
			list.add(newRoomLoopThread);
		}

		if (minValue == THREAD_MAX_ROOM_COUNT) {
			return false;
		}

		if (minRoomLoopThread.addRoomEvent(roomEvent)) {
			roomMap.put(((DeployRoomEvent) roomEvent).getRoomId(), minRoomLoopThread);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 移除房间
	 * 
	 * @param roomEvent
	 * @return
	 */
	private boolean removeRoomInfo(IRoomEvent roomEvent) {
		RoomLoopThread roomLoopThread = roomMap.get(((RemoveRoomEvent) roomEvent).getRoomId());
		
		if (roomLoopThread == null) {
			return false;
		}
		
		if (roomLoopThread.addRoomEvent(roomEvent)) {
			roomMap.remove(((RemoveRoomEvent) roomEvent).getRoomId());
			return true;
		} else {
			return false;
		}
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
	
	/**
	 * 移除角色
	 * 
	 * @param roomEvent
	 * @return
	 */
	private boolean removeRoleInfo(IRoomEvent roomEvent) {
		RoomLoopThread roomLoopThread = roomMap.get(((RemoveRoleInfoEvent) roomEvent).getReq().getRoomId());
		
		if (roomLoopThread == null) {
			return false;
		}
		
		if (roomLoopThread.addRoomEvent(roomEvent)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 事件添加
	 * 
	 * @param roomEvent
	 * @return
	 */
	public boolean addRoomEvent(IRoomEvent roomEvent) {
		try {
			queue.add(roomEvent);
			return true;
		} catch (Exception e) {
			logger.error("RoomThread : addRoomEvent error", e);
		}

		return false;
	}
	
	/**
	 * 获取房间对应的线程对象
	 * 
	 * @param roomId	房间号
	 * @return	RoomLoopThread
	 */
	public RoomLoopThread getRoomLoopThread(int roomId) {
		return roomMap.get(roomId);
	}

	@Autowired
	public void setRoomWorldService(RoomWorldService roomWorldService) {
		this.roomWorldService = roomWorldService;
	}
}
