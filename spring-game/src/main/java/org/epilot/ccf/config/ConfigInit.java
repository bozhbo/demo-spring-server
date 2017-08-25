package org.epilot.ccf.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
 
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.epilot.ccf.common.ClassResources;
import org.epilot.ccf.common.MessageResources;
import org.epilot.ccf.common.ProtocolsHead;
import org.epilot.ccf.common.ProtocolsProcessor;
import org.epilot.ccf.common.SocketOption;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.util.AbstractStringHandle;
import org.epilot.ccf.mapping.ClassMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConfigInit {
	 private Map<String,MessageHeader> headMap ;
	 private Map lengthEndianMap;
	 private HashMap<String, HashMap<String, Object>> resourceMap;
	 private static final Logger log =LoggerFactory.getLogger("ccf");
	 private static ConfigInit me = new ConfigInit() ;

	 private ConfigInit()
	 {
		 headMap = new HashMap<String,MessageHeader>();
		 lengthEndianMap = new HashMap<String,String>();
		 
		 initConfig();
	 }
	 public static ConfigInit getInstance()
	 {
	    return me;
	 }
	 

	    /**
	     * 装载配置文件
	     * @return
	     */
		private  boolean  initConfig()
		{		
			if(InitSocketOption()&&InitHeader()&&InitBody()&&InitClassResource()
					&&InitMessageResources())
			{
				log.info("Load  communication framework config success!");
				return true;
			}
			else
			{
				log.info("Load communication framework config failure,system will  close!");
				System.exit(0);
				return false;
			}
		}
	 /**
	 * 初始化socketoption
	 * @return
	 */
	private boolean InitSocketOption()
	{
	   SocketOption so = Config.getInstance().getSocketOption();
	   if(so!=null)
	   {
	     try
	     {
	    	if(so.getConnectTimeout()!=null)
	    	{
	    		Integer.parseInt(so.getConnectTimeout());
	    	}
	    	if(so.getWorkerTimeout()!=null)
	    	{
	    		Integer.parseInt(so.getWorkerTimeout());
	    	}
	    	if(so.getSocketSendBuffer()!=null)
	    	{
	    		Integer.parseInt(so.getSocketSendBuffer());
	    	}
	    	if(so.getSocketReceiveBuffer()!=null)
	    	{
	    		Integer.parseInt(so.getSocketReceiveBuffer());
	    	}
	    	if(so.getKeepAlive()!=null)
	    	{
	    		Boolean.valueOf( so.getKeepAlive());
	    	}
	    	if(so.getTcpNoDelay()!=null)
	    	{
	    		Boolean.valueOf( so.getTcpNoDelay());
	    	}
	    	if(so.getIdleTime()!=null)
	    	{
	    		String[] idle = so.getIdleTime();
	    		Integer.parseInt(idle[0]);
	    		Integer.parseInt(idle[1]);
	    	}
	    	if(so.getWriteTimeout()!=null)
	    	{
	    		Integer.parseInt(so.getWriteTimeout());
	    	}
	    	if(so.getLinkTest()!=null)
	    	{
	    		Boolean.valueOf(so.getLinkTest());
	    	}
	    	if(so.getLinkTestTimeout()!=null)
	    	{
	    		Integer.parseInt(so.getLinkTestTimeout());
	    	}
	    	
	     }
	     catch(Exception e)
	     {
	    	log.error("Load socketOption failure !",e);
	    	return false;
	     }
	    	log.info("Load socketOption success!");
	   }
	    return true;
	}
	/**
	 * 初始化消息头
	 * @return
	 */
	private boolean InitHeader()
	{
	 Map map =  Config.getInstance().getHead();
	  if(map !=null)
	  {
		try
		{ 	 Set set  = map.keySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext())
			{
				String name = (String) iterator.next();
				
				ProtocolsHead header = (ProtocolsHead) map.get(name);
				
				MessageHeader mh =null;
				//消息头class已经配置
				if(header.getClassName()!=null&&header.getClassName().length()>0)
				{
					mh= ClassMapping.buildHeader(name);
					if(mh==null)
					{
						log.error("Load message-header failure!");
						return false;
					}
				}
				
				//必须配置length-endian
				String lengthEndian = header.getEndian();
				if(lengthEndian==null||
					(!lengthEndian.equals("1")&&!lengthEndian.equals("0")))
				{
					
					log.error("Load message-header failure:length-endian error!");
					return false;
				}
				headMap.put(name, mh);
				lengthEndianMap.put(name, lengthEndian);
				
			}

		} catch (Exception e) {
				log.error("Load message-header failure!",e);
				return false;
			}
		}
		return true;
	}

	 
	/**
	 * 初始化消息体
	 * @return
	 */
	private boolean InitBody()
	{
		Map<String, ProtocolsProcessor> map =Config.getInstance().getProtocolsProcessor();

		if(map!=null)
		{
			Set set = map.keySet();
			
			if(set!=null)
			{
				Iterator it = set.iterator();
				while(it.hasNext())
				{
					ProtocolsProcessor pm=(ProtocolsProcessor) map.get(it.next());
					
					MessageBody body = ClassMapping.buildBody(pm.getProtocolId());
					
					if(body==null&&pm.getMessageBody()!=null)
					{
						if(log.isErrorEnabled())
						{
							log.error("create message body failure,ProtocolId:"+pm.getProtocolId());
						}
						return false;
					}
					if(pm.getProcessor()!=null&&pm.getProcessor()!=null)
					{
						ProtocolProcessor ph = ClassMapping.buildProcessor(pm.getProtocolId());
						if(ph==null)
						{
							if(log.isErrorEnabled())
							{
								log.error("create message body failure,ProtocolId:"+pm.getProtocolId());
							}
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean InitClassResource()
	{
		Map<String, ClassResources> map = Config.getInstance().getClassResource();
		if(map!=null)
		{
			Set set = map.keySet();	
			if(set!=null)
			{
				Iterator it = set.iterator();
				while(it.hasNext())
				{
					ClassResources resources = (ClassResources) map.get(it.next());
					if(resources==null)
					{
						log.error("Load class-resource failure!");
						
						return false;
					}
				
					AbstractStringHandle stringHandle = ClassMapping.buildStringHandle(resources.getName());
					if(stringHandle == null)
					{
						if(log.isErrorEnabled())
						{
							log.error("Load class-resource failure! name:"+resources.getParameter());
						}
						return false;
					}
				}
				 log.info("Load class-resource success!");
			}
		}
		return true;
	}
	private boolean InitMessageResources()
	{
		Map<String, MessageResources> map = Config.getInstance().getMessageResource();
		if(map!=null)
		{
			Set set = map.keySet();	
			if(set!=null)
			{
			  resourceMap =new HashMap<String, HashMap<String, Object>>();
			  Iterator it = set.iterator();
			  while(it.hasNext())
			  {
				MessageResources resources = (MessageResources) map.get(it.next());
				if(resources!=null)
				{
					String parameter = resources.getParameter();
					String name = resources.getKey();
//					String path = parameter.replace('.', '/');
////					path += ".properties";
					InputStream is = this.getClass().getResourceAsStream(parameter);
				   try
				   {
					 if(is == null) 
					 {
					   File file = new File(parameter);
					   if(file.exists())
					   {
						   is = new FileInputStream(file);
					   }
					 }
					 if(is != null) 
					 {
					   Properties p = new Properties();
					   p.load(is);
					   is.close();
					   Iterator names = p.keySet().iterator();
					   HashMap<String, Object> map1 =new HashMap<String, Object>();
					   while (names.hasNext()) 
					   {
						   String key = (String) names.next();
						   String message = new String(((String) p.get(key)).getBytes("ISO-8859-1"),"UTF-8");
						   map1.put(key,message );
					   }
					     resourceMap.put(name, map1);
					 }
					 else
					 {
						 if(log.isWarnEnabled())
						 {
							 log.warn("message-resource:"+parameter+" can not be finded!");
						 }
					 }
					} 
				    catch (Exception e) 
				    {
				    	log.error("Load message-resource failure!  key:"+parameter,e);
						return false;
					}
				}
			  }
			  log.info("Load message-resource success!");
			}
		}
		return true;
	}
	
 
	
	
	
	/**
	 * 获得资源文件
	 * @return
	 */

	public Map<String, Object> getMessageResource(String name)
	{
		Map<String, Object> map = resourceMap.get(name);
		if(map!=null)
		{
			return Collections.unmodifiableMap(map);
		}
		else
		{
			return null;
		}

	}
	public String getLengthEndian(String name) {
		 
		return (String) lengthEndianMap.get(name);
	}
 
}
