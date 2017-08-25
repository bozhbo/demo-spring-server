package com.snail.webgame.game.common;

/**
 * 消息队列发送实体
 * 
 * @author xiasd
 * 
 */
public class TimeMessage {
	private ETimeMessageType type;
	private int reTryCount; // 重试次数

	public TimeMessage(ETimeMessageType type) {
		this.type = type;
	}

	public ETimeMessageType getType() {
		return type;
	}

	public int getReTryCount() {
		return reTryCount;
	}

	public void setReTryCount(int reTryCount) {
		this.reTryCount = reTryCount;
	}

}
