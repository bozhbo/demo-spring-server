package com.spring.world.room.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.role.service.RoleRoomService;
import com.spring.logic.room.service.RoomService;
import com.spring.logic.server.cache.RoomManageServerCache;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.world.room.service.RoomManageService;

@Service
public class RoomManageServiceImpl implements RoomManageService {
	
	private static final Log logger = LogFactory.getLog(RoomManageServiceImpl.class);
	
	private RoleRoomService roleRoomService;
	
	private RoomService roomService;

	@Override
	public void roomServerRegister(int roomServerId) {
		RoomServerInfo roomServerInfo = new RoomServerInfo();
		roomServerInfo.setRoomServerId(roomServerId);
		
		RoomServerCache.addRoomServerInfo(roomServerInfo);
	}

	@Override
	public void roomServerClose(int roomServerId) {
		RoomServerCache.removeRoomServerInfo(roomServerId);
		
		Set<Integer> set = RoomManageServerCache.getAllRooms(roomServerId);
		
		for (Integer roomId : set) {
			List<Integer> list = roomService.getAllRoles(roomId);
			
			if (list != null && list.size() > 0) {
				for (Integer roleId : list) {
					RoleInfo roleInfo = RoleCache.getRoleInfo(roleId);
					
					if (roleInfo != null) {
						synchronized (roleInfo) {
							roleInfo.setRoomId(0);
							roleRoomService.refreshSceneInfo(roleInfo);
							
							logger.info("role leave room for room close " + roleId);
						}
					}
					
					roomService.leaveRoom(roomId, roleId);
				}
			}
		}
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
	public void roomServerShutDown(int roomServerId) {
		// TODO Auto-generated method stub
		
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

	@Autowired
	public void setRoleRoomService(RoleRoomService roleRoomService) {
		this.roleRoomService = roleRoomService;
	}

	
}
