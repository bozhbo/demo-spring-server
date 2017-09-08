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

import com.spring.logic.room.event.IRoomEvent;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.event.DeployRoleInfoEvent;
import com.spring.room.event.DeployRoomEvent;

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
	
	@Override
	public void run() {
		long sleepTime = SLEEP_TIME;
		long lastTime = System.currentTimeMillis();
		
		while (!stop) {
			try {
				IRoomEvent roomEvent = queue.poll(sleepTime, TimeUnit.MILLISECONDS);
				
				if (roomEvent != null) {
					if (roomEvent instanceof DeployRoomEvent) {
						map.put(((DeployRoomEvent)roomEvent).getRoomInfo().getRoomId(), roomControlService.deployRoomInfo(((DeployRoomEvent)roomEvent).getRoomInfo()));
					} else if (roomEvent instanceof DeployRoleInfoEvent) {
						
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
	public void setRoomControlService(RoomControlService roomControlService) {
		this.roomControlService = roomControlService;
	}
}
