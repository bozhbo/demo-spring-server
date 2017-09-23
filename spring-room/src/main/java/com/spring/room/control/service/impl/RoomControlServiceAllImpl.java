package com.spring.room.control.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.gf.service.GoldFlowerService;
import com.spring.logic.message.request.room.RoomOperateJsonRes;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.cache.RoleRoomCache;
import com.spring.logic.role.enums.RoleCardState;
import com.spring.logic.role.enums.RoleRoomState;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomPlayingEnum;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.logic.util.LogicValue;
import com.spring.room.control.service.RoomControlService;

/**
 * room-world合并实现
 * 
 * @author zhoubo
 *
 */
@Service
public class RoomControlServiceAllImpl implements RoomControlService {

	private static final Log logger = LogFactory.getLog(RoomControlServiceAllImpl.class);

	private MessageService messageService;

	@Override
	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo, long curTime) {
		if (playingRoomInfo.getList().size() == 0) {
			if (playingRoomInfo.getLastUpdateTime() == 0) {
				playingRoomInfo.setLastUpdateTime(curTime);
			} else {
				if (curTime - playingRoomInfo.getLastUpdateTime() > 60000) {
					// TODO close room
				}
			}

			return 1;
		}

		if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_READY) {
			if (playingRoomInfo.getPlayingList().size() <= 1) {
				// 人数过少
				return 1;
			}

			if (playingRoomInfo.getReadyTime() == 0) {
				playingRoomInfo.setReadyTime(curTime);

				// 推送准备开始消息,出现开始倒计时,当前状态玩家可以继续加入游戏
				RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
						RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_READY).toString());
				send2AllRoles(playingRoomInfo, roomMessageHead);
			} else {
				// 准备时间结束,进入发牌阶段,当前状态玩家不可以继续加入游戏
				if (curTime - playingRoomInfo.getReadyTime() > 6000) {
					playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_SEND_CARD);
					doRoomSendCard(playingRoomInfo, curTime);
				} else {
					// 等待开始中
				}
			}

			return 1;
		} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_SEND_CARD) {
			doRoomSendCard(playingRoomInfo, curTime);
		} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_PLAYING) {
			doRoomPlaying(playingRoomInfo, curTime);
		}

		return 1;
	}

	private void doRoomSendCard(PlayingRoomInfo playingRoomInfo, long curTime) {
		if (playingRoomInfo.getSendCardTime() == 0) {
			// 发牌
			Collections.shuffle(playingRoomInfo.getCardList());

			int index = 0;
			List<RoomRoleInfo> list = playingRoomInfo.getList();

			for (RoomRoleInfo roomRoleInfo : list) {
				roomRoleInfo.setRoleRoomState(RoleRoomState.PLAYING);
				roomRoleInfo.setGoldFlowerInfo(
						GoldFlowerService.getGoldFlowerInfo(playingRoomInfo.getCardList().get(index++), playingRoomInfo.getCardList().get(index++), playingRoomInfo.getCardList().get(index++)));
			}

			// 推送发牌消息
			for (RoomRoleInfo roomRoleInfo : list) {
				Map<String, Object> map = new HashMap<>();
				map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_CARD);
				map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
				map.put(LogicValue.KEY_ROLE_CARD, roomRoleInfo.getGoldFlowerInfo().getKey());

				RoomMessageHead roomMessageHead = messageService.createMessageHead(roomRoleInfo.getRoleId(), playingRoomInfo.getRoomId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE,
						playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
				messageService.sendGateMessage(roomRoleInfo.getGateId(), roomMessageHead);
			}
		} else {
			if (curTime - playingRoomInfo.getSendCardTime() > 3000) {
				playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_PLAYING);
				Collections.sort(playingRoomInfo.getPlayingList(), (r1, r2) -> {
					return playingRoomInfo.getList().indexOf(r1) - playingRoomInfo.getList().indexOf(r2);
				});
				doRoomPlaying(playingRoomInfo, curTime);
			}
		}
	}

	private void doRoomPlaying(PlayingRoomInfo playingRoomInfo, long curTime) {
		if (playingRoomInfo.getRoomRoleInfo() == null) {
			// 选择第一个玩家
			playingRoomInfo.setRoomRoleInfo(playingRoomInfo.getPlayingList().get(0));
			playingRoomInfo.getRoomRoleInfo().setStartTime(curTime);
			playingRoomInfo.setRoomRound(1);
			playingRoomInfo.setRoomRoundTemp(1);

			// 发送消息到第一个玩家要求操作
			Map<String, Object> map = new HashMap<>();
			map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION);
			map.put(LogicValue.KEY_ROLE, playingRoomInfo.getRoomRoleInfo().getRoleId());

			RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
			send2AllRoles(playingRoomInfo, roomMessageHead);
		} else {
			if (curTime - playingRoomInfo.getRoomRoleInfo().getStartTime() < 6000) {
				// 等待玩家操作
				return;
			} else {
				doRoleTimeout(playingRoomInfo, playingRoomInfo.getRoomRoleInfo());

				toNextRole(playingRoomInfo);
			}
		}
	}

	@Override
	public void ready(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (!playingRoomInfo.getList().contains(roomRoleInfo)) {
			logger.error("role ready error for not in room");
			return;
		}

		if (playingRoomInfo.getPlayingMap().containsKey(roomRoleInfo.getRoleId())) {
			// 已经准备
			return;
		}

		if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_INIT) {
			playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_READY);
		} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_READY) {
			// 可以继续加入
		} else {
			logger.error("room is in playing");
			return;
		}

		roomRoleInfo.setRoleRoomState(RoleRoomState.READY);

		playingRoomInfo.getPlayingMap().put(roomRoleInfo.getRoleId(), roomRoleInfo);
		playingRoomInfo.getPlayingList().add(roomRoleInfo);

		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
				RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROLE_READY).addIntValue(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId()).toString());
		send2AllRoles(playingRoomInfo, roomMessageHead);

		if (playingRoomInfo.getPlayingList().size() == playingRoomInfo.getList().size()) {
			// 立即开始
			playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_SEND_CARD);
		}
	}

	@Override
	public void giveUp(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}
		
		if (playingRoomInfo.getRoomState() != RoomPlayingEnum.ROOM_STATE_PLAYING) {
			logger.error("room is not in playing");
			return;
		}

		roomRoleInfo.setRoleRoomState(RoleRoomState.UNPLAYING);
		roomRoleInfo.setRoleCardState(RoleCardState.CLOSE);

		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
				RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_UP).addIntValue(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId()).toString());
		send2AllRoles(playingRoomInfo, roomMessageHead);

		// 检查游戏结束
		checkGameover(playingRoomInfo);
	}

	@Override
	public void follow(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}
		
		if (playingRoomInfo.getRoomState() != RoomPlayingEnum.ROOM_STATE_PLAYING) {
			logger.error("room is not in playing");
			return;
		}

		int gold = 0;

		if (roomRoleInfo.getRoleCardState() == RoleCardState.OPEN) {
			gold = playingRoomInfo.getCurGoldUnit();
		} else if (roomRoleInfo.getRoleCardState() == RoleCardState.UNKNOW) {
			gold = playingRoomInfo.getCurGoldUnit() / 2;
		}

		if (roomRoleInfo.getGold() - gold < 0) {
			messageService.sendGateMessage(roomRoleInfo.getGateId(), messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, roomRoleInfo.getGold() + ""));
			return;
		} else {
			roomRoleInfo.setGold(roomRoleInfo.getGold() - gold);
			playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + gold);
		}

		Map<String, Object> map = new HashMap<>();
		map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_FOLLOW);
		map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_ROLE_REDUCE_GOLD, gold);
		map.put(LogicValue.KEY_ROLE_GOLD, roomRoleInfo.getGold());
		map.put(LogicValue.KEY_ROOM_GOLD, playingRoomInfo.getAmountGold());

		// 发送跟注消息
		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
		send2AllRoles(playingRoomInfo, roomMessageHead);
	}

	@Override
	public void add(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}
		
		if (playingRoomInfo.getRoomState() != RoomPlayingEnum.ROOM_STATE_PLAYING) {
			logger.error("room is not in playing");
			return;
		}

		int gold = 0;

		try {
			gold = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return;
		}

		if (gold <= 0 || gold > 100000) {
			// TODO 100000
			return;
		}

		if (roomRoleInfo.getGold() - gold < 0) {
			messageService.sendGateMessage(roomRoleInfo.getGateId(), messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, roomRoleInfo.getGold() + ""));
			return;
		} else {
			roomRoleInfo.setGold(roomRoleInfo.getGold() - gold);
			playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + gold);
		}

		Map<String, Object> map = new HashMap<>();
		map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ADD);
		map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_ROLE_REDUCE_GOLD, gold);
		map.put(LogicValue.KEY_ROLE_GOLD, roomRoleInfo.getGold());
		map.put(LogicValue.KEY_ROOM_GOLD, playingRoomInfo.getAmountGold());

		// 发送跟注消息
		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
		send2AllRoles(playingRoomInfo, roomMessageHead);
	}

	@Override
	public void look(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}
		
		if (playingRoomInfo.getRoomState() != RoomPlayingEnum.ROOM_STATE_PLAYING) {
			logger.error("room is not in playing");
			return;
		}

		roomRoleInfo.setRoleCardState(RoleCardState.OPEN);

		Map<String, Object> map = new HashMap<>();
		map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_LOOK_CARD);
		map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_ROLE_CARD, roomRoleInfo.getGoldFlowerInfo().getKey());

		RoomMessageHead myRoomMessageHead = messageService.createMessageHead(roomRoleInfo.getRoleId(), 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
		messageService.sendGateMessage(roomRoleInfo.getGateId(), myRoomMessageHead);

		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
				RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_LOOK_CARD).addIntValue(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId()).toString());
		send2AllRolesExcept(playingRoomInfo, roomRoleInfo, roomMessageHead);
	}

	@Override
	public void compare(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}
		
		if (playingRoomInfo.getRoomState() != RoomPlayingEnum.ROOM_STATE_PLAYING) {
			logger.error("room is not in playing");
			return;
		}

		int target = 0;

		try {
			target = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			logger.error("compare failed for target error " + str);
			return;
		}

		RoomRoleInfo targetRoomRoleInfo = RoleRoomCache.getRoomRoleInfo(target);

		if (targetRoomRoleInfo == null || !playingRoomInfo.getPlayingMap().containsKey(targetRoomRoleInfo.getRoleId())) {
			logger.warn("compare failed for target is not playing " + str);
			return;
		}

		if (roomRoleInfo.getRoleRoomState() != RoleRoomState.PLAYING) {
			logger.warn("compare failed for role state error " + roomRoleInfo.getRoleRoomState() + ", roleId = " + roomRoleInfo.getRoleId());
			return;
		}

		if (targetRoomRoleInfo.getRoleRoomState() != RoleRoomState.PLAYING) {
			logger.warn("compare failed for target role state error " + targetRoomRoleInfo.getRoleRoomState() + ", roleId = " + targetRoomRoleInfo.getRoleId());
			return;
		}

		int gold = 0;

		if (roomRoleInfo.getRoleCardState() == RoleCardState.OPEN) {
			gold = playingRoomInfo.getCurGoldUnit();
		} else if (roomRoleInfo.getRoleCardState() == RoleCardState.UNKNOW) {
			gold = playingRoomInfo.getCurGoldUnit() / 2;
		}

		if (roomRoleInfo.getGold() - gold < 0) {
			messageService.sendGateMessage(roomRoleInfo.getGateId(), messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, roomRoleInfo.getGold() + ""));
			return;
		} else {
			roomRoleInfo.setGold(roomRoleInfo.getGold() - gold);
			playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + gold);
		}

		int winRoleId = 0;
		int lostRoleId = 0;

		if (roomRoleInfo.getGoldFlowerInfo().getLevel() > targetRoomRoleInfo.getGoldFlowerInfo().getLevel()) {
			winRoleId = roomRoleInfo.getRoleId();
			lostRoleId = targetRoomRoleInfo.getRoleId();
			targetRoomRoleInfo.setRoleRoomState(RoleRoomState.LOST);
		} else {
			winRoleId = targetRoomRoleInfo.getRoleId();
			lostRoleId = roomRoleInfo.getRoleId();
			roomRoleInfo.setRoleRoomState(RoleRoomState.LOST);
		}

		Map<String, Object> map = new HashMap<>();
		map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_COMPARE);
		map.put(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_ROLE_REDUCE_GOLD, gold);
		map.put(LogicValue.KEY_ROLE_GOLD, roomRoleInfo.getGold());
		map.put(LogicValue.KEY_ROOM_GOLD, playingRoomInfo.getAmountGold());
		map.put(LogicValue.KEY_TARGET_ROLE, targetRoomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_LOST_ROLE, lostRoleId);
		map.put(LogicValue.KEY_WIN_ROLE, winRoleId);

		// 发送比牌消息
		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
		send2AllRoles(playingRoomInfo, roomMessageHead);

		// 检查游戏结束
		checkGameover(playingRoomInfo);
	}

	@Override
	public void offline(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (roomRoleInfo.getRoleRoomState() == RoleRoomState.PLAYING) {

		} else {
			// 其他状态无修改
		}
	}

	private boolean checkRoleRound(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (playingRoomInfo.getRoomRoleInfo() != roomRoleInfo) {
			// 当前回合不对
			messageService.sendGateMessage(roomRoleInfo.getGateId(), messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, ""));
			logger.error("role round error");
			return false;
		}

		if (roomRoleInfo.getRoleRoomState() != RoleRoomState.PLAYING) {
			// 当前状态不对
			messageService.sendGateMessage(roomRoleInfo.getGateId(), messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, ""));
			logger.error("role state error");
			return false;
		}

		return true;
	}

	/**
	 * 检查游戏结束
	 * 
	 * @param playingRoomInfo
	 * @return
	 */
	private boolean checkGameover(PlayingRoomInfo playingRoomInfo) {
		List<RoomRoleInfo> list = playingRoomInfo.getPlayingList();
		RoomRoleInfo lastRoomRoleInfo = null;
		int open = 0;

		for (RoomRoleInfo roomRoleInfo : list) {
			RoleRoomState roleRoomState = roomRoleInfo.getRoleRoomState();

			if (roleRoomState == RoleRoomState.PLAYING) {
				lastRoomRoleInfo = roomRoleInfo;
				open++;
			}

			if (open >= 2) {
				return false;
			}
		}

		// 游戏结束
		lastRoomRoleInfo.setGold(lastRoomRoleInfo.getGold() + playingRoomInfo.getAmountGold());

		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		// 显示自己和比牌输的牌
		for (RoomRoleInfo tempRoomRoleInfo : roleList) {
			if (tempRoomRoleInfo.getRoleRoomState() == RoleRoomState.LOST || tempRoomRoleInfo == lastRoomRoleInfo) {
				RoomMessageHead myRoomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
						RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_SHOW_CARD).addIntValue(LogicValue.KEY_ROLE, tempRoomRoleInfo.getRoleId())
								.addStringValue(LogicValue.KEY_ROLE_CARD, tempRoomRoleInfo.getGoldFlowerInfo().getKey()).toString());
				send2AllRoles(playingRoomInfo, myRoomMessageHead);
			}
		}

		// 发送结束消息
		Map<String, Object> map = new HashMap<>();
		map.put(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GAME_END);
		map.put(LogicValue.KEY_ROLE, lastRoomRoleInfo.getRoleId());
		map.put(LogicValue.KEY_ROLE_GOLD, lastRoomRoleInfo.getRoleId());
		
		RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), LogicUtil.tojson(map));
		send2AllRoles(playingRoomInfo, roomMessageHead);

		playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_INIT);
		playingRoomInfo.setLastUpdateTime(System.currentTimeMillis());
		playingRoomInfo.setReadyTime(0);
		playingRoomInfo.setSendCardTime(0);
		playingRoomInfo.setRoomRound(1);
		playingRoomInfo.setRoomRoundTemp(1);

		if (playingRoomInfo.getRoomType() == RoomTypeEnum.ROOM_TYPE_NEW) {
			playingRoomInfo.setCurGoldUnit(200);
		} else if (playingRoomInfo.getRoomType() == RoomTypeEnum.ROOM_TYPE_LEVEL1) {
			playingRoomInfo.setCurGoldUnit(500);
		} else if (playingRoomInfo.getRoomType() == RoomTypeEnum.ROOM_TYPE_LEVEL2) {
			playingRoomInfo.setCurGoldUnit(1000);
		} else if (playingRoomInfo.getRoomType() == RoomTypeEnum.ROOM_TYPE_LEVEL3) {
			playingRoomInfo.setCurGoldUnit(2000);
		} else if (playingRoomInfo.getRoomType() == RoomTypeEnum.ROOM_TYPE_LEVEL4) {
			playingRoomInfo.setCurGoldUnit(3000);
		}

		playingRoomInfo.setAmountGold(0);
		playingRoomInfo.setRoomRoleInfo(null);
		playingRoomInfo.getPlayingList().clear();
		playingRoomInfo.getPlayingMap().clear();

		return true;
	}

	/**
	 * 玩家超时操作
	 * 
	 * @param playingRoomInfo
	 * @param roomRoleInfo
	 */
	private void doRoleTimeout(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		// TODO
	}

	/**
	 * 下一玩家回合
	 * 
	 * @param playingRoomInfo
	 */
	private void toNextRole(PlayingRoomInfo playingRoomInfo) {
		if (playingRoomInfo.getRoomRoundTemp() == playingRoomInfo.getPlayingList().size()) {
			playingRoomInfo.setRoomRound(playingRoomInfo.getRoomRound() + 1);
			playingRoomInfo.setRoomRoundTemp(1);

			// 通知回合变化
			RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
					RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROUND).addIntValue(LogicValue.KEY_ROOM_ROUND, playingRoomInfo.getRoomRound()).toString());
			send2AllRoles(playingRoomInfo, roomMessageHead);
		} else {
			playingRoomInfo.setRoomRoundTemp(playingRoomInfo.getRoomRoundTemp() + 1);
		}

		int index = playingRoomInfo.getPlayingList().indexOf(playingRoomInfo.getRoomRoleInfo());
		playingRoomInfo.getRoomRoleInfo().setStartTime(0);

		int start = -1;
		int end = -1;
		int start1 = -1;
		int end1 = -1;

		if (index == playingRoomInfo.getPlayingList().size() - 1) {
			// 最后一名
			start = 0;
			end = playingRoomInfo.getPlayingList().size();
		} else {
			start = index + 1;
			end = playingRoomInfo.getPlayingList().size();

			start1 = 0;
			end1 = index;
		}

		RoomRoleInfo roomRoleInfo = null;

		for (int i = start; i < end; i++) {
			roomRoleInfo = playingRoomInfo.getPlayingList().get(i);

			if (roomRoleInfo.getRoleRoomState() != RoleRoomState.PLAYING) {
				roomRoleInfo = null;
				continue;
			}
		}

		for (int i = start1; i < end1; i++) {
			roomRoleInfo = playingRoomInfo.getPlayingList().get(i);

			if (roomRoleInfo.getRoleRoomState() != RoleRoomState.PLAYING) {
				roomRoleInfo = null;
				continue;
			}
		}

		if (roomRoleInfo == null) {
			checkGameover(playingRoomInfo);
		} else {
			playingRoomInfo.setRoomRoleInfo(roomRoleInfo);
			playingRoomInfo.getRoomRoleInfo().setStartTime(System.currentTimeMillis());

			RoomMessageHead roomMessageHead = messageService.createMessageHead(0, 0, GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(),
					RoomOperateJsonRes.instance().addIntValue(LogicValue.KEY_SUB_MSG, GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION).addIntValue(LogicValue.KEY_ROLE, roomRoleInfo.getRoleId()).toString());
			send2AllRoles(playingRoomInfo, roomMessageHead);
		}
	}

	/**
	 * 发送所有玩家消息头
	 * 
	 * @param playingRoomInfo
	 * @param roomMessageHead
	 */
	private void send2AllRoles(PlayingRoomInfo playingRoomInfo, RoomMessageHead roomMessageHead) {
		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		for (RoomRoleInfo roomRoleInfo : roleList) {
			try {
				RoomMessageHead sendRoomMessageHead = roomMessageHead.clone();
				sendRoomMessageHead.setRoleId(roomRoleInfo.getRoleId());

				messageService.sendGateMessage(roomRoleInfo.getGateId(), sendRoomMessageHead);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送所有玩家消息头除了指定玩家不发送
	 * 
	 * @param playingRoomInfo
	 * @param exceptRoomRoleInfo
	 * @param roomMessageHead
	 */
	private void send2AllRolesExcept(PlayingRoomInfo playingRoomInfo, RoomRoleInfo exceptRoomRoleInfo, RoomMessageHead roomMessageHead) {
		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		for (RoomRoleInfo roomRoleInfo : roleList) {
			if (exceptRoomRoleInfo == roomRoleInfo) {
				continue;
			}

			try {
				RoomMessageHead sendRoomMessageHead = roomMessageHead.clone();
				sendRoomMessageHead.setRoleId(roomRoleInfo.getRoleId());

				messageService.sendGateMessage(roomRoleInfo.getGateId(), sendRoomMessageHead);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}
