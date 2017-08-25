package com.snail.webgame.game.charge.text;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.charge.text.code.RequestDecode;
import com.snail.webgame.game.charge.text.code.RequestEncoder;
import com.snail.webgame.game.charge.text.filter.AppProtocolCodecFilter;

/**
 * 
 * 类介绍:苹果充值验证客户端
 *
 * @author zhoubo
 * @2015年6月29日
 */
public class AppClientThread extends Thread {
	
	private static Logger logger = LoggerFactory.getLogger("logs");
	
	private volatile boolean cancel = false;
	
	private String serverIp;
	private int serverPort;
	private Executor handlerExecutor;
	
	private IoSession session;
	
	public AppClientThread(String serverIp, int serverPort, Executor handlerExecutor) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.handlerExecutor = handlerExecutor;
	}

	@Override
	public void run() {
		while (!cancel) {
			try {
				if (session != null && session.isConnected()) {
					// 连接成功
					// logger.info("connect to App charge server is OK");
				} else {
					session = connect(serverIp, serverPort, this.handlerExecutor);

					if (session != null && session.isConnected()) {
						// 连接成功
					} else {
						logger.info("connect to App charge server is Failed");
					}
				}

				try {
					if (session != null && session.isConnected()) {
						TimeUnit.SECONDS.sleep(10);
					} else {
						TimeUnit.SECONDS.sleep(1);
					}
				} catch (InterruptedException e) {
					if (logger.isErrorEnabled()) {
						logger.error("AppClientThread : ", e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("AppClientThread : unknow exception is occurred" , e.getMessage());
			}
		}
	}
	
	private IoSession connect(String remoteHost, int remotePort, Executor handlerExecutor) {
		SocketConnector connector = new SocketConnector(1, Executors.newCachedThreadPool());

		IoConnectorConfig clientConfig = connector.getDefaultConfig();
		clientConfig.setThreadModel(ThreadModel.MANUAL);
		
		DefaultIoFilterChainBuilder chainBuilder = clientConfig.getFilterChain();
		chainBuilder.addLast("codec", new AppProtocolCodecFilter(new RequestEncoder(), new RequestDecode()));

		IoSession session = null;
		ConnectFuture cf = null;

		if (remoteHost.length() == 0 || remotePort == 0) {
			return null;
		}

		AppClientHandler clientHandle = new AppClientHandler(handlerExecutor);

		try {
			cf = connector.connect(new InetSocketAddress(remoteHost, remotePort), clientHandle);
			
			if (cf != null) {
				cf.join(3000);
			}
			
			if (cf != null && cf.isConnected()) {
				if (logger.isInfoEnabled()) {
					logger.info("connect App server success (IP:" + remoteHost + ",port:" + remotePort + ")!");
				}
				session = cf.getSession();
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("connect App server failure (IP:" + remoteHost + ",port:" + remotePort + ")!");
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
		}

		return session;
	}
	
	public void cancel() {
		cancel = true;
	}

	public IoSession getSession() {
		return session;
	}
	
	
}
