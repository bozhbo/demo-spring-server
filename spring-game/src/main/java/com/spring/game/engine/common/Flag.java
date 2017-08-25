package com.snail.webgame.engine.common;

public class Flag {
	/**
	 * 1标识还没有注册到计费应用上，0标识已经注册到计费应用上
	 */
	public volatile static int flag = 1;
	/**
	 * 数据定时更新全局锁
	 */
	public static final Object OBJ_DB = new Object(); 
	/**
	 * 登录排队锁
	 */
	public static final Object OBJ_LOGIN_QUEUE = new Object();
}
