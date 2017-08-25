package org.epilot.ccf.config;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.epilot.ccf.common.ClassResources;
import org.epilot.ccf.common.Filters;
import org.epilot.ccf.common.MessageResources;
import org.epilot.ccf.common.PermitIP;
import org.epilot.ccf.common.ProtocolsHead;
import org.epilot.ccf.common.ProtocolsProcessor;
import org.epilot.ccf.common.ServiceAccept;
import org.epilot.ccf.common.ServiceSend;
import org.epilot.ccf.common.SocketOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;


public  class  Config {
	private HashMap accptorService;
	private HashMap sendService;
	private SocketOption socketOption ;
	private Map<String,ProtocolsHead> mapHead ;
	private ConcurrentHashMap<String, ProtocolsProcessor> protocolsProcessor ;
	private HashMap<String, ClassResources> classResource;
	private HashMap<String, MessageResources> messageResource;
	private HashMap<String, PermitIP> securityIP;
	private final static  Config me= new Config();
	private static final Logger log =LoggerFactory.getLogger("ccf");
	private String flashDomain  ;
	private String flashPort;
	private Config()
	{

	}
	public  synchronized static Config getInstance()
	{	
		return me ;
	}

	/**
	 * 读配置文件
	 *
	 */
	public void loadConfig()
	{
		InputStream sysIs = Config.class.getClass().getResourceAsStream(GlobalConfig.SYSTEM_CONFIG_PATH);
		if (sysIs != null) {
			ReadConfig1(sysIs);
		}
		else 
		{
			log.error("system-config.xml can not be finded,system will exit!");
			System.exit(0);
		}
		InputStream funIs = Config.class.getClass().getResourceAsStream(GlobalConfig.FUNCTION_CONFIG_PATH);
		
		if (funIs != null) {
			ReadConfig2(funIs);
		}
		else 
		{
			log.error("function-config.xml can not be finded,system will exit!");
			System.exit(0);
		}
	}

	private  void ReadConfig1(InputStream is) {
		Document doc = XMLUtil4DOM.file2Dom(is);
		Element rootEle = null;
		if (doc != null) {
			rootEle = doc.getRootElement();
			
			List ConfigEles = rootEle.elements("io-service");
		 
			if (ConfigEles != null&&ConfigEles.size()>0) {
				accptorService=readAccptorIoService(ConfigEles);
				
			}
			List ConfigEles1 = rootEle.elements("io-service");
			if (ConfigEles1 != null&&ConfigEles1.size()>0) {
				sendService=readSendIoService(ConfigEles1);
			}
			List ConfigEles2 = rootEle.elements("socket-option");
			if (ConfigEles2 != null&&ConfigEles2.size()>0) {
				socketOption=readSocketOption(ConfigEles2);
			}
			 
			List ConfigEles5= rootEle.elements("security");
			if (ConfigEles5 != null&&ConfigEles5.size()>0) {
				securityIP=readSecurityIP(ConfigEles5);
			}
			
			List ConfigEles6 = rootEle.elements("flash-domain");
			if (ConfigEles6 != null&&ConfigEles6.size()>0) {
				readFlashDomain(ConfigEles6);
			}
			
			
		}
}
	
	
	
	private  void ReadConfig2(InputStream is) {
		Document doc = XMLUtil4DOM.file2Dom(is);
		Element rootEle = null;
		if (doc != null) {
			rootEle = doc.getRootElement();
			
			 
			List ConfigEles3 = rootEle.elements("protocols");
			if (ConfigEles3 != null&&ConfigEles3.size()>0) {
				mapHead=readProtocolHead(ConfigEles3);
				protocolsProcessor=readProtocolsProcessor(ConfigEles3);
			}
			List ConfigEles4= rootEle.elements("resources");
			if (ConfigEles4 != null&&ConfigEles4.size()>0) {
				classResource=readClassResources(ConfigEles4);
				messageResource=readMessageResources(ConfigEles4);
			}
		}
}
	private HashMap readSecurityIP(List ConfigEles) {
	
		HashMap<String, PermitIP> IPMap =new HashMap<String, PermitIP>();
		Element ElementIP = (Element) ConfigEles.get(0);
		List ipList = ElementIP.elements("permit-ip");
		if(ipList!=null&&ipList.size()>0)
		{
			for (int i = 0; i < ipList.size(); i++) 
			{
				Element tempElement = (Element) ipList.get(i);
				PermitIP permitIp = new PermitIP();
				String ip = tempElement.elementTextTrim("ip");
				String port = tempElement.elementTextTrim("port");
				if(ip!=null)
				{
					permitIp.setIp(ip);
					if(port!=null)
					{
						permitIp.setPort(port);
					}
					IPMap.put(ip, permitIp);
				}
			}
			return IPMap;
		}
		return null;
	}
	private HashMap readAccptorIoService(List ConfigEles) {
		
		HashMap serverListener =new HashMap();
		Element ElementAccepter = (Element) ConfigEles.get(0);
		serverListener.put("socket-threads", ElementAccepter.elementTextTrim("socket-threads"));
		List serviceList = ElementAccepter.elements("service-acceptor");
		 
		if(serviceList!=null&& serviceList.size()>0)
		{
			HashMap<String, ServiceAccept> serviceTemp =new HashMap<String, ServiceAccept>();
			for (int i = 0; i < serviceList.size(); i++) 
			{
				Element tempElement = (Element) serviceList.get(i);
				ServiceAccept service =new ServiceAccept();
				String serviceName = tempElement.elementTextTrim("service-name");
				String localHost = tempElement.elementTextTrim("local-host");
				String localPort = tempElement.elementTextTrim("local-port");
				String acceptHandle = tempElement.elementTextTrim("service-handle");
				String sessionHandle = tempElement.elementTextTrim("session-handle");
				service.setServiceName(serviceName);
				service.setLocalHost(localHost);
				service.setLocalPort(localPort);
 
				service.setServiceHandle(acceptHandle);

				service.setSessionHandle(sessionHandle);
				
				List list = tempElement.elements("service-filters");
				
				if(list!=null&&list.size()>0)
				{
					Element ElementFileter = (Element) list.get(0);
					List listFileter =ElementFileter.elements("filter");
					if(listFileter !=null)
					{
						Filters [] filters =new Filters[listFileter.size()];
						for(int j=0;j<listFileter.size();j++)
						{
							Element temp = (Element) listFileter.get(j);
							Filters tempFileter = new Filters();
							String name =temp.attributeValue("name");
							String ClassName=temp.attributeValue("class");
							String type = temp.attributeValue("type");
							tempFileter.setName(name);
							tempFileter.setClassName(ClassName);
							tempFileter.setType(type);
							filters[j]=tempFileter;;
						}
							service.setFilters(filters);
					}
				}
				serviceTemp.put(serviceName, service);
			}
			serverListener.put("service", serviceTemp);
		}
		return serverListener;
	}
	private HashMap readSendIoService(List ConfigEles) {
		
		HashMap serverListener =new HashMap();
		Element ElementAccepter = (Element) ConfigEles.get(0);
		serverListener.put("socket-threads", ElementAccepter.elementTextTrim("socket-threads"));
		List serviceList = ElementAccepter.elements("service-send");
		if(serviceList!=null&& serviceList.size()>0)
		{
			HashMap<String, ServiceSend> serviceTemp =new HashMap<String, ServiceSend>();
			for (int i = 0; i < serviceList.size(); i++) 
			{
				Element tempElement = (Element) serviceList.get(i);
				ServiceSend service =new ServiceSend();
				String serviceName = tempElement.elementTextTrim("service-name");
				String localHost = tempElement.elementTextTrim("local-host");
				String localPort = tempElement.elementTextTrim("local-port");
				String remoteHost = tempElement.elementTextTrim("remote-host");
				String remotePort = tempElement.elementTextTrim("remote-port");
				String acceptHandle = tempElement.elementTextTrim("service-handle");
				String sessionHandle = tempElement.elementTextTrim("session-handle");
				service.setServiceName(serviceName);
				service.setLocalHost(localHost);
				service.setLocalPort(localPort);
				service.setRemoteHost(remoteHost);
				service.setRemotePort(remotePort);
				service.setServiceHandle(acceptHandle);

				service.setSessionHandle(sessionHandle);
				
				List list = tempElement.elements("service-filters");
				
				if(list!=null&&list.size()>0)
				{
					Element ElementFileter = (Element) list.get(0);
					List listFileter =ElementFileter.elements("filter");
					if(listFileter !=null)
					{
						Filters [] filters =new Filters[listFileter.size()];
						for(int j=0;j<listFileter.size();j++)
						{
							Element temp = (Element) listFileter.get(j);
							Filters tempFileter = new Filters();
							String name =temp.attributeValue("name");
							String ClassName=temp.attributeValue("class");
							String type = temp.attributeValue("type");
							tempFileter.setName(name);
							tempFileter.setClassName(ClassName);
							tempFileter.setType(type);
							filters[j]=tempFileter;
						}
							service.setFilters(filters);
					}
				}
				serviceTemp.put(serviceName, service);
			}
			serverListener.put("service", serviceTemp);
		}
		return serverListener;
	}
	private SocketOption readSocketOption(List ConfigEles)
	{
		Element tempElement = (Element) ConfigEles.get(0);
		SocketOption so = new SocketOption();
		so.setConnectTimeout(tempElement.elementTextTrim("ConnectTimeout"));
		so.setWorkerTimeout(tempElement.elementTextTrim("WorkerTimeout"));
		so.setSocketReceiveBuffer(tempElement.elementTextTrim("SocketReceiveBuffer"));
		so.setSocketSendBuffer(tempElement.elementTextTrim("socketSendBuffer"));
		so.setKeepAlive(tempElement.elementTextTrim("KeepAlive"));
		so.setTcpNoDelay(tempElement.elementTextTrim("TcpNoDelay"));
		String[] idleTime =	null;
		List temp = tempElement.elements("IdleTime");
		if(temp != null&&temp.size()>0)
		{
			idleTime = new String[2];
			Element temp1 = (Element)temp.get(0);
			idleTime[0] = temp1.elementTextTrim("state");
			idleTime[1] = temp1.elementTextTrim("Timeout");
			so.setIdleTime(idleTime);
		}
		so.setWriteTimeout(tempElement.elementTextTrim("WriteTimeout"));
		so.setLinkTest(tempElement.elementTextTrim("LinkTest"));
		so.setLinkTestTimeout(tempElement.elementTextTrim("LinkTestTimeout"));
		return so;
	}

	private Map readProtocolHead(List ConfigEles)
	{
		ProtocolsHead head =null;
		Element tempElement = (Element) ConfigEles.get(0);
		List headList = tempElement.elements("head");
		Map map = new HashMap();
		if(headList != null&&headList.size()>0)
		{
			for(int i=0;i<headList.size();i++)
			{
				head =new ProtocolsHead();
				Element temp = (Element) headList.get(i);
				head.setClassName(temp.attributeValue("class"));
				head.setEndian(temp.attributeValue("length-endian"));
				String name = temp.attributeValue("name");
				 
				map.put(name, head);
			}
		}
		
		return  map;
	}
	private ConcurrentHashMap<String, ProtocolsProcessor> readProtocolsProcessor(List ConfigEles)
	{
		Element tempElement = (Element) ConfigEles.get(0);
		List bodyList = tempElement.elements("protocol-processors");
		ConcurrentHashMap<String, ProtocolsProcessor> map = null;
		if(bodyList!=null&&bodyList.size()>0)
		{
			map =new ConcurrentHashMap<String, ProtocolsProcessor>();
			Element temp = (Element) bodyList.get(0);
			List mappingList =temp.elements("processor-mapping");
			if(mappingList!=null&&mappingList.size()>0)
			{
				for(int i=0;i<mappingList.size();i++)
				{
					Element temp1 = (Element)mappingList.get(i);
					ProtocolsProcessor mapping = new ProtocolsProcessor();
					String protocolId = temp1.elementTextTrim("protocolId");
					String messageBody = temp1.elementTextTrim("message-body");
					String processor = null;
					Element processorEle = temp1.element("processor");
					if(processorEle != null){
						processor = processorEle.getTextTrim();
						if(processor == null || "".equals(processor))
							processor = processorEle.attributeValue("value");
						
						List<Element> processorPropertyList = processorEle.elements("property");
						if(processorPropertyList != null && processorPropertyList.size() > 0){
							for(Element propertyEle : processorPropertyList){
								String name = propertyEle.attributeValue("name");
								String value = propertyEle.attributeValue("value");
								
								mapping.getPropertyMap().put(name, value);
							}
						}
					}
					
					String usedTreadPool = temp1.elementTextTrim("usedTreadPool");
					
					mapping.setMessageBody(messageBody);
					mapping.setProcessor(processor);
					mapping.setUsedTreadPool(usedTreadPool);
					long l;
					if(protocolId==null)
					{
						log.error("protocolId is config error,system will be close");
						System.exit(0);
					}
					else
					{
						if(protocolId.startsWith("0x"))
						{
							l=Long.parseLong(protocolId.substring(2), 16);
							protocolId=String.valueOf(l);	
						}
						mapping.setProtocolId(protocolId);
					}

					map.put(protocolId,mapping);
				}
			}
		}
		return map;
	}
	private HashMap<String, ClassResources> readClassResources(List ConfigEles)
	{	
		HashMap<String, ClassResources> map=null;
		Element temp = (Element)ConfigEles.get(0);
		List classResourceList = temp.elements("class-resource");
		if(classResourceList!=null&&classResourceList.size()>0)
		{
			map =new HashMap<String, ClassResources>();
			for(int i=0;i<classResourceList.size();i++)
			{
				Element resourceTemp = (Element)classResourceList.get(i);
				ClassResources resource = new ClassResources();
				String name=resourceTemp.attributeValue("name");
				String parameter = resourceTemp.attributeValue("parameter");
				String type = resourceTemp.attributeValue("type");
				
				if(name!= null&&parameter!= null)
				{
					resource.setName(name);
					resource.setType(type);
					resource.setParameter(parameter);
					map.put(name,resource);
				}
			}
		}
		return map;
	}
	private  HashMap<String, MessageResources> readMessageResources(List ConfigEles)
	{
		HashMap<String, MessageResources> map = null;
		Element temp = (Element)ConfigEles.get(0);
		List messageResourceList = temp.elements("message-resource");
		if(messageResourceList!=null&&messageResourceList.size()>0)
		{
		   map = new HashMap<String, MessageResources>();
		   for(int i=0;i<messageResourceList.size();i++)
		   {
			Element resourceTemp = (Element)messageResourceList.get(i);
			MessageResources resource = new MessageResources();
			String MessageKey=resourceTemp.attributeValue("key");
			String parameter = resourceTemp.attributeValue("parameter");
			if(parameter!= null&&parameter.length()>0)
			{
				resource.setKey(MessageKey);
				resource.setParameter(parameter);
				map.put(MessageKey,resource);
			}
		   }
		}
		return map;
	}
	
	private  void readFlashDomain(List ConfigEles)
	{
	
	 		if (ConfigEles != null&&ConfigEles.size()>0) {
			for(int i=0;i<ConfigEles.size();i++)
			{
				Element tempElement = (Element) ConfigEles.get(i);
				String domain =  tempElement.elementTextTrim("domain");
				String port = tempElement.elementTextTrim("port");

				flashDomain = domain;
				flashPort =  port;
			}
		}
	 
	}
	
	
	
	public  ProtocolsHead getProtocolsHead(String name) {
		return mapHead.get(name);
	}
	
	public  Map<String,ProtocolsHead> getHead() {
		return mapHead;
	}
	public  Map<String, ProtocolsProcessor> getProtocolsProcessor()
	{
		return Collections.unmodifiableMap(protocolsProcessor);
	}
	/**
	 * 获得协议处理类
	 * @param key
	 * @return
	 */
	public  ProtocolsProcessor getProcessorName(String key)
	{
		if(protocolsProcessor!=null&&protocolsProcessor.containsKey(key))
		{
			 return protocolsProcessor.get(key);
		}
		else
		{
			return null;
		}
	}
	/**
	 * 获得接收器启动配置
	 * @return
	 */
	public  Map getAccptorService() {
		
		if(accptorService!=null)
		{
			return Collections.unmodifiableMap(accptorService);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获得发送配置
	 * @return
	 */
	public  Map getSendService() {
		
		if(sendService!=null)
		{
			return Collections.unmodifiableMap(sendService);
		}
		else
		{
			return null;
		}
	}
	/**
	 * 获得socket的配置
	 * @return
	 */
	public  SocketOption getSocketOption() {
		return socketOption;
	}
	/**获得消息资源
	 */
	public  Map<String, MessageResources> getMessageResource()
	{
		if(messageResource!=null)
		{
			return Collections.unmodifiableMap(messageResource);
		}
		else
		{
			return null;
		}
	}
	/**
	 * 获得类资源
	 * @return
	 */
	public  Map<String, ClassResources> getClassResource()
	{
		if(classResource!=null)
		{
			return Collections.unmodifiableMap(classResource);
		}
		else
		{
			return null;
		}
	}
	/**
	 * 通过协议头查找出对应的处理线程池
	 * @param key
	 * @return
	 */
	public  String getUsedTreadPool(String key )
	{

		ProtocolsProcessor pm=(ProtocolsProcessor) protocolsProcessor.get(key);
		if(pm!=null)
		{
			return pm.getUsedTreadPool();
		}
		else
		{
			if(log.isWarnEnabled())
			{
				log.warn("protocolId:"+key+" is not config ThreadPool!");
			}
			return  null;
		}
	}
	
	public String getFlashDomain()
	{
		return flashDomain;
	}

	public String getFlashPort()
	{
		return flashPort;
	}
	
	public synchronized boolean IsIPPermit(String ip,String port)
	{
	
		if(securityIP!=null&&securityIP.size()>0)
		{
			if(securityIP.containsKey(ip))
			{
				PermitIP permitIp = (PermitIP) securityIP.get(ip);
				
				if(permitIp.getPort()!=null)
				{
					return port.equalsIgnoreCase(permitIp.getPort());
				}
				else
				{
					return true;
				}
			}
			
			return false;
		}
		return true;
	}
}
	
