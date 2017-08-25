package com.snail.webgame.engine.gate.main;

public interface IServerState
{
	/**
	 * 心跳维护时间间隔判断依据（根据与接入服务器连接的其它服务器的状态）
	 * @return
	 */
	public boolean isAllNormal();
}
