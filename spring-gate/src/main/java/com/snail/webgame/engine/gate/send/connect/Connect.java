package com.snail.webgame.engine.gate.send.connect;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.common.ConnectConfig;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.send.code.SendCodeFactory;

public class Connect {

	private static final Logger log = LoggerFactory.getLogger("logs");

	public static IoSession connectServer(String serverName, IoHandlerAdapter handlerAdapter) {

		Map<String, ConnectConfig> map = WebGameConfig.getInstance().getConnectConfig();

		ConnectConfig connectConfig = map.get(serverName);

		if (connectConfig == null) {
			if (log.isWarnEnabled()) {
				log.warn("It is not exit server:" + serverName);
			}
			return null;
		}
		SocketConnectorConfig config = new SocketConnectorConfig();
		IoSession session = null;

		config.setThreadModel(ThreadModel.MANUAL);
		DefaultIoFilterChainBuilder chain = config.getFilterChain();
		ByteBuffer.setUseDirectBuffers(false);
		ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

		chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		chain.addLast("codec", new ProtocolCodecFilter(new SendCodeFactory()));

		try {
			SocketConnector connector = new SocketConnector(1, Executors.newCachedThreadPool());
			ConnectFuture future1 = connector.connect(
					new InetSocketAddress(connectConfig.getServerIP(), connectConfig.getServerPort()), handlerAdapter,
					config);
			future1.join();
			session = future1.getSession();
			if (log.isInfoEnabled()) {
				log.info("Connect " + connectConfig.getServerName() + " success!IP:" + connectConfig.getServerIP()
						+ ",Port:" + connectConfig.getServerPort());
			}

			return session;
		} catch (Exception e) {

			if (log.isErrorEnabled()) {
				log.error("Connect " + connectConfig.getServerName() + " failure!IP:" + connectConfig.getServerIP()
						+ ",Port:" + connectConfig.getServerPort());
			}
			return null;
		}
	}
}
