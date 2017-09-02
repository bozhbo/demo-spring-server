package com.spring.world.io.process.active;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;

public class ActiveProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(ActiveProcessor.class);

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		ActiveReq req = (ActiveReq)message.getiRoomBody();
		
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return ActiveReq.class;
	}

	@Override
	public int getMsgType() {
		return 0xfffE;
	}

}
