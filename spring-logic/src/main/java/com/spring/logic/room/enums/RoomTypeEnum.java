package com.spring.logic.room.enums;

public enum RoomTypeEnum {

	ROOM_TYPE_NEW(1),
	ROOM_TYPE_LEVEL1(2),
	ROOM_TYPE_LEVEL2(3),
	ROOM_TYPE_LEVEL3(4),
	ROOM_TYPE_LEVEL4(5);
	
	private int value;
	
	private RoomTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
