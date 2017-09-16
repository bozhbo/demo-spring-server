package com.spring.world.room.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.world.room.service.RoomManageService;

@Service
public class RoomManageServiceImpl implements RoomManageService {
	
	private static final Log logger = LogFactory.getLog(RoomManageServiceImpl.class);

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
	public void roomServerShutDown(int roomServerId) {
		// TODO Auto-generated method stub
		
	}

}
