package com.spring.logic.message.service;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;

public interface MessageService {

	public Message createMessage(RoomMessageHead head);
	
	public Message createMessage(int msgType, int sceneId, String userState);
	
	public Message createMessage(RoomMessageHead head, IRoomBody body);
	
	public Message createMessage(int msgType, int sceneId, String userState, IRoomBody body);
	
	public boolean sendGateMessage(int gateId, Message message);
	
	public boolean sendGateMessage(int gateId, int msgType, IRoomBody body);
	
	public boolean sendRoomMessage(int roomServerId, Message message);
	
	public boolean sendRoomMessage(int roomServerId, int msgType, IRoomBody body);
	
	public Message createErrorMessage(int errorCode, String addInfo);
	
}
