package com.snail.webgame.engine.net.msg.impl;

import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.game.base.init.GameConfig;

public class BaseMessage extends Message {
	
	@SuppressWarnings("unchecked")
	public <T> T getiMessageHead(Class<T> clazz) {
		return (T)getiRoomHead();
	}
	@SuppressWarnings("unchecked")
	public <T> T  getiMessageBody(Class<T> clazz) {
		return (T)getiRoomBody();
	}
	
	@Override
	public void run() {
		if (getiRoomHead().getMsgType() > 0) {
			GameConfig.getControlProcessor().processor(this);
		}
	}
}
