package com.spring.logic.gf.enums;

public enum PokerTypeEnum {

	POKER_TYPE_BOOM(1),
	POKER_TYPE_GREAT_GF(2),
	POKER_TYPE_GF(3),
	POKER_TYPE_STRAIGHT(4),
	POKER_TYPE_PAIRS(5),
	POKER_TYPE_ALONE(6),
	POKER_TYPE_MIN(7);
	
	private int value;
	
	private PokerTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
