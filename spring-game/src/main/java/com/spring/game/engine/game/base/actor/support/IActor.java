package com.snail.webgame.engine.game.base.actor.support;

public interface IActor {

	public Integer getId();
	
	public boolean lock();
	
	public void unlock();
}
