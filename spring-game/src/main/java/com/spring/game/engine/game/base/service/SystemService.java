package com.snail.webgame.engine.game.base.service;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.game.base.info.msg.ServerActiveReq;

/**
 * 类介绍:系统功能处理类 可注入到其他Service或者BaseAction中使用
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class SystemService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void active(ServerActiveReq req, IoSession session) {
		String serverName = req.getServerName();

		if (serverName == null || serverName.trim().length() == 0) {
			return;
		}

		if (req.getFlag() == 0) {
			// 通讯服务器重启，对应用户下线
			String str[] = serverName.split("-");

			if (str != null && str.length > 0) {
				// byte gateServerId = Byte.valueOf(str[1]);

				if (logger.isErrorEnabled()) {
					logger.error("Gate server shut down");
				}
			} else {
				return;
			}
		}

		// ServerMap.addServer(serverName, session);
		session.setAttribute("serverName", serverName);
	}

	/**
	 * 发送消息到相关服务器
	 * 
	 * @param serverName 服务器名称,参考function-config.xml中的head标签
	 * @param message 消息对象
	 */
	// public boolean sendServerMessage(String serverName, Message message) {
	// IoSession session = ServerMap.getServerSession(serverName);
	//
	// if (session != null && session.isConnected()) {
	// session.write(message);
	// return true;
	// } else {
	// return false;
	// }
	// }
}
