package com.spring.room.event;

import com.spring.logic.room.event.IRoomEvent;
import com.spring.logic.room.info.RoomInfo;

public class RemoveRoomEvent implements IRoomEvent {

	private RoomInfo roomInfo;
	
	public RemoveRoomEvent(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}

	public RoomInfo getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}
	
	
}
