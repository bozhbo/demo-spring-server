package com.spring.logic.server.service;

import com.snail.mina.protocol.info.Message;

public interface ServerMessageService {

	public boolean sendMessageByGateId(int gateId, Message message);
}
