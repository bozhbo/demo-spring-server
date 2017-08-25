package org.epilot.ccf.core.processor;



import java.util.Iterator;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.GlobalConfig;
import org.epilot.ccf.core.pool.ConnectionManange;
import org.epilot.ccf.core.protocol.Message;

import flex.messaging.io.amf.ASObject;

public class Request {
	private Message message;
	private long procotolId;
	private IoSession session;
	private byte[] b;
	private ASObject amfMessage;


	public ASObject getAmfMessage() {
		return amfMessage;
	}

	public void setAmfMessage(ASObject amfMessage) {
		this.amfMessage = amfMessage;
	}

	public  Request()
	{
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public byte[] getBytes() {
		return b;
	}
	public void setBytes(byte[] b) {
		this.b = b;
	}

	public long getProcotolId() {
		return procotolId;
	}
	public void setProcotolId(long procotolId) {
		this.procotolId = procotolId;
	}
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	/**
	 * 关闭请求连接
	 */
	public void close()
	{
		Set<String> set = session.getAttributeKeys();
		Iterator<String> it =set.iterator();
		while(it.hasNext())
		{
			String key = it.next();
			if(key.startsWith(GlobalConfig.POOL_KEY_NAME))
			{
				ConnectionManange manage=(ConnectionManange)(session.getAttribute(key));
				manage.close();
			}
		}
	}
	/**
	 * 返回连接池连接
	 */
	public void back()
	{
		Set<String> set = session.getAttributeKeys();
		Iterator<String> it =set.iterator();
		while(it.hasNext())
		{
			String key = it.next();
			if(key.startsWith(GlobalConfig.POOL_KEY_NAME))
			{
				ConnectionManange manage=(ConnectionManange)(session.getAttribute(key));
				manage.back();
			}
		}
		
	}
}
