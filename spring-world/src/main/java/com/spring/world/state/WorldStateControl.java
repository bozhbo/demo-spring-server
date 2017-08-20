package com.spring.world.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.spring.world.main.enums.WorldServerEnum;

public class WorldStateControl {

	private WorldServerEnum serverState;
	private Map<WorldServerEnum, Observable> map = new HashMap<>();
	
	public void init() {
		WorldServerEnum[] enums = WorldServerEnum.values();
		
		for (WorldServerEnum worldServerEnum : enums) {
			map.put(worldServerEnum, new Observable());
		}
	}
	
	public void addObserver(WorldServerEnum worldServerEnum, Observer observer) {
		map.get(worldServerEnum).addObserver(observer);
	}
	
	public void deleteObserver(WorldServerEnum worldServerEnum, Observer observer) {
		map.get(worldServerEnum).deleteObserver(observer);
	}
	
	public void fireStateEvent(IStateEvent stateEvent) {
		map.get(serverState).notifyObservers(stateEvent);
	}
}
