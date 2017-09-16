package com.spring.room.control.service;


import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomControlService {

	/**
	 * 房间线程主循环
	 * 
	 * @param playingRoomInfo
	 * @return
	 */
	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo);
	
}
