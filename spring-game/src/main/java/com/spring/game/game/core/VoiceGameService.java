package com.snail.webgame.game.core;

import java.util.Map;

import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.voice.cache.ServerMap;
import com.snail.webgame.engine.component.voice.facade.VoiceService;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.info.RoleClubInfo;

/**
 * 
 * 类介绍:语音服务器接口帮助类
 *
 * @author zhoubo
 * @2015年7月31日
 */
public class VoiceGameService {

	private static final Logger logger = LoggerFactory.getLogger("voice");

	private static VoiceService service;

	/**
	 * 连接语音
	 */
	public static void connect() {
		if (GameValue.GAME_VOICE_FLAG == 0) {
			if (logger.isInfoEnabled()) {
				logger.info("Voice switch is off");
			}

			return;
		}

		service = new VoiceService(ThreadPoolManager.getInstance().Pool("snailcharge"));
		String logLevel = GameConfig.getInstance().getVoiceLog();
		VoiceService.setLogLevel((logLevel == null || "".equals(logLevel.trim())) ? "DEBUG" : logLevel.trim());

		try {
			service.connect(GameConfig.getInstance().getVoiceIp(), GameConfig.getInstance().getVoicePort(), new VoiceServiceHandleImpl());
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("connect voice error", e);
			}
		}
	}

	public static void disconnect() {
		if (GameValue.GAME_VOICE_FLAG == 0) {
			return;
		}
		try {
			service.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static VoiceService getVoiceService() {
		return service;
	}
	
	/**
	 * 服务器启动时,检测公会聊天房间
	 */
	public static void serverStartCreateClubRoom()
	{
		// 40秒内连不上语音服务器后,公会未创建房间的不再请求创建。
		boolean flag = true;
		try
		{
			if (ServerMap.getIoSession("VoiceServer") == null || !ServerMap.getIoSession("VoiceServer").isConnected()) {
				Thread.sleep(20000);
				flag = false;
			}
			
			if (ServerMap.getIoSession("VoiceServer") == null || !ServerMap.getIoSession("VoiceServer").isConnected()) {
				Thread.sleep(20000);
				flag = false;
			}
			
			if (ServerMap.getIoSession("VoiceServer") != null &&  ServerMap.getIoSession("VoiceServer").isConnected()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(flag)
		{
			try {
				if (VoiceGameService.getVoiceService() != null) {
					Map<Integer, RoleClubInfo> clubMap = RoleClubInfoMap.getAllClub();
					for(int clubId : clubMap.keySet())
					{
						RoleClubInfo clubInfo = clubMap.get(clubId);
						if(clubInfo == null || clubInfo.getVoiceInfo() != null)
						{
							continue;
						}
						VoiceGameService.getVoiceService().sendMessageCreateRoom(GameConfig.getInstance().getVoiceAccount(), GameConfig.getInstance().getVoicePass(), GameConfig.getInstance().getVoiceKey(), "2,"+clubInfo.getId(), "clubRoom:" + clubInfo.getId() + ":" + System.currentTimeMillis(), 1);
						
						Thread.sleep(50);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
}
