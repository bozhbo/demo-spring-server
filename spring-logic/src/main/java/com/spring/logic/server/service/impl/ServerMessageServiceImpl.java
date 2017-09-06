package com.spring.logic.server.service.impl;

import org.apache.mina.common.IoSession;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.logic.server.cache.GateServerCache;
import com.spring.logic.server.service.ServerMessageService;

@Service
public class ServerMessageServiceImpl implements ServerMessageService {

	@Override
	public boolean sendMessageByGateId(int gateId, Message message) {
		IoSession session = GateServerCache.getIoSession(gateId);
		
		if (session == null || !session.isConnected()) {
			return false;
		}
		
		session.write(message);
		return true;
	}

}
