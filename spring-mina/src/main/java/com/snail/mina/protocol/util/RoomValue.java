package com.snail.mina.protocol.util;


public class RoomValue {

	/**
	 * 心跳接口
	 */
	public static final int MESSAGE_TYPE_HEARTBEAT_FF01 = Integer.parseInt("FF01", 16);
	
	/**
	 * 错误码
	 */
	public static final int MESSAGE_TYPE_ERROR_CODE_FF02 = Integer.parseInt("FF02", 16);
	
	/**
	 * 注册接口请求
	 */
	public static final int MESSAGE_TYPE_REGISTER_FE01 = Integer.parseInt("FE01", 16);
	
	/**
	 * 注册接口响应
	 */
	public static final int MESSAGE_TYPE_REGISTER_FE02 = Integer.parseInt("FE02", 16);
	
	/**
	 * 战斗申请接口
	 */
	public static final int MESSAGE_TYPE_FIGHT_REQUEST_FE03 = Integer.parseInt("FE03", 16);
	
	/**
	 * 战斗开始接口(竞技场战斗)
	 */
	public static final int MESSAGE_TYPE_FIGHT_START_FE05 = Integer.parseInt("FE05", 16);
	
	/**
	 * 战斗结束接口
	 */
	public static final int MESSAGE_TYPE_FIGHT_END_FE07 = Integer.parseInt("FE07", 16);
	
	/**
	 * 战斗服务器状态发送接口
	 */
	public static final int MESSAGE_TYPE_SERVER_STATE_FE09 = Integer.parseInt("FE09", 16);
	
	/**
	 * 无使用
	 */
	public static final int MESSAGE_TYPE_FINAL_FIGHT_END_FE11 = Integer.parseInt("FE11", 16);
	
	/**
	 * 取消匹配接口
	 */
	public static final int MESSAGE_TYPE_FIGHT_CANCEL_FE13 = Integer.parseInt("FE13", 16);
	
	/**
	 * 战斗服务器准备就绪通知接口(管理服务器)
	 */
	public static final int MESSAGE_TYPE_SEND_MSG_RESULT_FE15 = Integer.parseInt("FE15", 16);
	
	/**
	 * 战斗服务器准备就绪通知接口(游戏服务器)
	 */
	public static final int MESSAGE_TYPE_SEND_GAME_SERVER_START_FE17 = Integer.parseInt("FE17", 16);
	
	/**
	 * 战斗服务器是否宕机检测接口
	 */
	public static final int MESSAGE_TYPE_SEND_GAME_SERVER_CHECK_FE19 = Integer.parseInt("FE19", 16);
	
	/**
	 * 战斗开始接口(地图战斗)
	 */
	public static final int MESSAGE_TYPE_FIGHT_START_MAP_FE21 = Integer.parseInt("FE21", 16);
	
	/**
	 * 是否启用服务器性能监控
	 */
	public static boolean USE_SERVER_STATE_MONITOR = false;
}
