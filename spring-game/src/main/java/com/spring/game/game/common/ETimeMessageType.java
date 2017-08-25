package com.snail.webgame.game.common;

/**
 * 批量事务类型枚举
 * 
 * @author xiasd
 *
 */
public enum ETimeMessageType {
	/**
	 * 系统发送多角色相同邮件
	 */
	SEND_BATCH_SYS_MAIL,
	/**
	 * 系统发送指定多人不同邮件
	 */
	SEND_BATCH_DIFF_MAIL,
	/**
	 * 批量处理日志
	 */
	EXECUTE_BATCH_LOGS
}
