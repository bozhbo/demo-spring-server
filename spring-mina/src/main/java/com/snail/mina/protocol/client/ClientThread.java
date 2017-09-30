package com.snail.mina.protocol.client;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.handler.ClientHandle;
import com.snail.mina.protocol.handler.SessionStateHandler;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.register.RegisterReq;
import com.snail.mina.protocol.util.RoomValue;

/**
 * 
 * 类介绍:客户端Socket维护线程，自动重连
 * 
 * @author zhoubo
 * @since 2014-11-19
 */
public class ClientThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger("room");

	private volatile boolean cancel = false;
	private volatile boolean isConnected = false;
	private String serverIp;
	private String serverInnerIp;
	private int serverPort;
	private String serverName;
	private String remoteServerName;
	private SessionStateHandler sessionStateHandler;
	private IoHandler ioHandler;
	private Executor handlerExecutor;
	private List<RoomFilterInfo> ioFiterList;
	private IoSession session = null;
	
	private SocketConnector connector;
	
	private boolean heartbeat = true;
	private boolean register = true;
	
	/**
	 * 连接远程服务器次数
	 */
	private int connectTimes = 0;

	private long begTime = System.currentTimeMillis();
	
	private byte[] b = new byte[0];
	
	public ClientThread(String serverIp, int serverPort, String serverInnerIp, String serverName, String remoteServerName, IoHandler ioHandler, List<RoomFilterInfo> ioFiterList, boolean heartbeat, boolean register) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverInnerIp = serverInnerIp;
		this.serverName = serverName;
		this.remoteServerName = remoteServerName;
		this.ioHandler = ioHandler;
		
		if (this.ioHandler instanceof ClientHandle) {
			this.sessionStateHandler = ((ClientHandle)this.ioHandler).getIoHandler();
			this.handlerExecutor = ((ClientHandle)this.ioHandler).getExecutor();
		}
		
		this.ioFiterList = ioFiterList;
		
		this.heartbeat = heartbeat;
		this.register = register;
		
	}

	public ClientThread(String serverIp, int serverPort, String serverName, String remoteServerName, SessionStateHandler sessionStateHandler, Executor handlerExecutor) {
		this(serverIp, serverPort, serverName, remoteServerName, sessionStateHandler, handlerExecutor, null);
	}
	
	public ClientThread(String serverIp, int serverPort, String serverName, String remoteServerName, SessionStateHandler sessionStateHandler, Executor handlerExecutor, List<RoomFilterInfo> ioFiterList) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverName = serverName;
		this.remoteServerName = remoteServerName;
		this.ioHandler = null;
		this.sessionStateHandler = sessionStateHandler;
		this.handlerExecutor = handlerExecutor;
		this.ioFiterList = ioFiterList;
	}

	@Override
	public void run() {
		while (!cancel) {
			try {
				session = RoomMessageConfig.serverMap.get(this.remoteServerName);

				if (session != null && session.isConnected()) {
					isConnected = true;
					
					if (this.heartbeat && (System.currentTimeMillis() - begTime) > 5000) {
						// 心跳维持
						Message message = new Message();
						RoomMessageHead roomMessageHead = new RoomMessageHead();
						roomMessageHead.setMsgType(RoomValue.MESSAGE_TYPE_HEARTBEAT_FF01);
						message.setiRoomHead(roomMessageHead);
						message.setServerName(this.remoteServerName);
						message.sendServerMessage();

						begTime = System.currentTimeMillis();
					}
				} else {
					isConnected = false;
					
					session = connect();

					if (session != null && session.isConnected()) {
						// 连接成功发送注册消息
						if (this.register) {
							Message message = new Message();
							RoomMessageHead roomMessageHead = new RoomMessageHead();
							roomMessageHead.setMsgType(RoomValue.MESSAGE_TYPE_REGISTER_FE01);
							message.setiRoomHead(roomMessageHead);
							RegisterReq req = new RegisterReq();
							req.setServerName(this.serverName);
							
							if (sessionStateHandler != null) {
								req.setReserve(sessionStateHandler.getRegisterReserve());
							}
							
							message.setiRoomBody(req);
							session.write(message);
						}

						session.setAttribute("serverName", this.remoteServerName);
						RoomMessageConfig.addIoSession(this.remoteServerName, session);
						
						connectTimes++;
					}
				}

				try {
					if (session != null && session.isConnected()) {
						synchronized (b) {
							b.wait(6000);
						}
					} else {
						synchronized (b) {
							b.wait(1000);
						}
					}
				} catch (InterruptedException e) {
					if (logger.isErrorEnabled()) {
						logger.error("ClientThread : ", e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("ClientThread : unknow exception is occurred" , e.getMessage());
			}
		}
	}

	/**
	 * Socket连接发起
	 * 
	 * @param remoteHost
	 *            远程地址
	 * @param remotePort
	 *            远程端口
	 * @param sessionStateHandler
	 *            连接事件回调接口
	 * @return IoSession 连接Session
	 */
	private IoSession connect() {
		if (this.serverIp.length() == 0 || this.serverPort == 0) {
			return null;
		}

		if (this.ioHandler == null) {
			this.ioHandler = new ClientHandle(sessionStateHandler, handlerExecutor);
		}
		
		IoSession session = null;
		ConnectFuture cf = null;
		
		if (this.connector == null) {
			connector = new SocketConnector(4, Executors.newCachedThreadPool());
			
			IoConnectorConfig clientConfig = connector.getDefaultConfig();
			clientConfig.setThreadModel(ThreadModel.MANUAL);
			
			DefaultIoFilterChainBuilder chainBuilder = clientConfig.getFilterChain();
			
			if (this.ioFiterList == null || this.ioFiterList.size() == 0) {
				chainBuilder.addLast("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode()));
				chainBuilder.addLast("messagecodec", new MessageCodecFilter("GameServer"));
			} else {
				for (RoomFilterInfo roomFilterInfo : this.ioFiterList) {
					if (roomFilterInfo.getName() != null && roomFilterInfo.getIoFilter() != null) {
						chainBuilder.addLast(roomFilterInfo.getName(), roomFilterInfo.getIoFilter());
					}
				}
			}
		}

		try {
			if (this.serverInnerIp == null || "".equals(serverInnerIp)) {
				cf = connector.connect(new InetSocketAddress(this.serverIp, this.serverPort), this.ioHandler);
			} else {
				cf = connector.connect(new InetSocketAddress(this.serverIp, this.serverPort), new InetSocketAddress(this.serverInnerIp, 0), this.ioHandler);
			}
			
			
			if (cf != null) {
				cf.join(3000);
			}
			
			if (cf != null && cf.isConnected()) {
				if (logger.isInfoEnabled()) {
					logger.info("connect server " + remoteServerName + " success (IP:" + this.serverIp + ",port:" + this.serverPort + ")!");
				}
				session = cf.getSession();
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("connect server " + remoteServerName + " failure (IP:" + this.serverIp + ",port:" + this.serverPort + ")!");
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
		}

		return session;
	}
	
	public void sendMessage(Message message) {
		if (this.session != null && this.session.isConnected()) {
			this.session.write(message);
		}
	}

	/**
	 * 停止连接维护线程
	 */
	public void doCancel() {
		cancel = true;
		
		synchronized (b) {
			try {
				b.notifyAll();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	public int getConnectTimes() {
		return connectTimes;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
}
