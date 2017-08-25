package com.snail.mina.protocol.info;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.error.ErrorResp;
import com.snail.mina.protocol.util.RoomValue;

/**
 * 
 * 类介绍:消息类
 * 用于表示传输消息的对象管理类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class Message implements Runnable {
	private Logger logger = LoggerFactory.getLogger("room");
	
	/**
	 * 消息头
	 */
	private IRoomHead iRoomHead;
	
	/**
	 * 消息体
	 */
	private IRoomBody iRoomBody;
	
	/**
	 * 消息Session
	 */
	private IoSession session;
	
	/**
	 * 消息请求时间
	 */
	private long recReqMsgTime;
	
	/**
	 * 服务器名称
	 */
	private String serverName;
	
	public IRoomHead getiRoomHead() {
		return iRoomHead;
	}
	public void setiRoomHead(IRoomHead iRoomHead) {
		this.iRoomHead = iRoomHead;
	}
	public IRoomBody getiRoomBody() {
		return iRoomBody;
	}
	public void setiRoomBody(IRoomBody iRoomBody) {
		this.iRoomBody = iRoomBody;
	}
	public IoSession getSession() {
		return session;
	}
	public void setSession(IoSession session) {
		this.session = session;
	}
	public long getRecReqMsgTime() {
		return recReqMsgTime;
	}
	public void setRecReqMsgTime(long recReqMsgTime) {
		this.recReqMsgTime = recReqMsgTime;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * 发送错误码
	 * 
	 * @param errorCode
	 */
	public void sendErrorResp(int errorCode) {
		if (session != null && session.isConnected()) {
			if (iRoomHead == null) {
				iRoomHead = new RoomMessageHead();
			}
			
			iRoomHead.setMsgType(RoomValue.MESSAGE_TYPE_ERROR_CODE_FF02);// FF02
			iRoomBody = new ErrorResp(errorCode);
			
			try {
				session.write(this);
			} catch (Exception e) {
				logger.error("Message : session write error", e);
			}
		}
	}
	
	/**
	 * 发送消息
	 * 
	 * @return int 1-成功 0-失败
	 */
	public int sendMessage() {
		if (session != null && session.isConnected()) {
			try {
				session.write(this);
				return 1;
			} catch (Exception e) {
				logger.error("Message : session write error", e);
			}
		}
		
		return 0;
	}
	
	/**
	 * 发送消息
	 * 
	 * @return int 1-成功 0-失败
	 */
	public int sendServerMessage() {
		try {
			if (this.serverName == null || "".equals(this.serverName) || this.iRoomHead == null) {
				logger.error("Message : sendMessage error, serverName is null or iRoomHead is null");
				return 0;
			}
			
			if (RoomMessageConfig.serverMap.containsKey(this.serverName)) {
				IoSession session = RoomMessageConfig.serverMap.get(this.serverName);
				
				if (session != null && session.isConnected()) {
					try {
						this.session = session;
						this.session.write(this);
						
						return 1;
					} catch (Exception e) {
						logger.error("Message : session write error", e);
					}
				}
			}
			
			return 0;
		} catch (Exception e) {
			logger.error("Message : write message error", e);
			return 0;
		}
	}
	
	@Override
	public void run() {
		try {
			if (this.getiRoomHead().getMsgType() > 0) {
				RoomMessageConfig.processorMap.get(this.getiRoomHead().getMsgType()).processor(this);
			}
		} catch (Exception e) {
			logger.error("Message : exec processor error, MsgType = " + Integer.toHexString(this.getiRoomHead().getMsgType()), e);
		}
	}
}
