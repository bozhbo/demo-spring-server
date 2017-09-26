package com.spring.world.io.process.handler;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.common.IoSession;

import com.snail.mina.protocol.handler.SessionStateHandler;
import com.snail.mina.protocol.info.IRoomHead;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.logic.server.cache.GateServerCache;
import com.spring.logic.server.cache.RoomManageServerCache;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.world.room.service.RoomManageService;

public class GameServerSessionHandler implements SessionStateHandler {
	
	private RoomManageService roomManageService;
	
	public GameServerSessionHandler(RoomManageService roomManageService) {
		this.roomManageService = roomManageService;
	}

	@Override
	public void sessionClose(IoSession session) {
		Object obj = session.getAttribute("RoomServerId");
		
		if (obj != null) {
			int roomServerId = (Integer)obj;
			RoomServerInfo roomServerInfo = RoomServerCache.getRoomServerInfo(roomServerId);
			
			if (roomServerInfo != null) {
				roomServerInfo.setSession(null);
			}
			
			roomManageService.roomServerClose(roomServerId);
		}
	}

	@Override
	public void sessionOpened(IoSession session) {
		// TODO 设置心跳时间
		
	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getRegisterReserve() {
		// 服务器忽略
		return null;
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
		if (serverReserve != null) {
			RoomServerInfo roomServerInfo = LogicUtil.fromJson(serverReserve, RoomServerInfo.class);
			RoomServerInfo oldRroomServerInfo = RoomServerCache.getRoomServerInfo(roomServerInfo.getRoomServerId());
			
			if (oldRroomServerInfo == null) {
				roomServerInfo.setSession(session);
				sendRoomServer2Gate(roomServerInfo);
				RoomServerCache.addRoomServerInfo(roomServerInfo);
			} else {
				if (!roomServerInfo.getIp().equals(oldRroomServerInfo.getIp()) || roomServerInfo.getPort() != oldRroomServerInfo.getPort()) {
					oldRroomServerInfo.setIp(roomServerInfo.getIp());
					oldRroomServerInfo.setPort(roomServerInfo.getPort());
					sendRoomServer2Gate(oldRroomServerInfo);
				}
				
				oldRroomServerInfo.setSession(session);
			}
			
			session.setAttribute("RoomServerId", roomServerInfo.getRoomServerId());
		}
	}
	
	private void sendRoomServer2Gate(RoomServerInfo roomServerInfo) {
		Set<Entry<String, IoSession>> set = GateServerCache.getSet();
		
		for (Entry<String, IoSession> entry : set) {
			Message message = new Message();
			RoomMessageHead head = new RoomMessageHead();
			
			head.setMsgType(0xfff9);
			head.setUserState(roomServerInfo.getIp() + "," + roomServerInfo.getPort() + "," + roomServerInfo.getServerName());
			
			message.setiRoomHead(head);
			
			entry.getValue().write(message);
		}
	}

	@Override
	public String execCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

}
