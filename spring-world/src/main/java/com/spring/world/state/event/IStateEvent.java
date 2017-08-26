package com.spring.world.state.event;

import com.spring.world.state.enums.WorldServerEventEnum;

public interface IStateEvent {

	public WorldServerEventEnum getWorldServerEventEnum();
	
	public void eventHandler();
}
