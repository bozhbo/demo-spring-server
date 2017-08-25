package com.snail.webgame.game.core;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.voice.facade.VoiceServiceHandle;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.VoiceRoomMap;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.dao.VoiceDao;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.VoiceInfo;
import com.snail.webgame.game.protocal.club.entity.RoomIdMsgResp;

public class VoiceServiceHandleImpl implements VoiceServiceHandle {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	@Override
	public void userRegister(int status, String flagId) {
		// 不使用
		
	}

	@Override
	public void messageCreateRoom(int status, String flagId, long roomId) {
		// 创建语音房间成功
		try {
			if (status == 1) {
				// 创建房间成功
				if (roomId > 0) {
					VoiceInfo voiceInfo = new VoiceInfo();
					
					if (flagId.startsWith("1")) {
						// 世界聊天
						voiceInfo.setRoomType((byte)1);
						
						VoiceRoomMap.worldVoiceInfo = voiceInfo;
					} else if (flagId.startsWith("2")) {
						// 公会聊天
						voiceInfo.setRoomType((byte)2);
						voiceInfo.setRoomDesc(flagId.split(",")[1]);
						
						RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(Integer.valueOf(voiceInfo.getRoomDesc()));
						
						if (roleClubInfo != null) {
							roleClubInfo.setVoiceInfo(voiceInfo);
						
							//创建公会后推送给会长roomId
							RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleClubInfo.getCreateRoleId());
							if(roleInfo != null && roleInfo.getLoginStatus() == 1){
								SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(roomId) , roleInfo.getId(), Command.CLUB_ROOM_ID_RESP);
							}
							
						}
					} else {
						if (logger.isErrorEnabled()) {
							logger.error("create voice room failed, flagId = " + flagId);
						}
						
						return;
					}
					
					voiceInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
					voiceInfo.setRoomId(roomId);
					// voiceInfo.setRoomName(roomName);
					
					VoiceDao.getInstance().insertVoiceRoom(voiceInfo);
				} else {
					if (logger.isErrorEnabled()) {
						logger.error("create voice room failed, roomId = " + roomId);
					}
				}
			} else {
				// 创建房间失败
				if (logger.isErrorEnabled()) {
					logger.error("create voice room failed, status = " + status);
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("create voice room failed", e);
			}
		}
	}

	@Override
	public void messageRemoveRoom(int status, String flagId) {
		if (status == 1) {
			// 删除房间成功
			try {
				VoiceDao.getInstance().deleteVoiceRoom(Integer.valueOf(flagId));
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("delete voice room failed, flagId = " + flagId);
				}
			}
		} else {
			// 删除房间失败
			if (logger.isErrorEnabled()) {
				logger.error("delete voice room failed, status = " + status);
			}
		}
	}

	@Override
	public void talkCreateRoom(int status, String flagId, int roomId) {
		// 不使用
		
	}

	@Override
	public void talkRemoveRoom(int status, String flagId) {
		// 不使用
		
	}

	@Override
	public void connectSuccessed() {
		if (VoiceGameService.getVoiceService() != null) {
			if (VoiceRoomMap.worldVoiceInfo == null) {
				try {
					VoiceGameService.getVoiceService().sendMessageCreateRoom(GameConfig.getInstance().getVoiceAccount(), GameConfig.getInstance().getVoicePass(), GameConfig.getInstance().getVoiceKey(), "1", "worldRoom:" + System.currentTimeMillis(), 1);
				} catch (Exception e) {
					logger.error("sendMessageCreateRoom error", e);
				}
			}
		}
	}
}
