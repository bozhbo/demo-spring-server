package com.spring.world.room.service.impl;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.RoomService;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.event.DeployRoleInfoEvent;
import com.spring.room.event.DeployRoomEvent;
import com.spring.room.event.RemoveRoleInfoEvent;
import com.spring.room.event.RemoveRoomEvent;
import com.spring.world.room.service.RoomClientService;

/**
 * 单一进程实现
 * 
 * @author zhoubo
 *
 */
@Service
public class RoomClientServiceAllImpl implements RoomClientService {
	
	private static final Log logger = LogFactory.getLog(RoomClientServiceAllImpl.class);
	
	private RoomService roomService;
	
	private RoomControlService roomControlService;
	
	@Override
	public void init() {
		RoomServerInfo roomServerInfo = new RoomServerInfo();
		roomServerInfo.setRoleCount(0);
		roomServerInfo.setRoomCount(0);
		roomServerInfo.setRoomServerId(1);
		
		RoomServerCache.addRoomServerInfo(roomServerInfo);
	}

	@Override
	public int deployRoomInfo(RoomInfo roomInfo) {
		if (roomInfo == null) {
			return 0;
		}
		
		synchronized (roomInfo) {
			if (roomInfo.getCurRoomServerId() > 0) {
				return 1;
			}
			
			Set<Entry<Integer, RoomServerInfo>> set = RoomServerCache.getSet();
			RoomServerInfo roomServerInfo = null;
			int minRoleCount = Integer.MAX_VALUE;
			
			for (Entry<Integer, RoomServerInfo> entry : set) {
				if (entry.getValue().getRoleCount() < 100) {
					RoomServerConfig.getRoomThread().addRoomEvent(new DeployRoomEvent(roomInfo));
					roomInfo.setCurRoomServerId(entry.getValue().getRoomServerId());
					return entry.getValue().getRoomServerId();
				} else {
					if (minRoleCount > entry.getValue().getRoleCount()) {
						roomServerInfo = entry.getValue();
						minRoleCount = entry.getValue().getRoleCount();
					}
				}
			}
			
			if (roomServerInfo != null) {
				RoomServerConfig.getRoomThread().addRoomEvent(new DeployRoomEvent(roomInfo));
				roomInfo.setCurRoomServerId(roomServerInfo.getRoomServerId());
				return roomServerInfo.getRoomServerId();
			}
		}
		
		logger.warn("deployRoomInfo failed");
		
		return 0;
	}
	
	@Override
	public int removeRoomInfo(RoomInfo roomInfo) {
		RoomServerConfig.getRoomThread().addRoomEvent(new RemoveRoomEvent(roomInfo));
		return 1;
	}
	
	@Override
	public int deployRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo) {
		RoomServerConfig.getRoomThread().addRoomEvent(new DeployRoleInfoEvent(roomInfo, roleInfo));
		return 1;
	}
	
	@Override
	public int removeRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo) {
		RoomServerConfig.getRoomThread().addRoomEvent(new RemoveRoleInfoEvent(roomInfo, roleInfo));
		return 1;
	}

	@Override
	public int deployRoomInfoSuccessed(RoomInfo roomInfo) {
		return 1;
	}

	@Override
	public int deployRoomInfoFailed(RoomInfo roomInfo) {
		RoomInfo curRoomInfo = roomService.queryRoom(roomInfo.getRoomId());
		
		if (curRoomInfo != null) {
			curRoomInfo.setCurRoomServerId(0);
		}
		
		return 0;
	}

	@Override
	public int removeRoomInfoSuccessed(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoomInfoFailed(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void autoJoin(RoleInfo roleInfo) {
		int result = 0;
		
		if (roleInfo.getRoomId() > 0) {
			// TODO send error
			return;
		}
		
		RoomInfo roomInfo = this.roomService.randomJoinRoom(RoomTypeEnum.ROOM_TYPE_NEW, roleInfo);
		
		if (roomInfo == null) {
			logger.warn("role join room failed for no room");
			// TODO send error
			return;
		}
		
		result = deployRoomInfo(roomInfo);
		
		if (result != 1) {
			logger.warn("role join room failed for no room");
			// TODO send error
			return;
		}
		
		deployRoleInfo(roomInfo, roleInfo);
	}

	@Override
	public void leaveRoom(RoleInfo roleInfo) {
		if (roleInfo.getRoomId() > 0) {
			RoomInfo roomInfo = this.roomService.queryRoom(roleInfo.getRoomId());
			
			// 离开RoomServer
			removeRoleInfo(roomInfo, roleInfo);
			
			// 离开WorldServer
			this.roomService.leaveRoom(roleInfo);
			
			roleInfo.setRoomId(0);
		}
		
		// TODO 刷新大厅数据
	}
	
	@Autowired
	public void setRoomControlService(RoomControlService roomControlService) {
		this.roomControlService = roomControlService;
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}
}
