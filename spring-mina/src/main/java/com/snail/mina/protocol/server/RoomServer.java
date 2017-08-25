package com.snail.mina.protocol.server;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.code.CommandDecode;
import com.snail.mina.protocol.code.CommandEncode;
import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.handler.InnerServerHandle;
import com.snail.mina.protocol.handler.ServerHandle;
import com.snail.mina.protocol.handler.SessionStateHandler;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.processor.IProcessor;
import com.snail.mina.protocol.thread.ServerStateThread;
import com.snail.mina.protocol.util.RoomValue;

/**
 * 
 * 类介绍: 监听服务器启动类
 *
 * @author zhoubo
 * @since 2014-10-23
 */
public class RoomServer {
	private static Logger logger = LoggerFactory.getLogger("room");
	
	private SocketAcceptor acceptor;
	private SocketAcceptor innerAcceptor;
	public long startTime = 0;
	
	private Map<String, IoFilter> filterMap = new ConcurrentHashMap<String, IoFilter>();

	public RoomServer() {
		ByteBuffer.setUseDirectBuffers(false);
		ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
	}

	/**
	 * 启动监听服务器
	 * 
	 * @param serverIp	服务器IP
	 * @param serverPort	服务器端口
	 * @param socketThreads	服务器可使用CPU核数
	 * @param sessionStateHandler 连接状态回调接口
	 * @param handlerExecutor 服务器接收消息的处理线程池，此线程池调用由{@link IProcessor}processor()方法开始
	 *            如不设置此值，将由接收线程直接处理，不建议置空
	 */
	public synchronized void start(String serverIp, int serverPort, int socketThreads, SessionStateHandler sessionStateHandler, Executor handlerExecutor, boolean useMonitor) {
		try {
			startTime = 0;
			
			if (acceptor == null) {
				acceptor = new SocketAcceptor(socketThreads, Executors.newCachedThreadPool());
			}
			
			SocketAcceptorConfig acceptorConfig = acceptor.getDefaultConfig();
			acceptorConfig.setThreadModel(ThreadModel.MANUAL);
			acceptorConfig.getSessionConfig().setReuseAddress(true);
			acceptorConfig.getSessionConfig().setTcpNoDelay(true);

			DefaultIoFilterChainBuilder chainBuilder = acceptorConfig.getFilterChain();
			chainBuilder.addLast("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode()));
			
			if (filterMap != null && filterMap.size() > 0) {
				Set<Entry<String, IoFilter>> set = filterMap.entrySet();
				
				for (Entry<String, IoFilter> entry : set) {
					chainBuilder.addLast(entry.getKey(), entry.getValue());
				}
			}
			
			chainBuilder.addLast("messagecodec", new MessageCodecFilter("GameServer"));

			acceptor.bind(new InetSocketAddress(serverIp, serverPort), new ServerHandle(sessionStateHandler, handlerExecutor));
		
			if (useMonitor) {
				RoomValue.USE_SERVER_STATE_MONITOR = useMonitor;
				ServerStateThread sst = new ServerStateThread();
				sst.start();
			}
			
			logger.info("Server started ," + "HOST : " + serverIp + " PORT : " + serverPort);
		
			startTime = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("start server error", e);
			System.exit(0);
		}
	}
	
	public synchronized void start(String serverIp, int serverPort, int socketThreads, IoHandler ioHandler, Executor handlerExecutor, boolean useMonitor, List<RoomFilterInfo> ioFilterList) {
		try {
			startTime = 0;
			
			if (acceptor == null) {
				acceptor = new SocketAcceptor(socketThreads, Executors.newCachedThreadPool());
			}
			
			SocketAcceptorConfig acceptorConfig = acceptor.getDefaultConfig();
			acceptorConfig.setThreadModel(ThreadModel.MANUAL);
			acceptorConfig.getSessionConfig().setReuseAddress(true);
			acceptorConfig.getSessionConfig().setTcpNoDelay(true);

			DefaultIoFilterChainBuilder chainBuilder = acceptorConfig.getFilterChain();
			
			for (RoomFilterInfo roomFilterInfo : ioFilterList) {
				if (roomFilterInfo.getName() != null && roomFilterInfo.getIoFilter() != null) {
					chainBuilder.addLast(roomFilterInfo.getName(), roomFilterInfo.getIoFilter());
				}
			}
			
			acceptor.bind(new InetSocketAddress(serverIp, serverPort), ioHandler);
		
			if (useMonitor) {
				RoomValue.USE_SERVER_STATE_MONITOR = useMonitor;
				ServerStateThread sst = new ServerStateThread();
				sst.start();
			}
			
			logger.info("Server started ," + "HOST : " + serverIp + " PORT : " + serverPort);
		
			startTime = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("start server error", e);
			System.exit(0);
		}
	}
	
	public synchronized void start(String serverIp, int serverPort, int socketThreads, IoHandler ioHandler, boolean useMonitor, List<RoomFilterInfo> ioFilterList) {
		try {
			startTime = 0;
			
			if (acceptor == null) {
				acceptor = new SocketAcceptor(socketThreads, Executors.newCachedThreadPool());
			}
			
			SocketAcceptorConfig acceptorConfig = acceptor.getDefaultConfig();
			acceptorConfig.setThreadModel(ThreadModel.MANUAL);
			acceptorConfig.getSessionConfig().setReuseAddress(true);
			acceptorConfig.getSessionConfig().setTcpNoDelay(true);

			DefaultIoFilterChainBuilder chainBuilder = acceptorConfig.getFilterChain();
			
			for (RoomFilterInfo roomFilterInfo : ioFilterList) {
				if (roomFilterInfo.getName() != null && roomFilterInfo.getIoFilter() != null) {
					chainBuilder.addLast(roomFilterInfo.getName(), roomFilterInfo.getIoFilter());
				}
			}
			
			acceptor.bind(new InetSocketAddress(serverIp, serverPort), ioHandler);
		
			if (useMonitor) {
				RoomValue.USE_SERVER_STATE_MONITOR = useMonitor;
				ServerStateThread sst = new ServerStateThread();
				sst.start();
			}
			
			logger.info("Server started ," + "HOST : " + serverIp + " PORT : " + serverPort);
		
			startTime = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("start server error", e);
			System.exit(0);
		}
	}
	
	public synchronized void innerServerStart(String serverIp, int serverPort, SessionStateHandler sessionStateHandler) {
		try {
			if (innerAcceptor == null) {
				innerAcceptor = new SocketAcceptor();
			}
			
			SocketAcceptorConfig acceptorConfig = innerAcceptor.getDefaultConfig();
			acceptorConfig.setThreadModel(ThreadModel.MANUAL);
			acceptorConfig.getSessionConfig().setReuseAddress(true);

			DefaultIoFilterChainBuilder chainBuilder = acceptorConfig.getFilterChain();
			chainBuilder.addLast("codec", new ProtocolCodecFilter(new CommandEncode(), new CommandDecode()));

			innerAcceptor.bind(new InetSocketAddress(serverIp, serverPort), new InnerServerHandle(sessionStateHandler));
		
			logger.info("Server started ," + "HOST : " + serverIp + " PORT : " + serverPort);
		} catch (Exception e) {
			logger.error("start inner server error", e);
		}
	}
	
	public void addMyIOFilter(String name, IoFilter ioFilter) {
		if (name != null && !"".equals(name) && ioFilter != null) {
			filterMap.put(name, ioFilter);
		}
	}

	/**
	 * 解除绑定，由关闭回调线程调用
	 */
	public synchronized void unbind() {
		if (acceptor != null) {
			acceptor.unbindAll();
		}
		
		if (innerAcceptor != null) {
			innerAcceptor.unbindAll();
		}
	}
	
	
}
