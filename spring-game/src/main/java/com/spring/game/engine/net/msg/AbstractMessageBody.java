package com.snail.webgame.engine.net.msg;

import com.snail.webgame.engine.component.room.protocol.info.impl.AbstractRoomBody;

public abstract class AbstractMessageBody extends AbstractRoomBody {
	
	public abstract IStringHandle getStringHandle();
}
