package com.snail.webgame.game.core;

import com.snail.webgame.engine.component.gmcc.common.GmccInitInfo;
import com.snail.webgame.engine.component.gmcc.message.GmccService;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.config.GameConfig;

/**
 * gmcc 处理类
 * @author zenggang
 * 
 */
public class GmccGameService {

	private static GmccService service;

	/**
	 * 连接gmcc
	 */
	public static void connect() {

		GameConfig gameConfig = GameConfig.getInstance();
		GmccInitInfo initInfo = new GmccInitInfo(gameConfig.getGmccIp(), gameConfig.getGmccPort(),
				gameConfig.getGameServerId(), gameConfig.getGameType(), gameConfig.getServerName(),
				gameConfig.getGmccLog());
		service = new GmccService(initInfo, new GmccServiceHandleImpl(), GameValue.GAME_GMCC_FLAG == 1 ? true : false);
		service.connectToGmcc();
	}

	public static void disconnect() {
		service.disconnectFromGmcc();
	}

	public static GmccService getService() {
		return service;
	}
}
