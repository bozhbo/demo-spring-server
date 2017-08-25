package com.snail.mina.protocol.processor.register;

import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.handler.ServerHandle;
import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.BaseProcessor;
import com.snail.mina.protocol.util.RoomValue;

/**
 * 
 * 类介绍:服务器注册操作
 * 所有连接到服务器的客户端必需发送注册操作，便于服务器主动推送消息到指定客户端
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RegisterProcessor extends BaseProcessor {
	
	private static Logger logger = LoggerFactory.getLogger("room");
	
	public RegisterProcessor() {
		super();
	}
	
	public RegisterProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		try {
			RegisterReq registerReq = (RegisterReq)message.getiRoomBody();
			
			if (registerReq.getServerName() == null || "".equals(registerReq.getServerName())) {
				if (logger.isWarnEnabled()) {
					logger.warn("RegisterProcessor : register serverName is null, client = " + message.getSession().getRemoteAddress().toString());
				}
			} else {
				IoSession session = RoomMessageConfig.serverMap.get(registerReq.getServerName());
				
				if (session != null && session.isConnected()) {
					if (logger.isWarnEnabled()) {
						logger.warn("RegisterProcessor : server " + registerReq.getServerName() + " was registered,please change serverName, using server = " + session.getRemoteAddress().toString());	
					}
					
					message.getSession().close();
					return;
				}
				
				logger.info("RegisterProcessor : server " + registerReq.getServerName() + " is registered, reserve = " + registerReq.getReserve());
				
				message.getSession().setAttribute("serverName", registerReq.getServerName());
				
				if (registerReq.getReserve() != null && !"".equals(registerReq.getReserve().trim())) {
					IoHandler handler = message.getSession().getHandler();
					
					if (handler != null && handler instanceof ServerHandle) {
						ServerHandle serverhandler = (ServerHandle)handler;
						
						if (serverhandler.getIoHandler() != null) {
							serverhandler.getIoHandler().registerEnd(message.getSession(), registerReq.getReserve());
						}
					}
				}
				
				RoomMessageConfig.addIoSession(registerReq.getServerName(), message.getSession());
				
				// 注册返回
//				message.getiRoomHead().setMsgType(RoomValue.MESSAGE_TYPE_REGISTER_FE02);
//				RegisterResp registerResp = new RegisterResp();
//				registerResp.setStartTime(System.currentTimeMillis() - RoomServer.startTime);
//				message.setiRoomBody(registerResp);
			}
		} catch (Exception e) {
			logger.error("RegisterProcessor : register error", e);
		}
	}
	
	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_REGISTER_FE01;
	}
}
