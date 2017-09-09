package com.spring.logic.business.service;

import com.spring.logic.message.request.room.RoomInitResp;
import com.spring.logic.message.request.room.RoomJoinResp;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomBusinessService {

	public RoomInitResp getRoomInitResp(PlayingRoomInfo playingRoomInfo);
	
	public RoomJoinResp getRoomJoinResp(RoomRoleInfo roomRoleInfo);
}
