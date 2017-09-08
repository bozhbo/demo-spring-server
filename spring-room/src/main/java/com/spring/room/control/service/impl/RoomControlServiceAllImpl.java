package com.spring.room.control.service.impl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.control.service.RoomLogicService;
import com.spring.room.thread.RoomThread;

/**
 * room-world合并实现
 * 
 * @author zhoubo
 *
 */
@Service
public class RoomControlServiceAllImpl implements RoomControlService {
	
	private static final Log logger = LogFactory.getLog(RoomControlServiceAllImpl.class);
	
	private RoomLogicService roomLogicService;

	@Override
	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PlayingRoomInfo deployRoomInfo(RoomInfo roomInfo) {
		PlayingRoomInfo playingRoomInfo = new PlayingRoomInfo(roomInfo.getRoomId(), roomInfo.getRoomType());
		
		return playingRoomInfo;
	}

	@Override
	public int deployRoleInfo(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo) {
		if (playingRoomInfo.getList().size() >= RoomConfig.ROOM_MAX_ROLES) {
			logger.warn("deploy playing role failed for room is full");
			return 0;
		}
		
		playingRoomInfo.getList().add(roleInfo);
		
		this.roomLogicService.sendJoinRoomMsg(playingRoomInfo, roleInfo);
		
		return 1;
	}
	
	@Override
	public PlayingRoomInfo removeRoomInfo(PlayingRoomInfo playingRoomInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int removeRoleInfo(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Autowired
	public void setRoomLogicService(RoomLogicService roomLogicService) {
		this.roomLogicService = roomLogicService;
	}
}
