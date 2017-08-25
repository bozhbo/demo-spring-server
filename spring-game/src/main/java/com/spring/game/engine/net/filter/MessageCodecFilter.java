package com.snail.webgame.engine.net.filter;


import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.IRoomHead;
import com.snail.webgame.engine.game.base.cache.MessageTypeMap;
import com.snail.webgame.engine.game.base.info.common.MessageTypeInfo;
import com.snail.webgame.engine.net.msg.impl.BaseMessage;
import com.snail.webgame.engine.net.msg.impl.GameMessageHead;

/**
 * 
 * 类介绍:消息编解码类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class MessageCodecFilter extends IoFilterAdapter {
	
	private static Logger logger = LoggerFactory.getLogger("room");
	
	private String serviceName;
	private ByteOrder messageByteOrder = null;

	public MessageCodecFilter(String serviceName, ByteOrder messageByteOrder) {
		if (messageByteOrder == null) {
			this.messageByteOrder = ByteOrder.BIG_ENDIAN;
		} else {
			this.messageByteOrder = messageByteOrder;
		}
		
		this.serviceName = serviceName;
	}

	public void sessionCreated(NextFilter nextFilter, IoSession session) {

		nextFilter.sessionCreated(session);
	}

	public void sessionOpened(NextFilter nextFilter, IoSession session) {

		nextFilter.sessionOpened(session);
	}

	public void sessionClosed(NextFilter nextFilter, IoSession session) {
		nextFilter.sessionClosed(session);
	}

	public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) {

		nextFilter.sessionIdle(session, status);
	}

	public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) {

		nextFilter.exceptionCaught(session, cause);
	}

	public void messageReceived(NextFilter nextFilter, IoSession session, Object in) {
		if (in instanceof byte[]) {
			ByteBuffer buffer = null;
			
			try {
				buffer = ByteBuffer.allocate(((byte[]) in).length);
				buffer.put((byte[]) in);
				buffer.position(0);
				
				IRoomHead roomHead = null;
				
				if (serviceName.equals("GameServer")) {
					roomHead = new GameMessageHead();
					roomHead.bytes2Head(buffer, this.messageByteOrder);
				}
				
				BaseMessage message = new BaseMessage();
				message.setiRoomHead(roomHead);
				
				MessageTypeInfo messageTypeInfo = MessageTypeMap.getMessageTypeInfo(roomHead.getMsgType());
				
				if (messageTypeInfo.getProcessor() != null) {
					IRoomBody messageBody = messageTypeInfo.getProcessor().getReq().newInstance();
					messageBody.bytes2Req(buffer, this.messageByteOrder);
					message.setiRoomBody(messageBody);
				}
				
				message.setSession(session);
				message.setRecReqMsgTime(System.currentTimeMillis());

				nextFilter.messageReceived(session, message);
			} catch (Exception e) {
				logger.error("handler request error", e);
			} finally {
				if (buffer != null) {
					buffer.release();
				}
			}
		}
	}

	public void messageSent(NextFilter nextFilter, IoSession session, Object message) {
		nextFilter.messageSent(session, message);
	}

	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
		ByteBuffer buffer = null;
		
		if (writeRequest.getMessage() instanceof BaseMessage) {
			BaseMessage message = (BaseMessage) writeRequest.getMessage();
			
			buffer = ByteBuffer.allocate(128, false);
			buffer.setAutoExpand(true);

			buffer.putInt(0);// 设置长度

			message.getiRoomHead().head2Bytes(buffer, this.messageByteOrder);
			
			if (message.getiRoomBody() != null) {
				message.getiRoomBody().resp2Bytes(buffer, this.messageByteOrder);
			}
			
			buffer.putInt(0, buffer.position() - 4);
			buffer.flip();
		}

		if (buffer != null) {
			nextFilter.filterWrite(session, new WriteRequest(buffer));
		}
	}

	public void filterClose(NextFilter nextFilter, IoSession session) throws Exception {
		nextFilter.filterClose(session);
	}
}
