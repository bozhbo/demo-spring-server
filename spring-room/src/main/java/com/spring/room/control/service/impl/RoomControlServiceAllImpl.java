package com.spring.room.control.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.common.GameMessageType;
import com.spring.logic.gf.service.GoldFlowerService;
import com.spring.logic.message.request.common.base.CommonResp;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.cache.RoleRoomCache;
import com.spring.logic.role.enums.RolePlayingState;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomPlayingEnum;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.control.service.RoomLogicService;

/**
 * room-world合并实现
 * 
 * @author zhoubo
 *
 */
@Service
public class RoomControlServiceAllImpl implements RoomControlService {

	private static final Log logger = LogFactory.getLog(RoomControlServiceAllImpl.class);

	private RoomLogicService roomLogicService;

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
				List<RoomRoleInfo> list = playingRoomInfo.getList();

				for (RoomRoleInfo roomRoleInfo : list) {
					Message message = this.messageService.createMessage(roomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
							new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_READY, ""));
					messageService.sendGateMessage(roomRoleInfo.getGateId(), message);
				}
			} else {
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

	private void doRoomReady() {

	}

	private void doRoomSendCard(PlayingRoomInfo playingRoomInfo, long curTime) {
		if (playingRoomInfo.getSendCardTime() == 0) {
			// 发牌
			Collections.shuffle(playingRoomInfo.getCardList());

			int index = 0;
			List<RoomRoleInfo> list = playingRoomInfo.getList();

			for (RoomRoleInfo roomRoleInfo : list) {
				roomRoleInfo.setGoldFlowerInfo(
						GoldFlowerService.getGoldFlowerInfo(playingRoomInfo.getCardList().get(index++), playingRoomInfo.getCardList().get(index++), playingRoomInfo.getCardList().get(index++)));
			}

			// 推送发牌消息
			for (RoomRoleInfo roomRoleInfo : list) {
				Message message = this.messageService.createMessage(roomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
						new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_CARD, "" + roomRoleInfo.getGoldFlowerInfo().getKey()));
				messageService.sendGateMessage(roomRoleInfo.getGateId(), message);
			}
		} else {
			if (curTime - playingRoomInfo.getSendCardTime() > 3000) {
				playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_PLAYING);
				sortRole(playingRoomInfo);
				doRoomPlaying(playingRoomInfo, curTime);
			}
		}
	}

	private void doRoomPlaying(PlayingRoomInfo playingRoomInfo, long curTime) {
		if (playingRoomInfo.getRoomRoleInfo() == null) {
			// 选择第一个玩家
			playingRoomInfo.setRoomRoleInfo(playingRoomInfo.getPlayingList().get(0));
			playingRoomInfo.getRoomRoleInfo().setStartTime(curTime);

			// 发送消息到第一个玩家要求操作
			Message message = this.messageService.createMessage(playingRoomInfo.getRoomRoleInfo().getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
					new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION, ""));
			messageService.sendGateMessage(playingRoomInfo.getRoomRoleInfo().getGateId(), message);
		} else {
			if (curTime - playingRoomInfo.getRoomRoleInfo().getStartTime() < 6000) {
				// 等待玩家操作
				return;
			} else {
				for (int i = 0; i < playingRoomInfo.getPlayingList().size(); i++) {
					int index = playingRoomInfo.getPlayingList().indexOf(playingRoomInfo.getRoomRoleInfo());
					playingRoomInfo.getRoomRoleInfo().setStartTime(0);

					if (index == playingRoomInfo.getPlayingList().size() - 1) {
						playingRoomInfo.setRoomRoleInfo(playingRoomInfo.getPlayingList().get(0));
					} else {
						playingRoomInfo.setRoomRoleInfo(playingRoomInfo.getPlayingList().get(index + 1));
					}

					playingRoomInfo.getRoomRoleInfo().setStartTime(curTime);

					if (playingRoomInfo.getRoomRoleInfo().getRolePlayingState() == RolePlayingState.CLOSE || playingRoomInfo.getRoomRoleInfo().getRolePlayingState() == RolePlayingState.LOST) {
						//
						continue;
					} else {
						// 发送消息到下一个玩家要求操作
						Message message = this.messageService.createMessage(playingRoomInfo.getRoomRoleInfo().getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
								new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION, ""));
						messageService.sendGateMessage(playingRoomInfo.getRoomRoleInfo().getGateId(), message);
						break;
					}
				}
			}
		}
	}

	@Override
	public void ready(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (playingRoomInfo.getList().contains(roomRoleInfo) && !playingRoomInfo.getPlayingList().contains(roomRoleInfo)) {
			if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_INIT) {
				playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_READY);
			} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_READY) {
				// nothing to do
			} else {
				// TODO 状态不对
				return;
			}

			playingRoomInfo.getPlayingList().add(roomRoleInfo);
		}

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

		roomRoleInfo.setRolePlayingState(RolePlayingState.CLOSE);
	}

	@Override
	public void follow(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}

		int gold = 0;

		if (roomRoleInfo.getRolePlayingState() == RolePlayingState.OPEN) {
			gold = playingRoomInfo.getCurGoldUnit() / 2;
		} else if (roomRoleInfo.getRolePlayingState() == RolePlayingState.UNKNOW) {
			gold = playingRoomInfo.getCurGoldUnit();
		}

		if (roomRoleInfo.getGold() - gold < 0) {
			logger.warn("role gold is not enough");
			return;
		}

		roomRoleInfo.setGold(roomRoleInfo.getGold() - gold);
		playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + gold);

		// TODO 发送跟注消息
	}

	@Override
	public void add(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}

		int gold = 0;

		try {
			gold = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return;
		}

		if (gold <= 0 || gold > 100000) {
			return;
		}

		if (roomRoleInfo.getGold() - gold < 0) {
			logger.warn("role gold is not enough");
			return;
		}

		roomRoleInfo.setGold(roomRoleInfo.getGold() - gold);
		playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + gold);

		// 发送加注消息
		List<RoomRoleInfo> list = playingRoomInfo.getList();

		for (RoomRoleInfo tempRoomRoleInfo : list) {
			messageService.createMessage(tempRoomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
					new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ADD, roomRoleInfo.getRoleId() + ":" + gold));
		}
	}

	@Override
	public void look(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}

		roomRoleInfo.setRolePlayingState(RolePlayingState.OPEN);

		// 发送看牌消息
		messageService.createMessage(roomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
				new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_LOOK_CARD, roomRoleInfo.getGoldFlowerInfo().getKey()));
	}

	@Override
	public void compare(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str) {
		if (!checkRoleRound(playingRoomInfo, roomRoleInfo)) {
			return;
		}

		int target = 0;

		try {
			target = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return;
		}

		RoomRoleInfo targetRoomRoleInfo = RoleRoomCache.getRoomRoleInfo(target);

		if (targetRoomRoleInfo == null || !playingRoomInfo.getPlayingList().contains(targetRoomRoleInfo)) {
			logger.warn("target role not exist");
			return;
		}

		if (roomRoleInfo.getRolePlayingState() != RolePlayingState.OPEN && roomRoleInfo.getRolePlayingState() != RolePlayingState.UNKNOW) {
			logger.warn("role state error " + roomRoleInfo.getRolePlayingState() + ", roleId = " + roomRoleInfo.getRoleId());
			return;
		}

		if (targetRoomRoleInfo.getRolePlayingState() != RolePlayingState.OPEN && targetRoomRoleInfo.getRolePlayingState() != RolePlayingState.UNKNOW) {
			logger.warn("role state error " + targetRoomRoleInfo.getRolePlayingState() + ", roleId = " + targetRoomRoleInfo.getRoleId());
			return;
		}

		String value = null;
		int gold = 0;

		if (roomRoleInfo.getRolePlayingState() == RolePlayingState.OPEN) {
			gold = playingRoomInfo.getCurGoldUnit();
		} else {
			gold = playingRoomInfo.getCurGoldUnit() / 2;
		}

		if (roomRoleInfo.getGoldFlowerInfo().getLevel() > targetRoomRoleInfo.getGoldFlowerInfo().getLevel()) {
			value = roomRoleInfo.getRoleId() + "," + targetRoomRoleInfo.getRoleId();
			targetRoomRoleInfo.setRolePlayingState(RolePlayingState.LOST);
		} else {
			value = targetRoomRoleInfo.getRoleId() + "," + roomRoleInfo.getRoleId();
			roomRoleInfo.setRolePlayingState(RolePlayingState.LOST);
		}

		if (roomRoleInfo.getGold() - gold < 0) {
			playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + roomRoleInfo.getGold());
			roomRoleInfo.setGold(0);
		} else {
			playingRoomInfo.setAmountGold(playingRoomInfo.getAmountGold() + gold);
			roomRoleInfo.setGold(roomRoleInfo.getGold() - gold);
		}
		
		sendGoldChange(playingRoomInfo, targetRoomRoleInfo);

		// 发送比牌消息
		List<RoomRoleInfo> list = playingRoomInfo.getList();

		for (RoomRoleInfo tempRoomRoleInfo : list) {
			messageService.createMessage(tempRoomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
					new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_COMPARE, value));
		}

		checkGameover(playingRoomInfo);
	}

	private boolean checkRoleRound(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (playingRoomInfo.getRoomRoleInfo() != roomRoleInfo) {
			// 当前回合不对
			messageService.sendGateMessage(roomRoleInfo.getGateId(), messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, ""));
			logger.error("role msg error for not in round");
			return false;
		}

		return true;
	}

	private void sortRole(PlayingRoomInfo playingRoomInfo) {
		Collections.sort(playingRoomInfo.getPlayingList(), (r1, r2) -> {
			return playingRoomInfo.getList().indexOf(r1) - playingRoomInfo.getList().indexOf(r2);
		});
	}

	private void sendGoldChange(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		for (RoomRoleInfo tempRoomRoleInfo : roleList) {
			messageService.createMessage(tempRoomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
					new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GOLD_CHANGE, roomRoleInfo.getRoleId() + "," + roomRoleInfo.getGold()));
		}
	}
	
	private void sendGameOver(PlayingRoomInfo playingRoomInfo) {
		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		for (RoomRoleInfo tempRoomRoleInfo : roleList) {
			messageService.createMessage(tempRoomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
					new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GAME_END, ""));
		}
	}

	private boolean checkGameover(PlayingRoomInfo playingRoomInfo) {
		List<RoomRoleInfo> list = playingRoomInfo.getPlayingList();
		RoomRoleInfo lastRoomRoleInfo = null;
		int open = 0;

		for (RoomRoleInfo roomRoleInfo : list) {
			RolePlayingState rolePlayingState = roomRoleInfo.getRolePlayingState();

			if (rolePlayingState == RolePlayingState.UNKNOW || rolePlayingState == RolePlayingState.OPEN) {
				lastRoomRoleInfo = roomRoleInfo;
				open++;
			}

			if (open >= 2) {
				return false;
			}
		}

		StringBuffer value = new StringBuffer();

		for (RoomRoleInfo roomRoleInfo : list) {
			if (roomRoleInfo.getRolePlayingState() != RolePlayingState.CLOSE) {
				value.append(roomRoleInfo.getRoleId()).append(":").append(roomRoleInfo.getGoldFlowerInfo().getKey()).append(",");
			}
		}

		lastRoomRoleInfo.setGold(lastRoomRoleInfo.getGold() + playingRoomInfo.getAmountGold());

		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		// 显示牌
		for (RoomRoleInfo tempRoomRoleInfo : roleList) {
			messageService.createMessage(tempRoomRoleInfo.getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
					new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_SHOW_CARD, value.substring(0, value.length() - 1)));
		}

		// 金币变化
		sendGoldChange(playingRoomInfo, lastRoomRoleInfo);

		// 游戏结束
		sendGameOver(playingRoomInfo);

		playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_INIT);
		playingRoomInfo.setLastUpdateTime(System.currentTimeMillis());
		playingRoomInfo.setReadyTime(0);
		playingRoomInfo.setSendCardTime(0);

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

		return true;
	}

	@Autowired
	public void setRoomLogicService(RoomLogicService roomLogicService) {
		this.roomLogicService = roomLogicService;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}
