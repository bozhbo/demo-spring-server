package com.spring.world.state;

import com.spring.world.main.enums.WorldServerEventEnum;

public interface IStateEvent {

	public WorldServerEventEnum getWorldServerEventEnum();
	
	public void setWorldServerEventEnum(WorldServerEventEnum worldServerEventEnum);
	
	public void eventHandle();
}
