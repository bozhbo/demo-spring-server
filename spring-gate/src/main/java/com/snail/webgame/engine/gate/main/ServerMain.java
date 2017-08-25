package com.snail.webgame.engine.gate.main;

import com.snail.webgame.engine.gate.receive.manage.ConnectProtocolHandler;
import com.snail.webgame.engine.gate.receive.manage.Listener;
import com.snail.webgame.engine.gate.send.manage.SendProtocolHandler;
import com.snail.webgame.engine.gate.thread.CloseSessionThread;
import com.snail.webgame.engine.gate.util.MessageServiceManage;

public class ServerMain {

	public static void main(String args[]) {
		MessageServiceManage msgmgt = new MessageServiceManage();
		
		// TODO License is ignore
		// CertConfig.getInstance(WebGameConfig.getInstance().getLicensePath()).isPass()
		
		if (Listener.listener(new ConnectProtocolHandler(msgmgt))) {
			// 服务器启动监听
			
			CheckConnectThread checkThread = new CheckConnectThread(new SendProtocolHandler());
			checkThread.start();

			// TODO License is ignore
//			CertThread certThread = new CertThread();
//			certThread.start();

			CloseSessionThread closeThread = new CloseSessionThread(msgmgt);
			closeThread.start();
			
			Runtime.getRuntime().addShutdownHook(new ShutDownThread());
		}
	}
}
