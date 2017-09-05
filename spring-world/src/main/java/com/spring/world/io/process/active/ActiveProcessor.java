package com.spring.world.io.process.active;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.world.cache.GateServerCache;

@Component
public class ActiveProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(ActiveProcessor.class);

	@Override
	public void processor(Message message) {
		//RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		ActiveReq req = (ActiveReq)message.getiRoomBody();
		
		if (GateServerCache.getIoSession(req.getGateServerName()) == null) {
			GateServerCache.addIoSession(req.getGateServerName(), message.getSession());
			logger.info("active gate server " + req.getGateServerName());
		}
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
