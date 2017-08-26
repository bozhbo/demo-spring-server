package com.spring.world.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.spring.world.state.enums.WorldServerEnum;
import com.spring.world.state.event.IStateEvent;

public class WorldStateControl {

	private WorldServerEnum serverState;
	private Map<WorldServerEnum, Observable> map = new HashMap<>();

	public WorldStateControl() {
		init();
	}

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

	public void fireStateChange(WorldServerEnum worldServerEnum) {
		switch (worldServerEnum) {
		case WORLD_STATE_UNKNOW:
			changeStateToUnknow(worldServerEnum);
			break;
		case WORLD_STATE_OPNEED:
			changeStateToOpen(worldServerEnum);
			break;
		case WORLD_STATE_CLOSED:
			changeStateToOpen(worldServerEnum);
			break;

		default:
			break;
		}
	}

	private boolean changeStateToUnknow(WorldServerEnum worldServerEnum) {
		serverState = worldServerEnum;

		return true;
	}

	private boolean changeStateToOpen(WorldServerEnum worldServerEnum) {
		serverState = worldServerEnum;

		return true;
	}

	private boolean changeStateToClosed(WorldServerEnum worldServerEnum) {
		serverState = worldServerEnum;

		return true;
	}
}
