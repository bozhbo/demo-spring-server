package com.spring.room.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.role.info.RoomRoleInfo;
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
 * 房间循环线程
 * 
 * 处理管理的房间事件
 * 
 * @author Administrator
 *
 */
public class RoomLoopThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(RoomLoopThread.class);
	
	/**
	 * 被管理的所有房间集合
	 */
	private Map<Integer, PlayingRoomInfo> map = new HashMap<>();
	
	/**
	 * 房间事件队列
	 */
	private LinkedBlockingQueue<IRoomEvent> queue = new LinkedBlockingQueue<IRoomEvent>();
	
	/**
	 * 停止标志，停止后RoomThread负责重启
	 */
	private volatile boolean stop = false;
	
	/**
	 * 队列等待超时时间
	 */
	private long SLEEP_TIME = 50;
	
	/**
	 * 所有房间人数总和
	 */
	private volatile int allRoles = 0;
	
	/**
	 * 房间业务Service
	 */
	private RoomControlService roomControlService;
	
	private RoomLogicService roomLogicService;
	
	private RoomWorldService roomWorldService;
	
	@Override
	public void run() {
		long sleepTime = SLEEP_TIME;
		long lastTime = System.currentTimeMillis();
		
		while (!stop) {
			try {
				IRoomEvent roomEvent = queue.poll(sleepTime, TimeUnit.MILLISECONDS);
				
				if (roomEvent != null) {
					if (roomEvent instanceof DeployRoomEvent) {
						PlayingRoomInfo playingRoomInfo = roomLogicService.createPlayingRoomInfo(((DeployRoomEvent)roomEvent).getRoomId(), ((DeployRoomEvent)roomEvent).getRoomType());
						map.put(playingRoomInfo.getRoomId(), playingRoomInfo);
					} else if (roomEvent instanceof RemoveRoomEvent) {
						PlayingRoomInfo playingRoomInfo = map.get(((RemoveRoomEvent)roomEvent).getRoomId());
					
						if (playingRoomInfo != null) {
							playingRoomInfo.getList().forEach((roleInfo) -> roomLogicService.removeRole(playingRoomInfo, roleInfo.getRoleId()));
						}
					} else if (roomEvent instanceof DeployRoleInfoEvent) {
						PlayingRoomInfo playingRoomInfo = map.get(((DeployRoleInfoEvent)roomEvent).getReq().getRoomId());
						
						if (playingRoomInfo != null) {
							RoomRoleInfo roomRoleInfo = roomLogicService.createRoomRoleInfo(playingRoomInfo, ((DeployRoleInfoEvent)roomEvent).getReq());
							roomLogicService.addRole(playingRoomInfo, roomRoleInfo);
						} else {
							roomWorldService.deployRoleInfoFailed(((DeployRoleInfoEvent)roomEvent).getReq().getRoomId(), ((DeployRoleInfoEvent)roomEvent).getReq().getRoleId());
						}
					} else if (roomEvent instanceof RemoveRoleInfoEvent) {
						PlayingRoomInfo playingRoomInfo = map.get(((RemoveRoleInfoEvent)roomEvent).getReq().getRoomId());
					
						if (playingRoomInfo != null) {
							roomLogicService.removeRole(playingRoomInfo, ((RemoveRoleInfoEvent)roomEvent).getReq().getRoleId());
						} else {
							roomWorldService.removeRoleInfoFailed(((RemoveRoleInfoEvent)roomEvent).getReq().getRoomId(), ((RemoveRoleInfoEvent)roomEvent).getReq().getRoleId());
						}
					}
					
					long start = System.currentTimeMillis();
					
					if (start - lastTime > SLEEP_TIME) {
						sleepTime = loop(start);
					}
				}
				
				sleepTime = loop(System.currentTimeMillis());
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
		stop = true;
	}
	
	private long loop(long start) {
		Set<Entry<Integer, PlayingRoomInfo>> set = map.entrySet();
		
		for (Entry<Integer, PlayingRoomInfo> entry : set) {
			this.roomControlService.loopRoomInfo(entry.getValue());
		}
		
		long use = (System.currentTimeMillis() - start);
		
		if (SLEEP_TIME - use < 0) {
			logger.warn("loop time is too long " + (use - SLEEP_TIME));
			return 10l;
		} else {
			return SLEEP_TIME - use;
		}
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
		return map.size();
	}
	
	public boolean isStop() {
		return stop;
	}
	
	public void cancel() {
		stop = true;
	}
	
	public int getAllRoles() {
		return allRoles;
	}
	
	public void setMap(Map<Integer, PlayingRoomInfo> map) {
		this.map = map;
	}

	public Map<Integer, PlayingRoomInfo> getMap() {
		return map;
	}
	
	@Autowired
	public void setRoomLogicService(RoomLogicService roomLogicService) {
		this.roomLogicService = roomLogicService;
	}

	@Autowired
	public void setRoomControlService(RoomControlService roomControlService) {
		this.roomControlService = roomControlService;
	}

	@Autowired
	public void setRoomWorldService(RoomWorldService roomWorldService) {
		this.roomWorldService = roomWorldService;
	}
	
	
}
