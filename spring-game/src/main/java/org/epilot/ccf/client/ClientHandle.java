package org.epilot.ccf.client;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.epilot.ccf.common.SocketOption;
import org.epilot.ccf.config.Config;
import org.epilot.ccf.core.processor.ProtocolHandler;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.processor.ServerHandleAdapter;
import org.epilot.ccf.core.protocol.Message;
import org.epilot.ccf.core.session.handle.SessionHandle;
import org.epilot.ccf.mapping.ClassMapping;
import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.AMFMessageKey;

import flex.messaging.io.amf.ASObject;

/**
 * 
 * @author tangjie
 * 
 */
public class ClientHandle extends ServerHandleAdapter {
	private static final Logger log = LoggerFactory.getLogger("ccf");

	private SessionHandle sessionHandle;
	public ClientHandle()
	{
		
	}
	public ClientHandle(SessionHandle sessionHandle) {
		this.sessionHandle = sessionHandle;
	}

	public void sessionClosed(IoSession session)throws Exception {
	
		if(sessionHandle!=null)
		{
			sessionHandle.notifyClose(session);
		}
	}

	public void sessionOpened(IoSession session) throws Exception {
		
		if(sessionHandle!=null)
		{
			sessionHandle.notifyOpen(session);
		}
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if(sessionHandle!=null)
		{
			sessionHandle.notifyIdle(session);
		}
	}

	public void messageReceived(IoSession session, Object in) {
	
		Request request = new Request();
		request.setSession(session);
		Response response =new Response();
		response.setSession(session);
	
		if(in instanceof byte[])
		{
			request.setBytes((byte[])in);
		}
		else if(in instanceof Message)
		{
			request.setMessage((Message)in);
			request.setProcotolId(((Message)in).getHeader().getProtocolId());
		}	
		else if(in instanceof ASObject)
		{
			ASObject message = (ASObject)in;
			ASObject header = (ASObject)message.get(AMFMessageKey.MSG_HEADER);
			request.setProcotolId(Long.parseLong(header.get(AMFMessageKey.MSG_TYPE).toString()));
			request.setAmfMessage((ASObject) in);
		}
		ProtocolProcessor pf = setProcesser(request);
		
		if(pf !=null)
		{
			ProtocolHandler processorHandler =new ProtocolHandler(request,response,pf);
			String userPoolName = Config.getInstance().getUsedTreadPool(String.valueOf(request.getProcotolId()));
			if(userPoolName !=null)
			{
				ThreadPoolManager.getInstance().Pool(userPoolName).execute(processorHandler);//.runRunnable(processorHandler,userPoolName);	
			}
			else
			{
				processorHandler.run();
			}
		}
	}
	public  ProtocolProcessor setProcesser(Request request)
	{
		ProtocolProcessor pf= ClassMapping.buildProcessor(String.valueOf(request.getProcotolId()));
		if(pf !=null)
		{
				return pf;
		}
		else
		{
				if(log.isWarnEnabled())
				{
					log.warn("ProtocolProcessor is not support ProcotolId:"+request.getProcotolId());
				}
				return null;
		}
		
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	public IoSession setReceiverSession(IoSession session) {
		return session;
	}

	public void exceptionCaught(IoSession session, Throwable cause)
	{
		if(log.isErrorEnabled())
		{
			log.error("",cause);
		}
		if(sessionHandle!=null)
		{
			sessionHandle.notifyException(session);
		}

		 
	}

	public void sessionCreated(IoSession session) throws Exception {
		if(sessionHandle!=null)
		{
			sessionHandle.notifyCreate(session);
		}
	}
}
