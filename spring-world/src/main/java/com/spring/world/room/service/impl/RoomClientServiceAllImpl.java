package com.spring.world.room.service.impl;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.room.control.service.RoomControlService;
import com.spring.world.cache.RoomServerCache;
import com.spring.world.config.WorldConfig;
import com.spring.world.event.ChooseRoomEvent;
import com.spring.world.info.RoomServerInfo;
import com.spring.world.room.service.RoomClientService;
import com.spring.world.thread.WorldRoomChooseThread;

/**
 * 单一进程实现
 * 
 * @author zhoubo
 *
 */
public class RoomClientServiceAllImpl implements RoomClientService {
	
	private static final Log logger = LogFactory.getLog(RoomClientServiceAllImpl.class);
	
	private RoomControlService roomControlService;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int deployRoomInfo(RoomInfo roomInfo) {
		Set<Entry<Integer, RoomServerInfo>> set = RoomServerCache.getSet();
		
		for (Entry<Integer, RoomServerInfo> entry : set) {
			if (entry.getValue().getRoleCount() < 100) {
				sendRoomInfo(roomInfo, entry.getValue());
				return entry.getValue().getRoomServerId();
			}
		}
		
		for (Entry<Integer, RoomServerInfo> entry : set) {
			if (entry.getValue().getRoomCount() < 20) {
				sendRoomInfo(roomInfo, entry.getValue());
				return entry.getValue().getRoomServerId();
			}
		}
		
		logger.warn("deployRoomInfo failed");
		
		return 0;
	}
	
	@Override
	public int sendRoomInfo(RoomInfo roomInfo, RoomServerInfo roomServerInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo) {
		this.roomControlService.deployRoleInfo(roomInfo, roleInfo);
		return 0;
	}

	@Override
	public void roomServerRegister(int roomServerId) {
		RoomServerInfo roomServerInfo = new RoomServerInfo();
		roomServerInfo.setRoomServerId(roomServerId);
		
		RoomServerCache.addRoomServerInfo(roomServerInfo);
	}

	@Override
	public void roomServerClose(int roomServerId) {
		RoomServerCache.removeRoomServerInfo(roomServerId);
	}

	@Override
	public void roomInfo(String info) {
		RoomServerInfo curRoomServerInfo = LogicUtil.fromJson(info, RoomServerInfo.class);
		
		if (curRoomServerInfo != null) {
			RoomServerInfo roomServerInfo = RoomServerCache.getRoomServerInfo(curRoomServerInfo.getRoomServerId());
			
			if (roomServerInfo != null) {
				roomServerInfo.setRoleCount(curRoomServerInfo.getRoleCount());
				roomServerInfo.setRoomCount(curRoomServerInfo.getRoomCount());
			} else {
				logger.error("roomServerInfo is null " + curRoomServerInfo.getRoomServerId());
			}
		} else {
			logger.error("room info is error " + info);
		}
	}

	@Override
	public void autoJoin(RoleInfo roleInfo) {
		WorldRoomChooseThread worldRoomChooseThread = WorldConfig.getWorldRoomChooseThread(RoomTypeEnum.ROOM_TYPE_NEW);
		ChooseRoomEvent chooseRoomEvent = new ChooseRoomEvent();
		chooseRoomEvent.setRoleInfo(roleInfo);
		
		worldRoomChooseThread.addRoomEvent(chooseRoomEvent);
	}

	@Autowired
	public void setRoomControlService(RoomControlService roomControlService) {
		this.roomControlService = roomControlService;
	}
	
	
}
