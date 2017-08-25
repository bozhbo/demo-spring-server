package com.snail.mina.protocol.filter;


import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.thread.ServerStateThread;
import com.snail.mina.protocol.util.RoomValue;

/**
 * 
 * 类介绍:消息编解码类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class MessageCodecFilter extends IoFilterAdapter {
	
	private Logger logger = LoggerFactory.getLogger("room");
	
	private ByteOrder messageByteOrder = null;
	private String serverName = null;

	public MessageCodecFilter(String serverName) {
		this.messageByteOrder = ByteOrder.BIG_ENDIAN;
		this.serverName = serverName;
	}
	
	public MessageCodecFilter(String serverName, ByteOrder byteOrder) {
		if (byteOrder == null) {
			this.messageByteOrder = ByteOrder.BIG_ENDIAN;
		} else {
			this.messageByteOrder = byteOrder;
		}
		this.serverName = serverName;
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
				
				RoomMessageHead roomMessageHead = new RoomMessageHead();
				roomMessageHead.bytes2Head(buffer, this.messageByteOrder);
				
				Message message = new Message();
				message.setiRoomHead(roomMessageHead);
				message.setiRoomBody(RoomMessageConfig.getRoomBody(roomMessageHead.getMsgType(), buffer, this.messageByteOrder));
				message.setSession(session);
				message.setRecReqMsgTime(System.currentTimeMillis());

				nextFilter.messageReceived(session, message);
			} catch (Exception e) {
				logger.error("handler request error", e);
			} finally {
				if (buffer != null) {
					buffer.release();
				}
				
				// =============== debug info ===============
				if (RoomValue.USE_SERVER_STATE_MONITOR) {
					ServerStateThread.addInput();
				}
				// =============== debug info ===============
			}
		}
	}

	public void messageSent(NextFilter nextFilter, IoSession session, Object message) {
		nextFilter.messageSent(session, message);
	}

	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
		ByteBuffer buffer = null;
		
		if (writeRequest.getMessage() instanceof Message) {
			Message message = (Message) writeRequest.getMessage();
			
			buffer = ByteBuffer.allocate(16, false);
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
		
		// =============== debug info ===============
		if (RoomValue.USE_SERVER_STATE_MONITOR) {
			ServerStateThread.addOutput();
		}
		// =============== debug info ===============
	}

	public void filterClose(NextFilter nextFilter, IoSession session) throws Exception {
		// 服务器主动关闭客户端不通知到IoHandler.sessionClosed();
		Object object = session.removeAttribute("serverName");
		
		if (object != null) {
			RoomMessageConfig.serverMap.remove(object.toString());
			logger.info("MessageCodecFilter : server close session, client = " + object.toString());
		}
		
		// 移除终端客户端登录权限标识
		session.removeAttribute("identity");
		
		nextFilter.filterClose(session);
	}
}
