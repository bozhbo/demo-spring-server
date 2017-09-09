package com.spring.room.event;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.event.IRoomEvent;

public class DeployRoomEvent implements IRoomEvent {

	private int roomId;
	private RoomTypeEnum roomType;
	
	public DeployRoomEvent(int roomId, RoomTypeEnum roomType) {
		this.roomId = roomId;
		this.roomType = roomType;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public RoomTypeEnum getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomTypeEnum roomType) {
		this.roomType = roomType;
	}

	
}
