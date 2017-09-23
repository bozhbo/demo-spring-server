package com.spring.room.control.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.business.service.RoomBusinessCallBack;
import com.spring.logic.business.service.RoomBusinessService;
import com.spring.logic.message.request.room.RoomInitResp;
import com.spring.logic.message.request.room.RoomJoinResp;
import com.spring.logic.message.request.room.RoomLeaveResp;
import com.spring.logic.message.request.room.RoomOperateJsonRes;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.cache.RoleRoomCache;
import com.spring.logic.role.enums.RoleCardState;
import com.spring.logic.role.enums.RoleRoomState;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.server.service.ServerMessageService;
import com.spring.logic.util.LogicUtil;
import com.spring.logic.util.LogicValue;
import com.spring.room.control.service.RoomLogicService;
import com.spring.room.control.service.RoomMessageService;
import com.spring.room.control.service.RoomWorldService;

@Service
public class RoomLogicServiceImpl implements RoomLogicService {

	private RoomMessageService roomMessageService;

	private ServerMessageService serverMessageService;

	private RoomBusinessCallBack roomBusinessCallBack;

	private RoomBusinessService roomBusinessService;

	private MessageService messageService;

	private RoomWorldService roomWorldService;

	@Override
	public PlayingRoomInfo createPlayingRoomInfo(int roomId, RoomTypeEnum roomTypeEnum) {
		PlayingRoomInfo playingRoomInfo = new PlayingRoomInfo(roomId, roomTypeEnum);
		
		if (roomTypeEnum == RoomTypeEnum.ROOM_TYPE_NEW) {
			playingRoomInfo.setCurGoldUnit(200);
		} else if (roomTypeEnum == RoomTypeEnum.ROOM_TYPE_LEVEL1) {
			playingRoomInfo.setCurGoldUnit(500);
		} else if (roomTypeEnum == RoomTypeEnum.ROOM_TYPE_LEVEL2) {
			playingRoomInfo.setCurGoldUnit(1000);
		} else if (roomTypeEnum == RoomTypeEnum.ROOM_TYPE_LEVEL3) {
			playingRoomInfo.setCurGoldUnit(2000);
		} else if (roomTypeEnum == RoomTypeEnum.ROOM_TYPE_LEVEL4) {
			playingRoomInfo.setCurGoldUnit(3000);
		}

		return playingRoomInfo;
	}

	@Override
	public RoomRoleInfo createRoomRoleInfo(PlayingRoomInfo playingRoomInfo, DeployRoleReq req) {
		RoomRoleInfo roomRoleInfo = new RoomRoleInfo();
		roomRoleInfo.setRoleId(req.getRoleId());
		roomRoleInfo.setRoomId(req.getRoomId());
		roomRoleInfo.setRoleRoomState(RoleRoomState.INIT);

		// 恢复角色数据
		int result = roomBusinessCallBack.roomRoleOnRecover(roomRoleInfo, req);

		if (result != 1) {
			// 通知World添加用户失败
			roomWorldService.deployRoleInfoFailed(req.getRoomId(), req.getRoleId());
			return null;
		}

		return roomRoleInfo;
	}

	@Override
	public void addRole(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (playingRoomInfo.getList().size() >= 5) {
			roomWorldService.deployRoleInfoFailed(playingRoomInfo.getRoomId(), roomRoleInfo.getRoleId());
			return;
		}

		// 发送房间当前数据
		RoomMessageHead roomMessageHead = messageService.createMessageHead(roomRoleInfo.getRoleId(), 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
				getRespRoomInfo(playingRoomInfo));
		messageService.sendGateMessage(roomRoleInfo.getGateId(), roomMessageHead);
		
		// 发送玩家加入房间信息
		roomMessageHead = messageService.createMessageHead(roomRoleInfo.getRoleId(), 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
				getRespRoomInfo(playingRoomInfo));
		messageService.sendGateMessage(roomRoleInfo.getGateId(), roomMessageHead);
		RoomJoinResp roomJoinResp = roomBusinessService.getRoomJoinResp(roomRoleInfo);

		List<RoomRoleInfo> list = playingRoomInfo.getList();

		for (RoomRoleInfo roomRoleInfo2 : list) {
			Message joinMessage = messageService.createMessage(roomRoleInfo2.getRoleId(),
					GameMessageType.GAME_CLIENT_ROOM_JOIN, playingRoomInfo.getRoomId(), "", roomJoinResp);
			messageService.sendGateMessage(roomRoleInfo2.getGateId(), joinMessage);
		}

		list.add(roomRoleInfo);
		
		// 业务回调
		roomBusinessCallBack.roomRoleOnAdd(playingRoomInfo, roomRoleInfo);

		// 通知World添加用户成功
		roomWorldService.deployRoleInfoSuccessed(playingRoomInfo.getRoomId(), roomRoleInfo.getRoleId());
	}

	@Override
	public void removeRole(PlayingRoomInfo playingRoomInfo, int roleId) {
		List<RoomRoleInfo> list = playingRoomInfo.getList();
		RoomRoleInfo roomRoleInfo = null;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRoleId() == roleId) {
				roomRoleInfo = list.remove(i);
				break;
			}
		}

		if (roomRoleInfo != null) {
			// 删除角色回调
			roomBusinessCallBack.roomRoleOnRemove(playingRoomInfo, roomRoleInfo);
		}
		
		// 发送玩家离开房间信息
		RoomLeaveResp resp = new RoomLeaveResp();
		resp.setRoleId(roleId);

		for (RoomRoleInfo roomRoleInfo2 : list) {
			Message message = messageService.createMessage(roomRoleInfo2.getRoleId(),
					GameMessageType.GAME_CLIENT_ROOM_LEAVE, playingRoomInfo.getRoomId(), "", resp);
			messageService.sendGateMessage(roomRoleInfo2.getGateId(), message);
		}

		// 发送World移除用户
		roomWorldService.removeRoleInfoSuccessed(playingRoomInfo.getRoomId(), roleId);
	}
	
	@Override
	public String getRespRoomInfo(PlayingRoomInfo playingRoomInfo) {
		List<RoomRoleInfo> list = playingRoomInfo.getList();
		Map<String, Object> roomMap = new HashMap<>();
		
		roomMap.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROOM_INIT);
		roomMap.put(LogicValue.KEY_ROOM_ID, playingRoomInfo.getRoomId());
		roomMap.put(LogicValue.KEY_ROOM_GOLD, playingRoomInfo.getAmountGold());
		roomMap.put(LogicValue.KEY_ROOM_UNIT_GOLD, playingRoomInfo.getCurGoldUnit());
		roomMap.put(LogicValue.KEY_ROOM_ROUND, playingRoomInfo.getRoomRound());
		
		List<Map<String, Object>> roleList = new ArrayList<>();
		
		for (RoomRoleInfo roomRoleInfo : list) {
			Map<String, Object> map = new HashMap<>();
			map.put(LogicValue.KEY_ROLE_GOLD, roomRoleInfo.getGold());
			map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
			map.put(LogicValue.KEY_ROLE_NAME, roomRoleInfo.getRoleName());
			map.put(LogicValue.KEY_ROLE_CARD_STATE, roomRoleInfo.getRoleCardState());
			map.put(LogicValue.KEY_ROLE_PLAY_STATE, roomRoleInfo.getRoleRoomState());
			map.put(LogicValue.KEY_ROLE_VIP, roomRoleInfo.getVipLevel());
			map.put(LogicValue.KEY_ROLE_HEAD, roomRoleInfo.getHeader());
			map.put(LogicValue.KEY_ROLE_ONLINE, roomRoleInfo.getOnline());
			
			roleList.add(map);
		}
		
		roomMap.put(LogicValue.KEY_ROOM_ROLE, roleList);
		
		return LogicUtil.tojson(roomMap);
	}
	
	public String getRespRoleInfo(RoomRoleInfo roomRoleInfo) {
		Map<String, Object> map = new HashMap<>();
		
		map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROLE_JOIN);
		map.put(LogicValue.KEY_ROLE_GOLD, roomRoleInfo.getGold());
		map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_ROLE_NAME, roomRoleInfo.getRoleName());
		map.put(LogicValue.KEY_ROLE_CARD_STATE, roomRoleInfo.getRoleCardState());
		map.put(LogicValue.KEY_ROLE_PLAY_STATE, roomRoleInfo.getRoleRoomState());
		map.put(LogicValue.KEY_ROLE_VIP, roomRoleInfo.getVipLevel());
		map.put(LogicValue.KEY_ROLE_HEAD, roomRoleInfo.getHeader());
		map.put(LogicValue.KEY_ROLE_ONLINE, roomRoleInfo.getOnline());
		
		return LogicUtil.tojson(map);
	}
	
	public static void main(String[] args) {
		LogicUtil.initJson();
		PlayingRoomInfo playingRoomInfo = new PlayingRoomInfo(1, RoomTypeEnum.ROOM_TYPE_NEW);
		
		playingRoomInfo.setRoomRound(1);
		playingRoomInfo.setAmountGold(1000);
		playingRoomInfo.setCurGoldUnit(100);
		playingRoomInfo.setRoomRound(2);
		
		for (int i = 0; i < 2; i++) {
			RoomRoleInfo roomRoleInfo = new RoomRoleInfo();
			roomRoleInfo.setGold(100 * (i + 1));
			roomRoleInfo.setHeader("xxmm" + i);
			roomRoleInfo.setOnline(1);
			roomRoleInfo.setRoleCardState(RoleCardState.OPEN);
			roomRoleInfo.setRoleRoomState(RoleRoomState.PLAYING);
			roomRoleInfo.setRoleId(i + 1);
			roomRoleInfo.setRoleName("role" + i + 1);
			roomRoleInfo.setRoomId(1);
			roomRoleInfo.setVipLevel(9);
			
			playingRoomInfo.getList().add(roomRoleInfo);
		}
		
		RoomLogicServiceImpl service = new RoomLogicServiceImpl();
		String str = service.getRespRoomInfo(playingRoomInfo);
		
		System.out.println(str);
	}

	@Autowired
	public void setRoomMessageService(RoomMessageService roomMessageService) {
		this.roomMessageService = roomMessageService;
	}

	@Autowired
	public void setServerMessageService(ServerMessageService serverMessageService) {
		this.serverMessageService = serverMessageService;
	}

	@Autowired
	public void setRoomBusinessService(RoomBusinessService roomBusinessService) {
		this.roomBusinessService = roomBusinessService;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	@Autowired
	public void setRoomWorldService(RoomWorldService roomWorldService) {
		this.roomWorldService = roomWorldService;
	}

	@Autowired
	public void setRoomBusinessCallBack(RoomBusinessCallBack roomBusinessCallBack) {
		this.roomBusinessCallBack = roomBusinessCallBack;
	}

	

}
