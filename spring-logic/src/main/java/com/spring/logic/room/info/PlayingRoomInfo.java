package com.spring.logic.room.info;

/**
 * 用于房间服务器运行对象
 * 
 * @author zhoubo
 *
 */
public class PlayingRoomInfo {

	private RoomInfo roomInfo;
	
	public PlayingRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}

	public RoomInfo getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}
	
	
}
