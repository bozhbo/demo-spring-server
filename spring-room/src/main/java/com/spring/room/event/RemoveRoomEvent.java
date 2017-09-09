package com.spring.room.event;

import com.spring.logic.room.event.IRoomEvent;

public class RemoveRoomEvent implements IRoomEvent {

	private int roomId;
	
	public RemoveRoomEvent(int roomId) {
		this.roomId = roomId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	
}
