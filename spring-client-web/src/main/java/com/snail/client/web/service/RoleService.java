package com.snail.client.web.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.snail.client.web.robot.RobotRoleInfo;
import com.snail.mina.protocol.client.RoomClient;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.common.base.CommonResp;
import com.spring.logic.message.request.world.init.InitResp;
import com.spring.logic.message.request.world.init.InitSceneResp;
import com.spring.logic.message.request.world.login.LoginReq;
import com.spring.logic.message.request.world.login.LoginResp;
import com.spring.logic.util.LogicUtil;
import com.spring.logic.util.LogicValue;

public class RoleService {
	
	public Map<Integer, String> roleLoginMap = new ConcurrentHashMap<>();
	public Map<Integer, RobotRoleInfo> roleIdMap = new ConcurrentHashMap<>();
	public AtomicInteger errorCode = new AtomicInteger(1);

	public void login(String userName, String password, String serverName) {
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_LOGIN_SEND);
		
		LoginReq req = new LoginReq();
		req.setAccount(userName);
		req.setClientType(1);
		req.setIP(12);
		req.setMac(UUID.randomUUID().toString());
		req.setMd5Pass(UUID.randomUUID().toString());
		req.setPackageName("0.0.1");
		req.setValidate("");
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(req);
		
		RoomClient.sendMessage(serverName, message);
	}
	
	public void loginEnd(LoginResp resp) {
		if (resp.getResult() != 1) {
			errorCode.compareAndSet(1, resp.getResult());
		} else {
			roleLoginMap.put(resp.getRoleId(), resp.getAccount());
		}
	}
	
	public void init(String serverName) {
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_INIT_SEND);
		
		Message message = new Message();
		message.setiRoomHead(head);
		
		RoomClient.sendMessage(serverName, message);
	}
	
	public void initEnd(InitResp resp) {
		RobotRoleInfo roleInfo = new RobotRoleInfo();
		roleInfo.setGold(resp.getGold());
		roleInfo.setRoleId(resp.getRoleId());
		roleInfo.setRoleName(resp.getRoleName());
		roleInfo.setRoomId(resp.getRoomId());
		roleInfo.setVipLevel(resp.getVipLevel());
		
		roleIdMap.put(resp.getRoleId(), roleInfo);
	}
	
	public void fastStart(String serverName) {
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND);
		
		CommonResp req = new CommonResp();
		req.setOptionType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_AUTO_START);
		req.setOptionStr("");
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(req);
		
		RoomClient.sendMessage(serverName, message);
	}
	
	public void joinRoomEnd(int roleId, CommonResp resp) {
		RobotRoleInfo roleInfo = roleIdMap.get(roleId);
		
		if (roleInfo == null) {
			errorCode.compareAndSet(1, 10004);
			return;
		}
		
		Map<String, Object> map = LogicUtil.fromJson(resp.getOptionStr(), Map.class);
		
		roleInfo.getRoomOtherRoleMap().put(Integer.parseInt(map.get(LogicValue.KEY_ROLE).toString()), 1);
	}
	
	public void roomInit(int roleId, CommonResp resp) {
		RobotRoleInfo roleInfo = roleIdMap.get(roleId);
		
		if (roleInfo == null) {
			errorCode.compareAndSet(1, 10001);
			return;
		}
		
		Map<String, Object> map = LogicUtil.fromJson(resp.getOptionStr(), Map.class);
		roleInfo.setRoomId(Integer.parseInt(map.get(LogicValue.KEY_ROOM_ID).toString()));
		
		roleInfo.setState(1);
	}
	
	public void roomRoleOperation(CommonResp resp) {
		Map<String, Object> map = LogicUtil.fromJson(resp.getOptionStr(), Map.class);
		
		int roleId = (int)map.get(LogicValue.KEY_ROLE);
		
		RobotRoleInfo roleInfo = roleIdMap.get(roleId);
		
		if (roleInfo == null) {
			errorCode.compareAndSet(1, 10002);
			return;
		}
		
		if (roleInfo.getState() != 2) {
			errorCode.compareAndSet(1, 10003);
			return;
		}
		
		roleInfo.setState(3);
	}
	
	public void roomPlayingEnd(int roleId, CommonResp resp) {
		RobotRoleInfo roleInfo = roleIdMap.get(roleId);
		
		if (roleInfo == null) {
			errorCode.compareAndSet(1, 10006);
			return;
		}
		
		roleInfo.setState(6);
	}
	
	public void roomReady(int roleId, CommonResp resp) {
		RobotRoleInfo roleInfo = roleIdMap.get(roleId);
		
		if (roleInfo == null) {
			errorCode.compareAndSet(1, 10007);
			return;
		}
		
		roleInfo.setState(1);
	}
	
	public void sendCommonMsg(int type, String value, String serverName) {
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_PLAY_SEND);
		CommonResp resp = new CommonResp(type, value);
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(resp);
		
		RoomClient.sendMessage(serverName, message);
	}
	
	public void back2Scene(String serverName) {
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND);
		
		CommonResp req = new CommonResp();
		req.setOptionType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_LEAVE_ROOM);
		req.setOptionStr("");
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(req);
		
		RoomClient.sendMessage(serverName, message);
	}
	
	public void back2SceneEnd(InitSceneResp resp) {
	}
}
