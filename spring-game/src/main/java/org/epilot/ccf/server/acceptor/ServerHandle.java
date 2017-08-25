package org.epilot.ccf.server.acceptor;





import java.net.InetSocketAddress;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.epilot.ccf.common.BlackProtocolList;
import org.epilot.ccf.common.SocketOption;
import org.epilot.ccf.config.Config;
import org.epilot.ccf.core.processor.ProtocolHandler;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.processor.ServerHandleAdapter;
import org.epilot.ccf.core.protocol.Message;
import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;
import org.epilot.ccf.core.session.handle.SessionHandle;
import org.epilot.ccf.mapping.ClassMapping;
import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.AMFMessageKey;

import flex.messaging.io.amf.ASObject;


/**
 * 客户端处理类
 * 
 * @author tangjie
 * 
 */
public class ServerHandle extends ServerHandleAdapter {


	private static final Logger log = LoggerFactory.getLogger("ccf");

	private SessionHandle sessionHandle = null;
	public ServerHandle() {

	}
	public ServerHandle(SessionHandle sessionHandle) {
		this.sessionHandle = sessionHandle;
	}
	
	public void sessionClosed(IoSession iosession) {

		if(sessionHandle!=null)
		{
			sessionHandle.notifyClose(iosession);
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
		response.setRecReqMsgTime(System.currentTimeMillis());
		if(in instanceof byte[])
		{
			request.setBytes((byte[])in);
		}
		else if(in instanceof Message)
		{
			request.setMessage((Message)in);
			request.setProcotolId(((Message)in).getHeader().getProtocolId());
			
			// 检查是否此接口号被屏蔽
			if (BlackProtocolList.contain((int) request.getProcotolId())) {
				// 已被屏蔽;
				((Message)in).getHeader().setProtocolId(0xFFFC);
				session.write(in);
				return;
			}
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
			String userPoolName = Config.getInstance().getUsedTreadPool(
				String.valueOf(request.getProcotolId()));
			if(userPoolName!=null)
			{
				ThreadPoolManager.getInstance().Pool(userPoolName).execute(processorHandler);
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
				log.warn("ProtocolProcessor is not support ProcotolId: 0x"+Long.toHexString(request.getProcotolId()));
			}
			return null;
		}
	}
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if(log.isErrorEnabled())
		{
			log.error("",cause);
		}
		if(sessionHandle!=null)
		{
			sessionHandle.notifyException(session);
		}
		
	}

	public void sessionOpened(IoSession session) throws Exception {
		if (session.getTransportType() == TransportType.SOCKET) {
			SocketOption so = Config.getInstance().getSocketOption();

			if (so != null) {
				if (so.getSocketSendBuffer() != null)
					((SocketSessionConfig) session.getConfig())
							.setSendBufferSize(Integer.valueOf(so
									.getSocketSendBuffer()));
				if (so.getSocketReceiveBuffer() != null)
					((SocketSessionConfig) session.getConfig())
							.setReceiveBufferSize(Integer.parseInt(so
									.getSocketReceiveBuffer()));
				if (so.getKeepAlive() != null)
					((SocketSessionConfig) session.getConfig())
							.setKeepAlive(Boolean.valueOf(so.getKeepAlive()));
				if (so.getTcpNoDelay() != null)
					((SocketSessionConfig) session.getConfig())
							.setTcpNoDelay(Boolean.valueOf(so.getTcpNoDelay()));
				if (so.getWriteTimeout() != null)
					session.setWriteTimeout(Integer.parseInt(so
							.getWriteTimeout()));
				if (so.getIdleTime() != null) {
					int state = Integer.parseInt(so.getIdleTime()[0]);
					int timeout = Integer.parseInt(so.getIdleTime()[1]);
					if (state == 1) {
						session.setIdleTime(IdleStatus.READER_IDLE, timeout);
					} else if (state == 2) {
						session.setIdleTime(IdleStatus.WRITER_IDLE, timeout);
					} else if (state == 3) {
						session.setIdleTime(IdleStatus.BOTH_IDLE, timeout);
					}
				}
			}
		}
	}

	public void sessionCreated(IoSession session) throws Exception {
		
		InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
		String ip = address.getAddress().getHostAddress();
		String port = String.valueOf(address.getPort());
		if(!Config.getInstance().IsIPPermit(ip, port))
		{
			if(log.isWarnEnabled())
			{
				session.close();
				log.warn("It is not a permit IP:("+ip+":"+port+")");
				return ;
			}
		}
		
		if(sessionHandle!=null)
		{
			sessionHandle.notifyCreate(session);
		}
	}

}
