package com.snail.webgame.engine.gate.main;

import com.snail.webgame.engine.gate.config.GlobalServer;

public class DefaultServerState implements IServerState {
	
	public boolean isAllNormal() {
		return GlobalServer.GAME_IS_REGISTER;
	}
}
