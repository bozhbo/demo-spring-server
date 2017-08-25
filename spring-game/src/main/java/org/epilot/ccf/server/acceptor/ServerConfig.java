package org.epilot.ccf.server.acceptor;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.epilot.ccf.config.Config;
import org.epilot.ccf.config.ConfigInit;
import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 初始化服务器端
 * @author tangjie
 *
 */

public class ServerConfig {
	private static ServerConfig me;

	private Map services;
    private Map<String,IoAcceptor> acceptorMap;

    protected static final Logger log =LoggerFactory.getLogger("ccf");
   
  
    private ServerConfig()
    {
    	acceptorMap = new HashMap<String,IoAcceptor>();
    	Config.getInstance().loadConfig();//读取config
    	ConfigInit.getInstance();//初始化config
    	ThreadPoolManager.getInstance();//初始化所有线程池
    	getServiceConfig();
    	InitSocket();
    }
    public static ServerConfig getInstance()
    {
    	if(me==null)
    	{
    		init();
    	}
    	return me;
    }
    private synchronized static void init()
	{
    	if(me==null)
    	{
    		me =new ServerConfig();
    	}
	}
	private  void getServiceConfig()
	{
	try{	
		Map map=Config.getInstance().getAccptorService();
		if(map==null)
		{
			log.error("Load service-config failure!");
			
		}
		else
		{
				services  =(HashMap)map.get("service");
				if(services==null)
				{
					log.error("Load service-config failure!");
				}
			  } 
		}
		catch(Exception e)
		{	
			log.error("Load service-config failure!",e);
		}
		if(services==null)
		{
			log.info("Load communication framework config failure,system will  close!");
			System.exit(0);
		}
	}
/**
 * 初始化socket接收器
 * @return
 */
	private boolean InitSocket()
	{
		try
		{
			Map map=Config.getInstance().getAccptorService();
			if(map==null)
			{
				log.error("Init socketAccepter failure!");
				return false;
			}
			int num=Integer.parseInt( (String) map.get("socket-threads"));
			Set set = services.keySet();
			Iterator it = set.iterator();
			while(it.hasNext())
			{
				IoAcceptor acceptor = null;
				String serviceName =(String) it.next();
				if(num==1)
				{
					acceptor  = new SocketAcceptor();
				}
				else
				{
					acceptor  = new SocketAcceptor(num,Executors.newCachedThreadPool());
				}
					   
				SocketAcceptorConfig acceptorConfig = (SocketAcceptorConfig) acceptor.getDefaultConfig();
				acceptorConfig.setThreadModel(ThreadModel.MANUAL);
			
			 
				if(log.isInfoEnabled())
				{
					log.info("Init socketAccepter success("+serviceName+")");
				}
				acceptorMap.put(serviceName, acceptor);
			}
			return true;
		}
		catch(Exception e)
		{
			log.error("Init socketAccepter failure", e);
			return false;
		}
	}

	public Map<String,IoAcceptor> getAcceptor()
	{
		return acceptorMap;
	}

	public Map getService()
	{
		return services;
	}

}
