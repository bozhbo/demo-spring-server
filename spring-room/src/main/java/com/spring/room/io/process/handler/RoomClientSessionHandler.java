package com.spring.room.io.process.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.common.IoSession;

import com.snail.mina.protocol.handler.SessionStateHandler;
import com.snail.mina.protocol.info.IRoomHead;
import com.spring.common.ServerName;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.room.config.RoomServerConfig;

public class RoomClientSessionHandler implements SessionStateHandler {

	@Override
	public void sessionClose(IoSession session) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionOpened(IoSession session) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		return true;
	}

	@Override
	public String getRegisterReserve() {
		RoomServerInfo roomServerInfo = new RoomServerInfo();
		roomServerInfo.setIp(RoomServerConfig.ROOM_SERVER_IP);
		roomServerInfo.setPort(RoomServerConfig.ROOM_SERVER_PORT);
		roomServerInfo.setRoleCount(0);
		roomServerInfo.setRoomCount(0);
		roomServerInfo.setServerName(ServerName.ROOM_SERVER_NAME + "-" + RoomServerConfig.ROOM_SERVER_ID);
		roomServerInfo.setSession(null);
		
		return LogicUtil.tojson(roomServerInfo);
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
		// 客户的忽略
	}

	@Override
	public String execCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

}
