package com.snail.webgame.game.charge.text;

import java.util.List;
import java.util.concurrent.Executor;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppClientHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private Executor executor;
	
	AppClientHandler() {
	}
	
	AppClientHandler(Executor executor) {
		this.executor = executor;
	}

	public void messageReceived(IoSession session, Object in) {
		if (in instanceof List) {
			AppService service = new AppService((List<Object>)in);
			
			if (this.executor != null) {
				this.executor.execute(service);
			} else {
				service.run();
			}
		} else {
			logger.error("AppClientHandler error : receive message is not byte[]");
		}
	}
}
