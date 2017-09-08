package com.spring.common;

public class GameMessageType {

	/**
	 * 登录请求
	 */
	public static int GAME_CLIENT_LOGIN_SEND = 0xA003;
	
	/**
	 * 登录响应
	 */
	public static int GAME_CLIENT_LOGIN_RECEIVE = 0xA004;
	
	/**
	 * 角色初始化请求
	 */
	public static int GAME_CLIENT_INIT_SEND = 0xA005;
	
	/**
	 * 角色初始化响应
	 */
	public static int GAME_CLIENT_INIT_RECEIVE = 0xA006;
	
	public static int GAME_CLIENT_ROOM_AUTO_JOIN_SEND = 0xA101;
	
	public static int GAME_CLIENT_ROOM_AUTO_JOIN_RECEIVE = 0xA102;
	
	public static int GAME_CLIENT_ROOM_CHOOSE_JOIN_SEND = 0xA103;
	
	public static int GAME_CLIENT_ROOM_CHOOSE_JOIN_RECEIVE = 0xA104;
	
	/**
	 * 通用请求
	 */
	public static int GAME_CLIENT_WORLD_COMMON_SEND = 0xA105;
	
	/**
	 * 通用响应
	 */
	public static int GAME_CLIENT_WORLD_COMMON_RECEIVE = 0xA106;
	
	/**
	 * 通用请求二级消息(快速开始)
	 */
	public static int GAME_CLIENT_WORLD_COMMON_SEND_AUTO_START = 1;
	
	/**
	 * 通用请求二级消息(离开房间)
	 */
	public static int GAME_CLIENT_WORLD_COMMON_SEND_LEAVE_ROOM = 2;
	
	/**
	 * 新加入成员响应
	 */
	public static int GAME_CLIENT_ROOM_JOIN = 0xA202;
	
}
