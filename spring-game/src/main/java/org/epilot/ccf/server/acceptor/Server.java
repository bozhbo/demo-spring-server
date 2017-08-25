package org.epilot.ccf.server.acceptor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.CloseFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.epilot.ccf.common.Filters;
import org.epilot.ccf.common.ServiceAccept;
import org.epilot.ccf.config.Config;
import org.epilot.ccf.config.ConfigInit;
import org.epilot.ccf.core.code.AbstaractCodeFilter;
import org.epilot.ccf.core.session.handle.SessionHandle;
import org.epilot.ccf.filter.codec.CodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 服务器端启动类
 *
 */

public class Server {

	private static final Logger log =LoggerFactory.getLogger("ccf");
    private Map<String,IoAcceptor> acceptorMap;
    private Map services ;
    private Iterator<IoSession> iterSession =null;
    private HashMap<String ,SessionHandle> sessionHandleMap = new HashMap<String ,SessionHandle>() ;
     
	public Server()
	{
		ServerConfig.getInstance();//初始化服务端的所有配置文件
		ByteBuffer.setUseDirectBuffers(false);
		ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
		acceptorMap = ServerConfig.getInstance().getAcceptor();
		services = ServerConfig.getInstance().getService();
 
	}
 

	/**
	 * 初始化过滤器
	 * @return
	 */
	private void InitFilter(Filters [] filters,String name )
	{
		DefaultIoFilterChainBuilder filterChainBuilder = null;
		
	   try{
		if(filters!= null&&Config.getInstance().getProtocolsHead(name)!=null)//拥有消息头
		{
			filterChainBuilder=acceptorMap.get(name).getDefaultConfig().getFilterChain();
			 
 
		  for (int i=0;i<filters.length;i++)
		  {
			if(filters[i]!=null)
			{
			  if(filters[i].getName()!=null)
			  {
				  if(filters[i].getClassName()!=null&&filters[i].getClassName().length()>0)
				  {
					 
					  if(filters[i].getType()!=null&&filters[i].getType().equals("code")
							  &&filters[i].getClassName()!=null&&filters[i].getClassName().length()>0)
					  {
						   
						  IoFilter c = (IoFilter) Class.forName(filters[i].getClassName()).
						  			getConstructor(String.class).newInstance(name);
						  filterChainBuilder.addLast(filters[i].getName(), c);
					  }
					  else if(filters[i].getType()!=null)
					  {
						  Class c = Class.forName(filters[i].getClassName());
						  filterChainBuilder.addLast(filters[i].getName(), (IoFilter) c.newInstance());
					  }
				  }
			  }
		   }
		 }
			log.info("Init filters success ("+name+")!");
	   }
	 }
	 catch(Exception e)
	 {
		log.error("Init filters success failure!",e);
	 }
	}
	/**
	 * 通过配置启动监听服务
	 *
	 */
	public void start()
	{
		Listening();
	
	}
	
	
	private void Listening()
	{
		
		Set set = services.keySet();
		Iterator it = set.iterator();
		while(it.hasNext())
		{	
			String serviceName =(String) it.next();
			ServiceAccept service = (ServiceAccept) services.get(serviceName);
			InitFilter(service.getFilters(),serviceName);
			InitSessionHandle(serviceName,service.getSessionHandle());
			InitAcceptor(serviceName,service);
			
		}
	}

	/**
	 * 初始化连接关闭处理类
	 * @param name
	 */
	private void InitSessionHandle(String serviceName,String sessionHandleClass)
	{
		
		if(sessionHandleClass != null&&sessionHandleClass.length()>0)
		{
			Class c;
			try {
				c = Class.forName(sessionHandleClass);
				SessionHandle sessionHandle = (SessionHandle) c.newInstance();
				
				sessionHandleMap.put(serviceName, sessionHandle);
				
				if(log.isInfoEnabled())
				{
					log.info("Init sessionHandle success("+serviceName+")!");
				}
				
			} catch (Exception e) {
				
				if(log.isErrorEnabled())
				{
					log.error("<session-handle> config error,please check config ,system will close ("+serviceName+")!", e);
				}
	 
			}
		}
	}
	/**
	 * 初始化消息接收器
	 * @param service
	 */
	private void InitAcceptor( String name,ServiceAccept service)
	{
		IoHandler c = null;
		try{
			String host = service.getLocalHost();
			int port = Integer.parseInt(service.getLocalPort());
	
			SessionHandle sessionHandle = sessionHandleMap.get(name);
			if(service.getServiceHandle()==null||
						service.getServiceHandle().length()==0)
			{
				c = new ServerHandle(sessionHandle);
			}
			else
			{

				c = (IoHandler) Class.forName(service.getServiceHandle()).
								getConstructor(SessionHandle.class).newInstance(sessionHandle);

			}
			bind(host,port,c,name);
		 }catch(Exception e)
		 {
			 if(log.isErrorEnabled())
			 {
				 log.error("<io-service> config error,please check config,system will close("+name+")!", e);
			 }
		 
		 }
	}
	/**
	 * @param host 监听地址
	 * @param port 监听端口
	 * @param c 处理类
	 */
	public void bind(String host,int port,IoHandler c,String serviceName)
	{
		if(c==null||host.length()==0||port==0)
		{
			return ;
		}
		try
		{
			acceptorMap.get(serviceName).bind(new InetSocketAddress(
						host, port), (IoHandler) c);
			//IoAcceptorConfig sc=(IoAcceptorConfig) acceptor.getDefaultConfig();
			//sc.setDisconnectOnUnbind(false);//服务端不主动关闭连接
			if(log.isInfoEnabled())
			{
				 log.info("Start Server Listener (IP:"+host+",port:"+port+")!");
			}
		}
		catch(Exception e)
		{
			if(log.isErrorEnabled())
			 {
				 log.error("Start Server Listener Failure(IP:"+host+",port:"+port+")!",e);
			 }
		}
	}
	
	/**
	 * 解除所有的监听服务
	 */
	public void unbind(String serviceName)
	{
 
		if(acceptorMap.containsKey(serviceName))
		{
			acceptorMap.get(serviceName).unbindAll();
		}
	}
	
	/**
	 * 关闭连接
	 * @param session
	 */
	public void disConnect(IoSession session)
	{
		if(session !=null&&session.isConnected())
		{
			CloseFuture future = session.close();
			future.join();
		}
	}
}
