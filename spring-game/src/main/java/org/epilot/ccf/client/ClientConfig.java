package org.epilot.ccf.client;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.epilot.ccf.common.SocketOption;
import org.epilot.ccf.config.Config;
import org.epilot.ccf.config.ConfigInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * 初始化客户端
 *
 */

public class ClientConfig {
	private static ClientConfig me;
    private HashMap<String,SocketConnector> connectors ;
    private static final Logger log =LoggerFactory.getLogger("ccf");
    private static Map services = null;
	private ClientConfig()
    {
    	connectors =new HashMap<String,SocketConnector>();

    	Config.getInstance().loadConfig();//读取config
    	ConfigInit.getInstance();//初始化config
    	getServiceConfig();
    	InitSocket();//初始化socket
    }
    public static ClientConfig getInstance()
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
    		me =new ClientConfig();
    	}
	}
    private  Map getServiceConfig()
	{
	 
	try{	
		Map map=Config.getInstance().getSendService();
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
		return services;
	}
	/**
	 * 初始化socket连接器
	 * @return
	 */
	private boolean InitSocket()
	{
		try
		{
			Map hashmap=Config.getInstance().getSendService();
			if(hashmap==null)
			{
				log.error("Init socketConnector failure!");
				return false;
			}
			int num=Integer.parseInt( (String) hashmap.get("socket-threads"));
			
		 
			if(services==null)
			{
				log.error("Init socketConnector failure!");
				return false;
			}
			
			Set set = services.keySet();
			Iterator it = set.iterator();
			while(it.hasNext())
			{	
				SocketConnector connector = null;
				String serviceName =(String) it.next();

				if(num==1)
				{
					connector  = new SocketConnector();
				}
				else
				{
					connector  = new SocketConnector(num,Executors.newCachedThreadPool());
				}
				IoConnectorConfig clientConfig = connector.getDefaultConfig();
				clientConfig.setThreadModel(ThreadModel.MANUAL);
			
				SocketOption so = Config.getInstance().getSocketOption();
			
				if(so!=null&&so.getConnectTimeout()!=null)
				{
					clientConfig.setConnectTimeout(Integer.parseInt(so.getConnectTimeout()));
				}
				if(so!=null&&so.getWorkerTimeout()!=null)
				{
					
					connector.setWorkerTimeout(Integer.parseInt(so.getWorkerTimeout()));
				
				}
				if(log.isInfoEnabled())
				{
					log.info("Init socketConnector success("+serviceName+")");
				}
				connectors.put(serviceName, connector);
			}
			
			return true;
		}
		catch(Exception e)
		{
			log.error("Init socketConnector failure!",e);
			return false;
		}
	}
	

	public HashMap getConnector()
	{
		return  connectors;
	}
	public Map getService()
	{
		return services;
	}
	
}
