package com.spring.common;

public class GameMessageType {
	
	public static int WORLD_2_ROOM_DEPLOY_ROOM = 0xFC01;
	
	public static int WORLD_2_ROOM_REMOVE_ROOM = 0xFC03;
	
	public static int WORLD_2_ROOM_DEPLOY_ROLE = 0xFC05;
	
	public static int WORLD_2_ROOM_REMOVE_ROLE = 0xFC07;

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
	
	/**
	 * 错误信息
	 */
	public static int GAME_CLIENT_ERROR_RECEIVE = 0xA022;
	
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
	
	public static int ROOM_CLIENT_ROOM_INIT = 0xA204;
	
}
