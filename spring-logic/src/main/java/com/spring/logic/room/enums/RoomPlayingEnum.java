package com.spring.logic.room.enums;

public enum RoomPlayingEnum {

	ROOM_STATE_INIT(1), 
	ROOM_STATE_READY(2), 
	ROOM_STATE_PLAYING(3), 
	ROOM_TYPE_END(4);

	private int value;

	private RoomPlayingEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static RoomPlayingEnum transfer(int value) {
		switch (value) {
		case 1:
			return ROOM_STATE_INIT;
		case 2:
			return ROOM_STATE_READY;
		case 3:
			return ROOM_STATE_PLAYING;
		case 4:
			return ROOM_TYPE_END;
		default:
			break;
		}
		
		return ROOM_STATE_INIT;
	}
}
