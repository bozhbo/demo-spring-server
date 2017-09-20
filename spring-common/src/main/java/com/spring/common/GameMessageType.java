package com.spring.common;

public class GameMessageType {
	
	/**
	 * 部署房间请求
	 */
	public static int WORLD_2_ROOM_DEPLOY_ROOM_REQ = 0xFC01;
	
	/**
	 * 部署房间响应
	 */
	public static int WORLD_2_ROOM_DEPLOY_ROOM_RESP = 0xFC02;
	
	public static int WORLD_2_ROOM_REMOVE_ROOM_REQ = 0xFC03;
	
	public static int WORLD_2_ROOM_REMOVE_ROOM_RESP = 0xFC04;
	
	public static int WORLD_2_ROOM_DEPLOY_ROLE_REQ = 0xFC05;
	
	public static int WORLD_2_ROOM_DEPLOY_ROLE_RESP = 0xFC06;
	
	public static int WORLD_2_ROOM_REMOVE_ROLE_REQ = 0xFC07;
	
	public static int WORLD_2_ROOM_REMOVE_ROLE_RESP = 0xFC08;
	
	/**
	 * 房间信息提交
	 */
	public static int ROOM_2_WORLD_ROOM_INFO = 0xFC10;

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
	 * 大厅信息响应
	 */
	public static int GAME_CLIENT_WORLD_SCENE_INIT_RECEIVE = 0xA108;
	
	/**
	 * 新成员加入响应
	 */
	public static int GAME_CLIENT_ROOM_JOIN = 0xA202;
	
	/**
	 * 房间当前信息
	 */
	public static int ROOM_CLIENT_ROOM_INIT = 0xA204;
	
	/**
	 * 成员离开响应
	 */
	public static int GAME_CLIENT_ROOM_LEAVE = 0xA206;
	
	/**
	 * 房间内游戏通用请求
	 */
	public static int GAME_CLIENT_PLAY_SEND = 0xA207;
	
	/**
	 * 房间内游戏通用请求二级消息(准备)
	 */
	public static int GAME_CLIENT_PLAY_SEND_READY = 1;
	
	/**
	 * 房间内游戏通用请求二级消息(弃牌)
	 */
	public static int GAME_CLIENT_PLAY_SEND_GIVE_UP = 2;
	
	/**
	 * 房间内游戏通用请求二级消息(跟注)
	 */
	public static int GAME_CLIENT_PLAY_SEND_FOLLOW = 3;
	
	/**
	 * 房间内游戏通用请求二级消息(加注)
	 */
	public static int GAME_CLIENT_PLAY_SEND_ADD = 4;
	
	/**
	 * 房间内游戏通用请求二级消息(看牌)
	 */
	public static int GAME_CLIENT_PLAY_SEND_LOOK = 5;
	
	/**
	 * 房间内游戏通用请求二级消息(比牌)
	 */
	public static int GAME_CLIENT_PLAY_SEND_COMPARE = 6;
	
	/**
	 * 房间内游戏通用响应
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE = 0xA208;
	
	/**
	 * 房间内游戏通用响应二级消息(准备)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_READY = 1;
	
	/**
	 * 房间内游戏通用响应二级消息(发牌)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_GIVE_CARD = 2;
	
	/**
	 * 房间内游戏通用响应二级消息(操作)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_OPERATION = 3;
	
	/**
	 * 房间内游戏通用响应二级消息(比牌)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_COMPARE = 4;
	
	/**
	 * 房间内游戏通用响应二级消息(显示)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_SHOW_CARD = 5;
	
	/**
	 * 房间内游戏通用响应二级消息(金币变化)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_GOLD_CHANGE = 6;
	
	/**
	 * 房间内游戏通用响应二级消息(看牌)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_LOOK_CARD = 7;
	
	/**
	 * 房间内游戏通用响应二级消息(加注)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_ADD = 8;
	
	/**
	 * 房间内游戏通用响应二级消息(结束)
	 */
	public static int GAME_CLIENT_PLAY_RECEIVE_GAME_END = 9;
	
}
