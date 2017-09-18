package com.spring.room.control.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.common.base.CommonResp;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.enums.RolePlayingState;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomPlayingEnum;
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
				return 1;
			} else {
				if (curTime - playingRoomInfo.getLastUpdateTime() > 60000) {
					// TODO close room
				}

				return 1;
			}
		}

		if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_READY) {
			if (playingRoomInfo.getPlayingList().size() <= 1) {
				// 人数过少
				return 1;
			}

			playingRoomInfo.setReadyTime(curTime);
			playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_PLAYING);

			// 推送准备开始消息
			List<RoomRoleInfo> list = playingRoomInfo.getPlayingList();

			for (RoomRoleInfo roomRoleInfo : list) {
				Message message = this.messageService.createMessage(roomRoleInfo.getRoleId(),
						GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
						new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_READY, ""));
				messageService.sendGateMessage(roomRoleInfo.getGateId(), message);
			}

			return 1;
		} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_PLAYING) {
			if (curTime - playingRoomInfo.getReadyTime() < 6000) {
				// 等待开始
				return 1;
			}

			if (playingRoomInfo.getSendCardTime() == 0) {
				// TODO 发牌

				// 推送发牌消息
				List<RoomRoleInfo> list = playingRoomInfo.getPlayingList();

				for (RoomRoleInfo roomRoleInfo : list) {
					Message message = this.messageService.createMessage(roomRoleInfo.getRoleId(),
							GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "", new CommonResp(
									GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_CARD, "" + roomRoleInfo.getCard()));
					messageService.sendGateMessage(roomRoleInfo.getGateId(), message);
				}
				return 1;
			} else {
				if (curTime - playingRoomInfo.getSendCardTime() < 3000) {
					// 发牌中
					return 1;
				} else {
					if (playingRoomInfo.getRoomRoleInfo() == null) {
						// 选择第一个玩家
						playingRoomInfo.setRoomRoleInfo(playingRoomInfo.getPlayingList().get(0));
						playingRoomInfo.getRoomRoleInfo().setStartTime(curTime);

						// 发送消息到第一个玩家要求操作
						Message message = this.messageService.createMessage(
								playingRoomInfo.getRoomRoleInfo().getRoleId(), GameMessageType.GAME_CLIENT_PLAY_RECEIVE,
								playingRoomInfo.getRoomId(), "",
								new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION, ""));
						messageService.sendGateMessage(playingRoomInfo.getRoomRoleInfo().getGateId(), message);
					} else {
						if (curTime - playingRoomInfo.getRoomRoleInfo().getStartTime() < 6000) {
							// 等待玩家操作
							return 1;
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

								if (playingRoomInfo.getRoomRoleInfo().getRolePlayingState() == RolePlayingState.CLOSE
										|| playingRoomInfo.getRoomRoleInfo()
												.getRolePlayingState() == RolePlayingState.LOST) {
									//
									continue;
								} else {
									// 发送消息到下一个玩家要求操作
									Message message = this.messageService.createMessage(
											playingRoomInfo.getRoomRoleInfo().getRoleId(),
											GameMessageType.GAME_CLIENT_PLAY_RECEIVE, playingRoomInfo.getRoomId(), "",
											new CommonResp(GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION, ""));
									messageService.sendGateMessage(playingRoomInfo.getRoomRoleInfo().getGateId(),
											message);
								}
							}
						}
					}
				}
			}
		}

		return 1;
	}

	@Override
	public void ready(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		long curTime = System.currentTimeMillis();

		if (playingRoomInfo.getList().contains(roomRoleInfo)
				&& !playingRoomInfo.getPlayingList().contains(roomRoleInfo)) {
			if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_INIT) {
				playingRoomInfo.setRoomState(RoomPlayingEnum.ROOM_STATE_READY);
			} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_READY) {
				// nothing to do
			} else if (playingRoomInfo.getRoomState() == RoomPlayingEnum.ROOM_STATE_PLAYING) {
				if (curTime - playingRoomInfo.getReadyTime() >= 5000) {
					// TODO 已经过了准备时间
					return;
				}
			} else {
				// TODO 状态不对
				return;
			}

			playingRoomInfo.getPlayingList().add(roomRoleInfo);
		}
	}

	@Override
	public void giveUp(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (playingRoomInfo.getRoomRoleInfo() != roomRoleInfo) {
			// 当前回合不对
			messageService.sendGateMessage(roomRoleInfo.getGateId(),
					messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, ""));
			logger.error("role msg error for not in round");
			return;
		}

		roomRoleInfo.setRolePlayingState(RolePlayingState.CLOSE);
	}

	@Override
	public void follow(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		if (playingRoomInfo.getRoomRoleInfo() != roomRoleInfo) {
			// 当前回合不对
			messageService.sendGateMessage(roomRoleInfo.getGateId(),
					messageService.createErrorMessage(roomRoleInfo.getRoleId(), 730001, ""));
			logger.error("role msg error for not in round");
			return;
		}

		
	}

	@Override
	public void add(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str) {
		// TODO Auto-generated method stub

	}

	@Override
	public void look(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void compare(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str) {
		// TODO Auto-generated method stub

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
