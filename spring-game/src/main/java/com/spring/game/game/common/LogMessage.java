package com.snail.webgame.game.common;

import java.util.List;

/**
 * 日志类
 * 
 * @author xiasd
 *
 */
public class LogMessage extends TimeMessage {

	private List<?> logList;
	
	public LogMessage(ETimeMessageType type, List<?> logList) {
		super(type);
		this.logList = logList;
	}

	public List<?> getLogList() {
		return logList;
	}
	
}
