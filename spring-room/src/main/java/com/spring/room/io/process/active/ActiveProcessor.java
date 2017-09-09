package com.spring.room.io.process.active;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.logic.server.cache.GateServerCache;

@Component
public class ActiveProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(ActiveProcessor.class);

	@Override
	public void processor(Message message) {
		//RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		ActiveReq req = (ActiveReq)message.getiRoomBody();
		
		IoSession session =  GateServerCache.getIoSession(req.getGateServerName());
		
		if (session == null) {
			GateServerCache.addIoSession(req.getGateServerName(), message.getSession());
		} else if (session != message.getSession()) {
			session.close();
			GateServerCache.addIoSession(req.getGateServerName(), message.getSession());
		}
		
		if (req.getFlag() == 1) {
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
