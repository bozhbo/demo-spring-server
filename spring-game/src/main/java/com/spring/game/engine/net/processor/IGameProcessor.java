package com.snail.webgame.engine.net.processor;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;

public interface IGameProcessor {
	
	public void setMsgType(int msgType);
	
	public int getMsgType();
	
	public void setReq(Class<IRoomBody> req);
	
	public Class<IRoomBody> getReq();

}
