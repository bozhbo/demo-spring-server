package com.spring.world.state;

import java.util.Observable;
import java.util.Observer;

public class StateObserver implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof IStateEvent) {
			((IStateEvent)arg).eventHandle();
		}
	}
}
