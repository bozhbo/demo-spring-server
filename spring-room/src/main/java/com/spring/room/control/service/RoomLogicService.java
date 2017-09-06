package com.spring.room.control.service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomLogicService {

	/**
	 * 发送加入房间消息给房间所有玩家
	 * 
	 * @param playingRoomInfo
	 * @param roleInfo
	 */
	public void sendJoinRoomMsg(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo);
}
