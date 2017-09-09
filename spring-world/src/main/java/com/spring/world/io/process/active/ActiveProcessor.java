package com.spring.world.io.process.active;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.logic.server.cache.GateServerCache;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;

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
		
		if (req.getFlag() == 0) {
			Set<Entry<Integer, RoomServerInfo>> set = RoomServerCache.getSet();
			
			for (Entry<Integer, RoomServerInfo> entry2 : set) {
				RoomServerInfo roomServerInfo = entry2.getValue();
				Message roomMessage = new Message();
				RoomMessageHead head = new RoomMessageHead();
				
				head.setMsgType(0xfff9);
				head.setUserState(roomServerInfo.getIp() + "," + roomServerInfo.getPort() + "," + roomServerInfo.getServerName());
				
				roomMessage.setiRoomHead(head);
				
				message.getSession().write(roomMessage);
			}
			
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
