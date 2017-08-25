package com.snail.webgame.game.thread;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.protocal.server.active.ServerActiveReq;

public class CheckGameThread extends Thread {

	private int flag = 0;// 0 刚启动 1 已经启动
	private volatile boolean cancel = false;

	public void run() {
		while (!cancel) {
			checkConnect(ServerName.MAIL_SERVER_NAME);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

	public void checkConnect(String serverName) {
		IoSession session = Client.getInstance().getSession(serverName);
		
		if (session != null && session.isConnected()) {

			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setMsgType(Command.GAME_SERVER_ACTIVE_REQ);
			message.setHeader(header);

			ServerActiveReq req = new ServerActiveReq();
			req.setServerName(GameConfig.getInstance().getServerName());
			req.setFlag(flag);
			message.setBody(req);
			// 发送连接维护信息
			session.write(message);

			if (flag == 0) {
				flag = 1;
			}
		}

	}

	public void cancel() {
		cancel = true;
	}
}
