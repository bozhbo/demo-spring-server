	package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.VoiceRoomMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.VoiceInfo;

public class VoiceDao extends SqlMapDaoSupport {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	public final static VoiceDao instance = new VoiceDao();

	public static VoiceDao getInstance() {
		return instance;
	}

	private VoiceDao() {
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadVoiceRoom() {
		try {
			List<VoiceInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectALLRoom");
			
			if (list != null && list.size() > 0) {
				for (VoiceInfo voiceInfo : list) {
					if (voiceInfo.getRoomType() == 1) {
						// 世界频道
						VoiceRoomMap.worldVoiceInfo = voiceInfo;
						continue;
					} else if (voiceInfo.getRoomType() == 2) {
						// 公会频道
						RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(Integer.valueOf(voiceInfo.getRoomDesc()));
					
						if (roleClubInfo != null) {
							roleClubInfo.setVoiceInfo(voiceInfo);
							continue;
						}
					} else {
						logger.warn("load voice room error, roomType = " + voiceInfo.getRoomType());
					}
					
					logger.warn("voice room is not in use, roomDesc = " + voiceInfo.getRoomDesc());
				}
			} else {
				// Nothing todo
			}
		} catch (Exception e) {
			logger.error("loadVoiceRoom error!",e);
			return false;
		}
		
		return true;
	}
	
	public boolean insertVoiceRoom(VoiceInfo voiceInfo) {
		try {
			return getSqlMapClient(DbConstants.GAME_DB).update("insertRoomInfo", voiceInfo);
		} catch (Exception e) {
			logger.error("insertVoiceRoom error!",e);
			return false;
		}
	}
	
	public boolean deleteVoiceRoom(int id) {
		try {
			return getSqlMapClient(DbConstants.GAME_DB).update("deleteRoom", id);
		} catch (Exception e) {
			logger.error("deleteVoiceRoom error!",e);
			return false;
		}
	}
}
