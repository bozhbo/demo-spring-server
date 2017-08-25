package com.snail.webgame.engine.common;

public class ErrorCode {

	/**
	 * 系统异常，请重启浏览器，并清空浏览器缓存，如问题还是存在，请联系客服人员。
	 */
	public static int SYSTEM_ERROR = 110000;

	/**
	 * 服务器尚未开放,请稍候登录
	 */
	public static int GAME_LOGIN_ERROR_1 = 110001;
	/**
	 * 登录失败,游戏服务器过于繁忙,请稍候登录
	 */
	public static int GAME_LOGIN_ERROR_2 = 110002;
	/**
	 * 登录失败,游戏服务器已达到最大在线人数
	 */
	public static int GAME_LOGIN_ERROR_3 = 110003;
	/**
	 * 登录失败，登录过于频繁，请稍候登录
	 */
	public static int LOGIN_FREQ_ERROR_1 = 110004;
	/**
	 * 登录失败，服务器处于维护中
	 */
	public static int GATE_SERVER_ERROR_1 = 110005;
	/**
	 * 其他用户登录该帐号，您已被踢出游戏。
	 */
	public static int USER_LOGIN_ERROR_2 = 110006;

	/**
	 * 系统正在维护，请到游戏官网查看维护公告，稍后登录。
	 */
	public static int USER_LOGIN_CHARGE_ERROR_1 = 110007;
	/**
	 * 用户验证失效，请重新登录。
	 */
	public static int USER_LOGIN_ERROR_3 = 110008;
	/**
	 * 非法请求
	 */
	public static int ERROR_REQUEST = 110009;
	
	/**
	 * 服务器断开,请重新登录
	 */
	public static int GAME_LOGIN_ERROR_6 = 110014;

	//----------聊天、邮件--------------//
	/**
	 * 收件人不存在。
	 */
	public static int MAIL_RECEIVER_NOEXIST = 10101;

	/**
	 * 对方邮箱空间不足，无法接收新邮件。
	 */
	public static int MAILSPACE_NOT_ENOUGH = 10102;
	/**
	 * 保存邮件失败，请重启浏览器，并清空浏览器缓存，如问题还是存在，请联系客服人员。
	 */
	public static int SAVE_MAIL_FAILED = 10103;
	/**
	 * 该邮件不存在。
	 */
	public static int MAIL_NOT_EXIST = 10104;
	/**
	 * 删除邮件失败，请重启浏览器，并清空浏览器缓存，如问题还是存在，请联系客服人员。
	 */
	public static int DELETE_MAIL_ERROR = 10105;

	/**
	 * 设置邮件全部已读失败，请重启浏览器，并清空浏览器缓存，如问题还是存在，请联系客服人员。
	 */
	public static int SET_ALLMAILREAD_ERROR = 10106;

	/**
	 * 您的邮箱快要满了，请注意清理，否则可能无法接收到新邮件
	 */
	public static int MAIL_NUM_ERROR_0 = 10107;
	/**
	 * 您的邮箱已经满了，请注意清理，否则可能无法接收到新邮件
	 */
	public static int MAIL_NUM_ERROR_1 = 10108;

	// ---聊天end

}
