package com.snail.webgame.engine.gate.send.manage;

 
import java.net.InetSocketAddress;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.cache.ServerMap;
import com.snail.webgame.engine.gate.common.SocketConfig;
import com.snail.webgame.engine.gate.config.GlobalServer;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.util.MessageServiceManage;
import com.spring.common.ServerName;

public class SendProtocolHandler extends IoHandlerAdapter {
	
	private static final Logger log =LoggerFactory.getLogger("logs");
	private MessageServiceManage msgmgt = new MessageServiceManage();
	 public void sessionOpened(IoSession session) throws Exception 
	 {
			if (session.getTransportType() == TransportType.SOCKET) {
				SocketConfig socketConfig = WebGameConfig.getInstance().getScoketConfig();

				if (socketConfig != null) {
					 
						((SocketSessionConfig) session.getConfig())
								.setSendBufferSize(socketConfig.getSocketSendBuffer());
			
						((SocketSessionConfig) session.getConfig())
								.setReceiveBufferSize(socketConfig.getSocketReceiveBuffer());
				
						((SocketSessionConfig) session.getConfig())
								.setKeepAlive(socketConfig.isKeepAlive());
						 
						((SocketSessionConfig) session.getConfig())
								.setTcpNoDelay(socketConfig.isTcpNoDelay());
					}
				}
	 }

	 public void sessionClosed(IoSession session) throws Exception {
		 
		 InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
			String ip = address.getAddress().getHostAddress();
			String port = String.valueOf(address.getPort());
			
			//与其中某个服务器断开连接 ，断开与列表服务器的连接
			if(session.getAttribute("serverName")!=null)
			{
				String serverName = (String) session.getAttribute("serverName");
				
	
				if(serverName.equalsIgnoreCase(ServerName.GAME_SERVER_NAME))
				{
					GlobalServer.GAME_IS_REGISTER = false;
					
				}
				 
				ServerMap.removeSession(serverName);
				if(session!=null&&session.isConnected())
				{
					log.warn("SendProtocolHandler : remote server close session");
					session.close();
				}
	
				
				
			}
			
//			 if(log.isErrorEnabled())
//			 {
//				 log.error(serverName+" is be interrupted (IP:"+ip+" port:"+port+")!");
//			 }
	 }

 
	 
	 public void messageReceived(IoSession session, Object message)
     throws Exception 
     {
		 
		if(message instanceof byte[])
		{
			 
//				ThreadPoolManager.getInstance().Pool(serverName).
//					execute( new RecMsgMgtRunnable((byte[]) message,msgmgt) );
			RecMsgMgtRunnable.sendMsg((byte[]) message, msgmgt);
		}
	 }
	 
	 public void exceptionCaught(IoSession session, Throwable e)
     throws Exception {
		
		 
		 
	 }
}
