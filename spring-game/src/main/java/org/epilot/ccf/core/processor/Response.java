package org.epilot.ccf.core.processor;



import org.apache.mina.common.IoSession;
import org.epilot.ccf.core.protocol.Message;
import org.epilot.ccf.server.send.ExecutorSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class Response {
	
	private Message message;
	private byte[] b;
	private IoSession session;
	private long recReqMsgTime;
	public  Response()
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
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	public long getRecReqMsgTime() {
		return recReqMsgTime;
	}

	public void setRecReqMsgTime(long recReqMsgTime) {
		this.recReqMsgTime = recReqMsgTime;
	}


	/**
	 * 异步发送
	 * @param obj
	 * @return
	 */
	public boolean write(Object obj)
	{
		if(session.isConnected())
		{
			if(obj instanceof Message)
			{
				printMgtTimeoutMsg((Message) obj);
			}
			
			ExecutorSend.getInstance().write(session, obj);
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * 同步发送，确保先发送的消息先到
	 * @param obj
	 * @return
	 */
	public boolean synchWrite(Object obj)
	{
		if(session.isConnected())
		{
			if(obj instanceof Message)
			{
				printMgtTimeoutMsg((Message) obj);
			}
			session.write(obj);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void printMgtTimeoutMsg(Message message)
	{
 
		long time = System.currentTimeMillis()-recReqMsgTime;
		if(time>100)
		{
			Logger log =LoggerFactory.getLogger("ccf");
			
			if(log.isWarnEnabled())
			{
				log.warn("System to process time-out:MsgType="
						+message.getHeader().getProtocolId()+",time="+time);
			}
		}
		
		
	 
	}
}
