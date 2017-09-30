package com.snail.mina.protocol.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.handler.SessionStateHandler;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.processor.IProcessor;

/**
 * 
 * 类介绍:客户端连接类
 * 连接成功后由内部线程维持连接状态和断线重连，不需调用者额外控制连接
 * 如需关闭连接 调用此类的{@link shutdown} 方法
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RoomClient {

	/**
	 * 全局客户端连接线程 保存所有由此类创建的Socket连接，用于关闭连接使用
	 */
	private static Map<String, ClientThread> clientMap = new ConcurrentHashMap<String, ClientThread>();
	
	private static AtomicInteger ai = new AtomicInteger(1);
	
	/**
	 * 发起Socket连接到远程服务器
	 * 
	 * @param serverIp
	 *            服务器IP
	 * @param serverPort
	 *            服务器端口
	 * @param serverName
	 *            本地服务器名称
	 *            不同的客户端连接到相同的服务器，必需确保不同的客户端的此名称不一致，否则服务器无法确认客户端唯一性
	 * @param remoteServerName
	 *            远程服务器名称
	 * @param sessionStateHandler
	 *            连接状态回调接口
	 * @param handlerExecutor
	 *            客户端接收消息的处理线程池，此线程池调用由{@link IProcessor}processor()方法开始
	 *            如不设置此值，将由接收线程直接处理，不建议置空
	 * @throws Exception
	 *             连接异常
	 */
	public static synchronized void connect(String serverIp, int serverPort, String serverName, String remoteServerName, SessionStateHandler sessionStateHandler, Executor handlerExecutor)
			throws Exception {
		if (serverIp == null || "".equals(serverIp.trim())) {
			throw new Exception("serverIp is null");
		}

		if (serverPort <= 0 || serverPort > Short.MAX_VALUE) {
			throw new Exception("serverPort is wrong");
		}

		if (serverName == null || "".equals(serverName.trim())) {
			throw new Exception("serverName is null");
		}

		if (remoteServerName == null || "".equals(remoteServerName.trim())) {
			throw new Exception("remoteServerName is null");
		}

		if (RoomMessageConfig.serverMap.get(remoteServerName) != null && RoomMessageConfig.serverMap.get(remoteServerName).isConnected()) {
			throw new Exception("remote server " + remoteServerName + " is connecting");
		}

		if (clientMap.containsKey(remoteServerName)) {
			clientMap.get(remoteServerName).doCancel();
		}

		ClientThread clientThread = new ClientThread(serverIp, serverPort, serverName, remoteServerName, sessionStateHandler, handlerExecutor);
		clientMap.put(remoteServerName, clientThread);

		// 启动连接线程
		Thread t = new Thread(clientThread, "RoomClientActiveThread-" + ai.getAndIncrement());
		t.setName("ClientThread-" + remoteServerName);
		t.start();
	}
	
	public static synchronized void connect(String serverIp, int serverPort, String serverName, String remoteServerName, IoHandler ioHandler, List<RoomFilterInfo> list, boolean heartbeat, boolean register)
			throws Exception {
		if (serverIp == null || "".equals(serverIp.trim())) {
			throw new Exception("serverIp is null");
		}

		if (serverPort <= 0 || serverPort > Short.MAX_VALUE) {
			throw new Exception("serverPort is wrong");
		}

		if (serverName == null || "".equals(serverName.trim())) {
			throw new Exception("serverName is null");
		}

		if (remoteServerName == null || "".equals(remoteServerName.trim())) {
			throw new Exception("remoteServerName is null");
		}

		if (RoomMessageConfig.serverMap.get(remoteServerName) != null && RoomMessageConfig.serverMap.get(remoteServerName).isConnected()) {
			throw new Exception("remote server " + remoteServerName + " is connecting");
		}

		if (clientMap.containsKey(remoteServerName)) {
			clientMap.get(remoteServerName).doCancel();
		}

		ClientThread clientThread = new ClientThread(serverIp, serverPort, null, serverName, remoteServerName, ioHandler, list, heartbeat, register);
		clientMap.put(remoteServerName, clientThread);

		// 启动连接线程
		new Thread(clientThread, "RoomClientActiveThread-" + ai.getAndIncrement()).start();
	}
	
	public static synchronized void connect(String serverIp, int serverPort, String serverInnerIP, String serverName, String remoteServerName, IoHandler ioHandler, List<RoomFilterInfo> list, boolean heartbeat, boolean register)
			throws Exception {
		if (serverIp == null || "".equals(serverIp.trim())) {
			throw new Exception("serverIp is null");
		}
		
		if (serverInnerIP == null || "".equals(serverInnerIP.trim())) {
			throw new Exception("serverInnerIP is null");
		}

		if (serverPort <= 0 || serverPort > Short.MAX_VALUE) {
			throw new Exception("serverPort is wrong");
		}

		if (serverName == null || "".equals(serverName.trim())) {
			throw new Exception("serverName is null");
		}

		if (remoteServerName == null || "".equals(remoteServerName.trim())) {
			throw new Exception("remoteServerName is null");
		}

		if (RoomMessageConfig.serverMap.get(remoteServerName) != null && RoomMessageConfig.serverMap.get(remoteServerName).isConnected()) {
			throw new Exception("remote server " + remoteServerName + " is connecting");
		}

		if (clientMap.containsKey(remoteServerName)) {
			clientMap.get(remoteServerName).doCancel();
		}

		ClientThread clientThread = new ClientThread(serverIp, serverPort, serverInnerIP, serverName, remoteServerName, ioHandler, list, heartbeat, register);
		clientMap.put(remoteServerName, clientThread);

		// 启动连接线程
		new Thread(clientThread, "RoomClientActiveThread-" + ai.getAndIncrement()).start();
	}

	/**
	 * 关闭连接
	 * 
	 * @param serverName	远程服务器名称，对应connect方法的remoteServerName参数
	 */
	public static synchronized void shutdown(String serverName) {
		if (clientMap.containsKey(serverName)) {
			clientMap.remove(serverName).doCancel();
		}

		if (RoomMessageConfig.serverMap.containsKey(serverName)) {
			IoSession session = RoomMessageConfig.serverMap.remove(serverName);

			if (session != null && session.isConnected()) {
				session.close();
			}
		}
	}
	
	/**
	 * 发送消息到指定服务器
	 * 
	 * @param serverName	远程服务器名称
	 * @param message		消息信息
	 * @return	boolean true-发送成功 false-发送失败
	 */
	public static boolean sendMessage(String serverName, Message message) {
		ClientThread clientThread = clientMap.get(serverName);
		
		if (clientThread == null) {
			return false;
		} else {
			clientThread.sendMessage(message);
			return true;
		}
	}
	
	/**
	 * 取得连接远程服务器的次数
	 * 
	 * @param serverName	远程服务器名称
	 * @return	int 次数
	 */
	public static int getConectTimes(String serverName) {
		if (clientMap.containsKey(serverName)) {
			ClientThread clientThread = clientMap.get(serverName);
			
			return clientThread.getConnectTimes();
		}
		
		return -1;
	}
	
	public static boolean isConnected(String serverName) {
		if (clientMap.containsKey(serverName)) {
			return clientMap.get(serverName).isConnected();
		}
		
		return false;
	}
}
