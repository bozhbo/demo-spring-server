package com.snail.mina.protocol.info.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.util.FlashHandleStr;

/**
 * 
 * 类介绍:抽象消息体
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public abstract class AbstractRoomBody implements IRoomBody {
	private int msgtype = 0;

	protected byte getByte(ByteBuffer buffer, ByteOrder order) {
		return buffer.order(order).get();
	}
	
	protected short getShort(ByteBuffer buffer, ByteOrder order) {
		return buffer.order(order).getShort();
	}
	
	protected int getInt(ByteBuffer buffer, ByteOrder order) {
		return buffer.order(order).getInt();
	}
	
	protected long getLong(ByteBuffer buffer, ByteOrder order) {
		return buffer.order(order).getLong();
	}
	
	protected float getFloat(ByteBuffer buffer, ByteOrder order) {
		return buffer.order(order).getFloat();
	}
	
	protected double getDouble(ByteBuffer buffer, ByteOrder order) {
		return buffer.order(order).getDouble();
	}
	
	protected String getString(ByteBuffer buffer, ByteOrder order) {
		if (order.equals(ByteOrder.BIG_ENDIAN)) {
			return FlashHandleStr.decodeStringB(buffer);
		} else {
			return FlashHandleStr.decodeStringL(buffer);
		}
	}
	
	protected void setByte(ByteBuffer buffer, ByteOrder order, byte b) {
		buffer.order(order).put(b);
	}
	
	protected void setShort(ByteBuffer buffer, ByteOrder order, short s) {
		buffer.order(order).putShort(s);
	}
	
	protected void setInt(ByteBuffer buffer, ByteOrder order, int i) {
		buffer.putInt(i);
	}
	
	protected void setLong(ByteBuffer buffer, ByteOrder order, long l) {
		buffer.order(order).putLong(l);
	}
	
	protected void setFloat(ByteBuffer buffer, ByteOrder order, float f) {
		buffer.order(order).putFloat(f);
	}
	
	protected void setDouble(ByteBuffer buffer, ByteOrder order, double d) {
		buffer.order(order).putDouble(d);
	}
	
	protected void setString(ByteBuffer buffer, ByteOrder order, String str) {
		if (order.equals(ByteOrder.BIG_ENDIAN)) {
			buffer.put(FlashHandleStr.encodeStringB(str));
		} else {
			buffer.put(FlashHandleStr.encodeStringL(str));
		}
	}
	
	public void setMsgType(int msgType) {
		this.msgtype = msgType;
	}
	
	public int getMsgType() {
		return this.msgtype;
	}
}
