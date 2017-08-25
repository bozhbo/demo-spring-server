package com.snail.webgame.engine.gate.main;

import com.snail.webgame.engine.gate.config.GlobalServer;

public class DefaultServerState implements IServerState
{
	public boolean isAllNormal()
	{
		// TODO Auto-generated method stub
		return GlobalServer.GAME_IS_REGISTER && GlobalServer.CHAT_IS_REGISTER;
	}
}
