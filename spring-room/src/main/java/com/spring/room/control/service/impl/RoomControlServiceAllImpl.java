package com.spring.room.control.service.impl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.control.service.RoomLogicService;

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

	@Autowired
	public void setRoomLogicService(RoomLogicService roomLogicService) {
		this.roomLogicService = roomLogicService;
	}
}
