package com.spring.world.state.event.impl;

import com.spring.world.bean.GlobalBeanFactory;
import com.spring.world.state.WorldStateControl;
import com.spring.world.state.enums.WorldServerEnum;
import com.spring.world.state.enums.WorldServerEventEnum;
import com.spring.world.state.event.IStateEvent;

public class LoadDataEvent implements IStateEvent {

	@Override
	public WorldServerEventEnum getWorldServerEventEnum() {
		return WorldServerEventEnum.WORLD_STATE_EVENT_LOAD_DATA;
	}

	@Override
	public void eventHandler() {
		WorldStateControl worldStateControl = GlobalBeanFactory.getBeanByName("WorldStateControl", WorldStateControl.class);
		worldStateControl.fireStateChange(WorldServerEnum.WORLD_STATE_OPNEED);
	}
}
