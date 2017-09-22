package com.spring.logic.message.service;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;

public interface MessageService {

	public RoomMessageHead createMessageHead(int roleId, int gateId, int msgType, int sceneId, String userState);
	
	public Message createMessage(RoomMessageHead head);
	
	public Message createMessage(int roleId, int msgType, int sceneId, String userState);
	
	public Message createMessage(RoomMessageHead head, IRoomBody body);
	
	public Message createMessage(int roleId, int msgType, int sceneId, String userState, IRoomBody body);
	
	public boolean sendGateMessage(int gateId, RoomMessageHead head);
	
	public boolean sendGateMessage(int gateId, Message message);
	
	public boolean sendGateMessage(int gateId, int msgType, IRoomBody body);
	
	public boolean sendRoomMessage(int roomServerId, Message message);
	
	public boolean sendRoomMessage(int roomServerId, int msgType, IRoomBody body);
	
	public boolean sendWorldMessage(Message message);
	
	public Message createErrorMessage(int roleId, int errorCode, String addInfo);
	
	public Message createErrorMessage(int errorCode, String addInfo, RoomMessageHead head);
	
}
