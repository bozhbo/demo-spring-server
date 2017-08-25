package org.epilot.ccf.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.CloseFuture;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.epilot.ccf.common.Filters;
import org.epilot.ccf.common.ServiceSend;
import org.epilot.ccf.config.Config;

import org.epilot.ccf.core.session.handle.SessionHandle;
import org.epilot.ccf.filter.codec.CodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端的启动类
 * @author tangjie
 *
 */

public class Client {

	private HashMap connectors = null;
	private static final Logger log =LoggerFactory.getLogger("ccf");
	private Map services;
    private final static Client client = new Client();
    private HashMap<String ,SessionHandle> sessionHandleMap = new HashMap<String ,SessionHandle>() ;
	private HashMap<String,IoSession> connectPool = new HashMap<String,IoSession>();
	
    private Client()
	{
		ClientConfig.getInstance();//初始化客户端的所有配置文件
		ByteBuffer.setUseDirectBuffers(false);
	    ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
	    
	    services = ClientConfig.getInstance().getService();
		Set set =services.keySet();
		Iterator it =set.iterator();
		 
		while(it.hasNext())
		{
			initclient((String)it.next());
		}
		
	}
    
	public static  Client getInstance()
	{
		return client;
	}
	/**
	 * 初始化连接
	 * @param poolname
	 */
	private void initclient(String poolname)
	{
		
		connectors = ClientConfig.getInstance().getConnector();
	
		if(services.containsKey(poolname))
		{
			ServiceSend service = (ServiceSend) services.get(poolname);
			InitFilter(service.getFilters(),poolname);
			
			InitSessionHandle(poolname,service.getSessionHandle());
			
			
		}
	}
	/**
	 * 初始化过滤器
	 * @return
	 */
	private void InitFilter(Filters [] filters,String name)
	{
		DefaultIoFilterChainBuilder filterChainBuilder = null;
	   try{
		   
		if(filters!= null&&Config.getInstance().getProtocolsHead(name)!=null)//拥有消息头
		{
			
			filterChainBuilder=((SocketConnector) connectors.get(name)).getDefaultConfig().getFilterChain();
			 
	 
		  for (int i=0;i<filters.length;i++)
		  {
			  if(filters[i].getName()!=null)
			  {
		
				  if(filters[i].getType()!=null&&filters[i].getType().equals("code")
						  &&filters[i].getClassName()!=null&&filters[i].getClassName().length()>0)
				  {
					   
						 
					  IoFilter c = (IoFilter) Class.forName(filters[i].getClassName()).
					  			getConstructor(String.class).newInstance(name);
					  filterChainBuilder.addLast(filters[i].getName(), c);
				  }
				  else  if(filters[i].getClassName()!=null&&filters[i].getClassName().length()>0)
				  {
					  Class c = Class.forName(filters[i].getClassName());
					  filterChainBuilder.addLast(filters[i].getName(), (IoFilter) c.newInstance());
				  }
			  }
		 }
		  if(log.isInfoEnabled())
		  {
			log.info("Init filters success("+name+")!");
		  }
	   }
	 }
	 catch(Exception e)
	 {
		 if(log.isErrorEnabled())
		 {
			 log.error("Init filters success failure!",e);
		 }
	 }
	}
	/**
	 * 
	 * @param name
	 */
	private void InitSessionHandle(String serviceName,String sessionHandleClass)
	{
		if(sessionHandleClass != null&&sessionHandleClass.length()>0)
		{
			try {
				Class c = Class.forName(sessionHandleClass);
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
	 * 采用长连接方式连接服务器端
	 * @param poolname
	 * @return
	 */
	public synchronized IoSession getSession(String poolname)
	{
		if(connectPool.containsKey(poolname)&&connectPool.get(poolname)!=null
				&&connectPool.get(poolname).isConnected())
		{
			return connectPool.get(poolname);
		}
		else if(services.containsKey(poolname))
		{
			return initConnect(poolname);
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * 初始化一个新连接
	 * @param poolname
	 * @return
	 */
	public synchronized IoSession initConnect(String name)
	{
		if(services.containsKey(name))
		{
			IoSession session = InitConnector(sessionHandleMap.get(name),name);
			connectPool.put(name, session);
			return connectPool.get(name);
		}
		else
		{
			return null;
		}
	}
	/**
	 * 初始化一个连接
	 * @param service
	 * @return
	 */
	private IoSession  InitConnector(SessionHandle sessionHandle,String name)
	{
		ServiceSend service = (ServiceSend) services.get(name);
		
		IoSession session =null;
		IoHandler ioHandler =null;
		String localHost=null;
		int localPort=0;
		String remoteHost = service.getRemoteHost();
		int remotePort = Integer.parseInt(service.getRemotePort());
	try{
		if(service.getLocalHost()!=null&&service.getLocalPort()!=null)
		{
			localHost = service.getLocalHost();
			localPort =Integer.parseInt( service.getLocalPort());
		}
		if(service.getServiceHandle()==null||
			service.getServiceHandle().length()==0)
		{
			
			ioHandler = new ClientHandle(sessionHandle);
		}
		else
		{
			ioHandler = (IoHandler) Class.forName(service.getServiceHandle()).
						getConstructor(SessionHandle.class).newInstance(sessionHandle);
		}
		if(localHost!=null&&localPort>0)
		{
			session=connect(remoteHost,remotePort,localHost,localPort,ioHandler,service.getServiceName());

		}
		else
		{
			session=connect(remoteHost,remotePort,ioHandler,service.getServiceName());
		}
	 }
	 catch(Exception e)
	 {
		 if(log.isErrorEnabled())
		 {
			 log.error("<io-service> config error,please check config,system will close("+name+")!", e);
		 }
	 
	 }
	 if(session!=null&&session.isConnected())
	 {
		 return session;
	 }
	 else
	 {
		 return null;
	 }
	}
	/**
	 * 
	 * @param remoteHost 连接地址
	 * @param remotePort 连接端口
	 * @param class 处理类
	 */
	private IoSession connect(String remoteHost,int remotePort,IoHandler c ,String name)
	{
		IoSession session = null;
		ConnectFuture  cf = null;
		if(c==null||remoteHost.length()==0||remotePort==0)
		{
			return null;
		}
		try
		{		
			 cf = ((SocketConnector) connectors.get(name)).connect(new InetSocketAddress(
						 remoteHost, remotePort), c);
			 cf.join(3000);
			 if(cf!=null&&cf.isConnected())
			 {
				 if(log.isInfoEnabled())
				 {
					 log.info("connect server success (IP:"+remoteHost+",port:"+remotePort+")!");
				 }
				 session = cf.getSession();
			 }
			 else
			 {
				 if(log.isErrorEnabled())
				 {
					 log.error("connect server failure (IP:"+remoteHost+",port:"+remotePort+")!");
				 }
			 }
		}
		catch(Exception e)
		{
			log.error("",e);
		}
		return session;
	}
	/**
	 * @param remoteHost 远程连接地址
	 * @param remotePort 远程连接端口
	 * @param localHost 本地选择地址
	 * @param localPort 本地选择端口
	 * @param c 处理类
	 */
	private IoSession connect(String remoteHost,int remotePort,
			String localHost,int localPort, IoHandler c ,String name)
	{
		IoSession session = null;
		ConnectFuture  cf = null;
		if(c==null||remoteHost.length()==0||remotePort==0
				||localHost.length()==0||localPort==0)
		{
			return null;
		}
		try
		{		
			
				
			 cf =((SocketConnector) connectors.get(name)).connect(new InetSocketAddress(
						 remoteHost, remotePort), new InetSocketAddress(
								 localHost, localPort), c);
			 cf.join(3000);
			 if(cf!=null&&cf.isConnected())
			 {
				 if(log.isInfoEnabled())
				 {
					 log.info("connect server success(IP:"+remoteHost+",port:"+remotePort+")!");
				 }
				 session = cf.getSession();
				
			 }
			 else
			 {
				 if(log.isErrorEnabled())
				 {
					 log.error("connect server failure (IP:"+remoteHost+",port:"+remotePort+")!");
				 }
			 }
			 
		}
		catch(Exception e)
		{
			log.error("",e);
		}
		return session;
	}
	
	public void disConnect(IoSession session)
	{
		if(session !=null&&session.isConnected())
		{
			CloseFuture future = session.close();
			future.join();
			session.close();
		}
	}
}
