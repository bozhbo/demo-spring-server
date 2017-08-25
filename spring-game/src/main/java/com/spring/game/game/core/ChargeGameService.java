package com.snail.webgame.game.core;

import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.charge.facade.ChargeService;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.config.GameConfig;

/**
 * 计费业务类
 * 
 * @author zhoubo
 * 
 */
public class ChargeGameService {

	private static final Logger logger = LoggerFactory.getLogger("charge");

	private static ChargeService service;

	/**
	 * 连接计费
	 */
	public static void connect() {
		if (GameValue.GAME_VALIDATEIN_FLAG == 0) {
			if (logger.isInfoEnabled()) {
				logger.info("Charge switch is off");
			}

			return;
		}

		service = new ChargeService(ThreadPoolManager.getInstance().Pool("snailcharge"));
		String logLevel = GameConfig.getInstance().getChargeLog();
		ChargeService.setLogLevel((logLevel == null || "".equals(logLevel.trim())) ? "DEBUG" : logLevel.trim());
		ChargeService.setMallExist(false);

		try {
			service.connect(GameConfig.getInstance().getChargeIp(), GameConfig.getInstance().getChargePort(),
					GameConfig.getInstance().getGameType(), GameConfig.getInstance().getGameServerId(), GameConfig
							.getInstance().getGameName(), new ChargeServiceHandleImpl());
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("connect charge error", e);
			}
		}
	}

	public static void disconnect() {
		if (GameValue.GAME_VALIDATEIN_FLAG == 0) {
			return;
		}
		try {
			service.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ChargeService getChargeService() {
		return service;
	}
}
