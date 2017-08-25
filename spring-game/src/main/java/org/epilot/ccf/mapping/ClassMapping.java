package org.epilot.ccf.mapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.epilot.ccf.common.ClassResources;
import org.epilot.ccf.common.ProtocolsHead;
import org.epilot.ccf.common.ProtocolsProcessor;
import org.epilot.ccf.config.Config;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.util.AbstractStringHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class ClassMapping {

	private static final Logger log =LoggerFactory.getLogger("ccf");
	private final static ConcurrentHashMap<String, AbstractStringHandle>  stringHandleMap =new ConcurrentHashMap<String, AbstractStringHandle>();
	private final static ConcurrentHashMap<String, MessageBody> classMapBody =new ConcurrentHashMap<String, MessageBody>();
	private final static ConcurrentHashMap<String, MessageBody> classRelMapBody =new ConcurrentHashMap<String, MessageBody>();
	private final static ConcurrentHashMap<String, MessageHeader>  classMapHeader =new ConcurrentHashMap<String, MessageHeader>();
	private final static ConcurrentHashMap<String, ProtocolProcessor> classMapProcessor =new ConcurrentHashMap<String, ProtocolProcessor>();
	private final static Object StringObject = new Object();
	private final static Object BodyObject = new Object();
	private final static Object RelBodyObject = new Object();
	private final static Object HeaderObject = new Object();
	private final static Object ProcessorObject = new Object();
	private  ClassMapping(){
		 
	}
	
	/**
	 * 该方法返回的字符串处理类是不安全的，如要安全必须返回处理类的clone对象
	 * 根据不同的消息类型选出不同的消息处理工厂
	 * @param key 消息类型
	 * @return 字符串处理
	 */
	public static  AbstractStringHandle buildStringHandle(String key) {
		AbstractStringHandle handle = null;
		
		//判断是否已经将类载入
		handle =  (AbstractStringHandle) stringHandleMap.get(key);
		if(handle!=null)
		{
		
		}
		else
		{
			synchronized(StringObject)
			{
				if( stringHandleMap.get(key)==null)
				{
					ClassResources resource =new Resource().getClassResources(key);
					String name  = null;
					if(resource!=null)
					{
						name = resource.getParameter();
					}
					if(name == null||name.length()==0)
					{
						return null;
					}
					try {
						Class c = Class.forName(name);
						handle = (AbstractStringHandle) c.newInstance();
						stringHandleMap.put(key, handle);
					
					} catch (Exception e) {
						log.error("stringHandle init error! name:"+key,e);
					}
					
				}
				else
				{
					handle =  (AbstractStringHandle) stringHandleMap.get(key);
					
				}
			}
		}
		
		return handle;		
	}
	/**
	 * 该方法返回的消息处理器是不安全的，如要安全必须返回处理类的clone对象
	 * 根据不同的消息类型选出不同的消息处理工厂
	 * @param key 消息类型
	 * @return 消息处理工厂
	 */
	public static  ProtocolProcessor buildProcessor(String key) {
		
		
		ProtocolProcessor processor = null;
		processor =  (ProtocolProcessor) classMapProcessor.get(key);
		if(processor!=null)
		{	
			
		}
		else
		{	
			synchronized(ProcessorObject)
			{
				if( classMapProcessor.get(key)==null)
				{
					ProtocolsProcessor pm =null;
					String name  = null;
					pm = Config.getInstance().getProcessorName(key);
					if(pm!=null)
					{
						name = pm.getProcessor();
					}
					if(name ==null||name.length() ==0)
					{
						return null;
					}
					try {
						//实例化类
						Class c = Class.forName(name);
						processor = (ProtocolProcessor) c.newInstance();
						
						Set<String> propertySet = pm.getPropertyMap().keySet();
						if(propertySet != null && propertySet.size() > 0){
							for(String propName : propertySet){
								String propValue = pm.getPropertyMap().get(propName);
								Class propValueClass = Class.forName(propValue);
								char firstChar = propName.charAt(0);
								propName = propName.replaceFirst(String.valueOf(firstChar), String.valueOf((char)(firstChar - 32)));
								Method method = null;
								
								Method[] methods = processor.getClass().getMethods();
								for(Method m : methods){
									if(m.getName().equals("set" + propName)){
										method = m;
										break;
									}
								}
								
								if(method != null){
									method.invoke(processor, propValueClass.newInstance());
								}
							}
						}
						
						classMapProcessor.put(key, processor);
						
					} catch (Exception e) {
						log.error("ProtocolProcessor init error ! ProtocolId："+key,e);
					}
					
					
				}
				else
				{
					processor =  (ProtocolProcessor) classMapProcessor.get(key);
					
				}
			}
		}
		return processor;		
	}
	/**
	 * 根据不同的消息类型选出不同的消息体，在多线程中使用是安全的
	 * @param key 消息类型
	 * @return 消息类
	 */
	public static  MessageBody buildBody(String key) {
		MessageBody body = null;

		body =  (MessageBody) classMapBody.get(key);
		if(body!=null)
		{
			body = body.clone();
		}
		else
		{	
			synchronized(BodyObject)
			{
				if((MessageBody) classMapBody.get(key)==null)	
				{
					ProtocolsProcessor pm =null;
					String name  = null;
					pm = Config.getInstance().getProcessorName(key);
					if(pm!=null)
					{
						name = pm.getMessageBody();

					}
					//不存在消息类路径
					if(name==null||name.length()==0)
					{
						return null;
					}
					try {
						Class	c = Class.forName(name);
						body = (MessageBody) c.newInstance();
						classMapBody.put(key, body);
						body = body.clone();
						}catch (Exception e) {
						 log.error("MessageBody init error , ProtocolId："+key,e);
						}
						
					}
				else
				{
					body =  (MessageBody) classMapBody.get(key);
					body = body.clone();
				}
			}
		}
		return body;		
	}
	/**
	 * 根据不同的消息类型选出不同的消息体中List型对象，在多线程中使用是安全的
	 * @param key 消息类型
	 * @return 消息类
	 */
	public static  MessageBody buildRelBody(String key) {
		MessageBody body = null;

		body =  (MessageBody) classRelMapBody.get(key);
		if(body!=null)
		{
			body = body.clone();
		}
		else
		{	
			synchronized(RelBodyObject)
			{
				if((MessageBody) classRelMapBody.get(key)==null)	
				{
					ProtocolsProcessor pm =null;
					String name  = null;
					pm = Config.getInstance().getProcessorName(key.split("-")[0]);
					if(pm!=null)
					{
						String str [] = key.split("-");
						if(str.length>2)
						{
							name = str[str.length-1];
						}
					 
					}
					//不存在消息类路径
					if(name==null||name.length()==0)
					{
						return null;
					}
					try {
						Class	c = Class.forName(name);
						body = (MessageBody) c.newInstance();
						classRelMapBody.put(key, body);
						body = body.clone();
						}catch (Exception e) {
						 log.error("MessageBody-Reffer Init error, ProtocolId："+key,e);
						}
						
					}
				else
				{
					body =  (MessageBody) classRelMapBody.get(key);
					body = body.clone();
				}
			}
		}
		return body;		
	}
	/**
	 * 根据不同的消息类型选出不同的消息头，在多线程中使用是安全的
	 * @param key 消息类型
	 * @return 消息类
	 */	
	
	public static MessageHeader buildHeader(String key) {
		MessageHeader header = null;
		//读入已加载入hashmap中的类
		header = (MessageHeader) classMapHeader.get(key);
		if(header!=null)
		{
			header =  header.clone();
		}
		else
		{	
			synchronized(HeaderObject)
			{
				if((MessageHeader) classMapHeader.get(key)==null)
				{
					ProtocolsHead head =  Config.getInstance().getProtocolsHead(key);
					if(head==null)
					{
						return null;
					}
					String name = head.getClassName();
					if(name==null||name.length()==0)
					{
						return null;
					}
					try {
						
							Class	c = Class.forName(name);
							header = (MessageHeader) c.newInstance();
							classMapHeader.put(key, header);
							header =  header.clone();
						} catch (Exception e) {
							log.error("MessageHeader init error ,  ProtocolId："+key,e);
						}	
				}
				else
				{
					header = (MessageHeader) classMapHeader.get(key);
					header =  header.clone();
				}
			}
		}
		
		return header;		
	}
}
