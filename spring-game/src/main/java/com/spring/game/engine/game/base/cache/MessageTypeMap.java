/*
 * @(#) ServerMap.java 1.00 2011-9-9
 *
 *   FILENAME     :  ServerMap.java
 *   PACKAGE      :  com.snail.webgame.game.cache
 *   CREATE DATE  :  2011-9-9
 *   AUTHOR       :  zhoubo
 *   MODIFIED BY  :  
 *   DESCRIPTION  :  Server Map
 */

package com.snail.webgame.engine.game.base.cache;

import java.util.HashMap;

import com.snail.webgame.engine.game.base.info.common.MessageTypeInfo;

/**
 * 类介绍:游戏服务器上的各种子系统Map
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class MessageTypeMap {

	public static HashMap<Integer, MessageTypeInfo> map = new HashMap<Integer, MessageTypeInfo>();

	public static void addMessageTypeInfo(Integer msgType, MessageTypeInfo messageTypeInfo) {
		map.put(msgType, messageTypeInfo);
	}

	public static MessageTypeInfo getMessageTypeInfo(Integer msgType) {
		return map.get(msgType);
	}
}
