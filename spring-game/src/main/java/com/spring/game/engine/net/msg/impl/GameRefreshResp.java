package com.snail.webgame.engine.net.msg.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * 类介绍:游戏刷新响应类，用于一次操作后缓存多处修改后的刷新，保证一次刷新所有变化
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class GameRefreshResp extends GameMessageResp {
	
	private int superKey; // 顶级类型
	private int key;	  // 子类型
	private Object value; // 传输对象,可以是简单类型或是传输对象
	
	public GameRefreshResp() {
		
	}
	
	public GameRefreshResp(int key, String value, int superKey) {
		this.key = key;
		this.value = value;
		this.superKey = superKey;
	}
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getSuperKey() {
		return superKey;
	}

	public void setSuperKey(int superKey) {
		this.superKey = superKey;
	}

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.superKey);
		setInt(buffer, order, this.key);
		
		if (value instanceof Integer) {
			setInt(buffer, order, (Integer)value);
		} else if (value instanceof Byte) {
			setByte(buffer, order, (Byte)value);
		} else if (value instanceof Short) {
			setShort(buffer, order, (Short)value);
		} else if (value instanceof Long) {
			setLong(buffer, order, (Long)value);
		} else if (value instanceof Double) {
			setDouble(buffer, order, (Double)value);
		} else if (value instanceof Float) {
			setFloat(buffer, order, (Float)value);
		} else if (value instanceof String) {
			setString(buffer, order, (String)value);
		} else if (value instanceof GameMessageResp) {
			((GameMessageResp)value).resp2Bytes(buffer, order);
		} else if (value instanceof GameMessageReq) {
			((GameMessageReq)value).resp2Bytes(buffer, order);
		} else {
			// error type can not send to client
		}
	}
}
