package com.snail.webgame.game.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.info.GameSettingInfo;

/**
 * 
 * 类介绍:游戏全局设定MAP，此集合包含以下信息 1.服务器开服时间
 * [key=SERVER_START_TIME,value=SYSTEM.CurrentTimeMillis()]
 *
 * @author zhoubo
 * @2015-1-4
 */
public class GameSettingMap {

	private static Map<String, GameSettingInfo> map = new HashMap<String, GameSettingInfo>();

	public static void addValue(GameSettingInfo gameSettingInfo) {
		map.put(gameSettingInfo.getKey(), gameSettingInfo);
	}

	public static GameSettingInfo getValue(GameSettingKey key) {
		if (key != null) {
			return map.get(key.getValue());
		}
		return null;
	}
}
