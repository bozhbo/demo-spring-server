package com.spring.logic.room.enums;

public enum RoomPlayingEnum {

	ROOM_STATE_INIT(1), 
	ROOM_STATE_READY(2), 
	ROOM_STATE_SEND_CARD(3), 
	ROOM_STATE_PLAYING(4), 
	ROOM_TYPE_END(5);

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
			return ROOM_STATE_SEND_CARD;
		case 4:
			return ROOM_STATE_PLAYING;
		case 5:
			return ROOM_TYPE_END;
		default:
			break;
		}
		
		return ROOM_STATE_INIT;
	}
}
