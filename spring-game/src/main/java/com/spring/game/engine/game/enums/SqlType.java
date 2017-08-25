package com.snail.webgame.engine.game.enums;

public enum SqlType {
	
	UPDATE(1), UPDATE_BATCH(2), INSERT_FORKEY(3), INSERT_FORKEY_BATCH(4);

	private final int type;
	
	private SqlType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
}
