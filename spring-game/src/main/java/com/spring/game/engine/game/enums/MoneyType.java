package com.snail.webgame.engine.game.enums;

public enum MoneyType {

	GOLD(1), 
	POINT(2);
	
	private int type;
	
	private MoneyType(int type) {
		this.type = type;
	}
}
