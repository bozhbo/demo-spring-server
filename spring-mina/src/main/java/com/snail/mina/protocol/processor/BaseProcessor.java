package com.snail.mina.protocol.processor;

import org.apache.mina.common.IoSession;

import com.snail.mina.protocol.info.IRoomBody;

/**
 * 
 * 类介绍:具体业务处理超类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public abstract class BaseProcessor implements IProcessor {
	
	private Class<? extends IRoomBody> c = null;

	public BaseProcessor() {
		
	}
	
	public BaseProcessor(Class<? extends IRoomBody> c) {
		this.c = c;
	}
	
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return c;
	}
	
	public boolean isPermitSession(IoSession session) {
		if (session.getAttribute("serverName") != null) {
			return true;
		} else {
			return false;
		}
	}
}
