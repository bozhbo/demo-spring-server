package com.spring.world.state.observer;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.world.state.WorldStateControl;
import com.spring.world.state.enums.WorldServerEnum;
import com.spring.world.state.enums.WorldServerEventEnum;
import com.spring.world.state.event.IStateEvent;

public class UnknowObserver implements Observer {
	
	private WorldStateControl worldStateControl;
	
	@Autowired
	public void setWorldStateControl(WorldStateControl worldStateControl) {
		this.worldStateControl = worldStateControl;
	}

	public UnknowObserver() {
		worldStateControl.addObserver(WorldServerEnum.WORLD_STATE_UNKNOW, this);
	}

	@Override
	public void update(Observable o, Object arg) {
		IStateEvent stateEvent = (IStateEvent)arg;
		
		if (stateEvent.getWorldServerEventEnum() == WorldServerEventEnum.WORLD_STATE_EVENT_LOAD_DATA) {
			// 加载数据成功
			stateEvent.eventHandler();
		} else {
			
		}
	}
}
