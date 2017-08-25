package org.epilot.ccf.codec;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.epilot.ccf.core.code.ProtocolChange;
import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.protocol.ProtocolSequence;
import org.epilot.ccf.core.util.AbstractStringHandle;
import org.epilot.ccf.core.util.ByteBufferDataHandle;
import org.epilot.ccf.mapping.ClassMapping;
import org.epilot.ccf.mapping.MappingMethodCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 具体实现将数据流转化为对象,对象转化为数据流
 * @author tangjie
 *
 */
public class OldMessageDataHandle extends ProtocolChange {
	
	

	private static final Logger log =LoggerFactory.getLogger("ccf");
	
	public OldMessageDataHandle()
	{
	
	}
	public void getBodyBuffer(String key,MessageBody body,
			ByteBufferDataHandle byteBufferDataHandle) {
		ProtocolSequence ps = body.getSequnce();
		MethodInvoke(key,ps,body,byteBufferDataHandle,false);
	}

	public void getHeaderBuffer(String key,MessageHeader header,
			ByteBufferDataHandle byteBufferDataHandle) {
	
		ProtocolSequence ps = header.getSequnce();
		MethodInvoke(key,ps,header,byteBufferDataHandle,false);
	}
	public void setBodyObject(String key,MessageBody body,
			ByteBufferDataHandle byteBufferDataHandle) {

		ProtocolSequence ps = body.getSequnce();
		MethodInvoke(key,ps,body,byteBufferDataHandle,true);

	}
	public void setHeaderObject(String key,MessageHeader header,
			ByteBufferDataHandle byteBufferDataHandle) {
	
		
		ProtocolSequence ps = header.getSequnce();
		
		MethodInvoke(key,ps,header,byteBufferDataHandle,true);
	
	}
	/**
	 * @param flag 类识别码（协议标识）
	 * @param ps 协议的字段顺序
	 * @param obj 
	 * @param byteBufferDataHandle
	 * @param SG true为set方法,false 表示 get方法
	 */
	private void MethodInvoke(String flag,ProtocolSequence ps,
			Object obj,
			ByteBufferDataHandle byteBufferDataHandle,
			boolean SG){	
			
	try 
	{
		AbstractStringHandle stringHandle = null;
		Iterator it = ps.get();
		MethodObject methodObj = MappingMethodCache.getInstance().getSetMethod(obj,flag,it);
		if(methodObj==null)
		{
			if(log.isInfoEnabled())
			{
				log.info(obj.getClass().getName()+" class config error!");
			}
		 
		}
		else
		{
			if(SG)
			{
				LinkedHashMap methodsMap  = methodObj.getSetMethod();
				Set set = methodsMap.keySet();
				Iterator iterator = set.iterator();
				String MethodCode[] = methodObj.getSetMethodCode();
				String MethodType[] = methodObj.getSetMethodType();
				int  i =0;
				while(iterator.hasNext())
				{
					String name = (String) iterator.next();
					Method method = (Method)methodsMap.get(name);
					if(MethodType[i].startsWith("class-"))//对象类型
					{
						String className = MethodType[i].split("-")[1];
						MessageBody body = ClassMapping.buildRelBody(flag+"-rel-"+className);
						setBodyObject(flag+"-rel-"+className,body,byteBufferDataHandle);
						method.invoke(obj,body);
					}
					else if(MethodType[i].startsWith("classArray-"))//对象数组类型
					{
						String className = MethodType[i].split("-")[1];
						String count = String.valueOf(((Method)methodObj.getGetMethod().get( MethodType[i].split("-")[2])).invoke(obj));
					
						if(count!=null)
						{
							int num = Integer.valueOf(count);
							if(num>0)
							{
								List list = new ArrayList(num);
								for(int j=0;j<num;j++)
								{
									MessageBody body = ClassMapping.buildRelBody(flag+"-rel-"+className);
									setBodyObject(flag+"-rel-"+className,body,byteBufferDataHandle);
									list.add(body);
								}
								method.invoke(obj,new Object[]{list});
							}
						}
						else
						{
							log.warn("The object[] is not set size!");
						}
					}
					else
					{
						if(MethodType[i].startsWith("String-"))
						{
							stringHandle = ClassMapping.buildStringHandle(MethodType[i].substring(7));
						}
						else
						{
							if(MethodType[i].indexOf("String")!=-1)
							{
								stringHandle =new DefaultStringHandle();
							}
						}
						method.invoke(obj, new Object[]{ReadBuffer(MethodType[i],MethodCode[i], stringHandle, byteBufferDataHandle)});
					}
					i++;
				}
			}
			else
			{
				LinkedHashMap methodsMap  = methodObj.getGetMethod();
				Set set = methodsMap.keySet();
				Iterator iterator = set.iterator();
				String MethodCode[] = methodObj.getGetMethodCode();
				String MethodType[] = methodObj.getGetMethodType();
				int  i =0;
				while(iterator.hasNext())
				{
					String name = (String) iterator.next();
					Method method = (Method)methodsMap.get(name);
					if(MethodType[i].startsWith("class-"))//对象类型
					{
						String className = MethodType[i].split("-")[1];
						MessageBody body = (MessageBody) method.invoke(obj);
						getBodyBuffer(flag+"-rel-"+className,body,byteBufferDataHandle);
					}
					else if(MethodType[i].startsWith("classArray-"))//对象数组类型
					{
						
						String className = MethodType[i].split("-")[1];
						
						List body = (List) method.invoke(obj);
						if(body!=null)
						{
								for(int j=0;j<body.size();j++)
								{
									getBodyBuffer(flag+"-rel-"+className,(MessageBody) body.get(j),byteBufferDataHandle);
								}
						}
					}
					else
					{
						if(MethodType[i].startsWith("String-"))
						{
							stringHandle = ClassMapping.buildStringHandle(MethodType[i].substring(7));
						}
						else
						{
							if(MethodType[i].indexOf("String")!=-1)
							{
								stringHandle =new DefaultStringHandle();
							}
						}
						
						WriteBuffer(MethodType[i],MethodCode[i], stringHandle,method.invoke(obj),byteBufferDataHandle);
					}
					i++;
				}
			}
			
		}
   	 }
	   catch (Exception e) {
		   log.error(obj.getClass().getName()+",system will be closed!",e);
		   System.exit(0);
	}
}
	private Object ReadBuffer(String type,String endian,AbstractStringHandle stringHandle,ByteBufferDataHandle byteBufferDataHandle)
	{
	
		if(endian.equals("0"))
		{
			if(type.equalsIgnoreCase("byte"))
			{
				byteBufferDataHandle.getBigInt();
				return byteBufferDataHandle.getBigByte();
			}
			else if(type.equalsIgnoreCase("Short"))
			{
				return byteBufferDataHandle.getBigShort();
			}
			else if(type.equalsIgnoreCase("int"))
			{
				byteBufferDataHandle.getBigInt();
				return byteBufferDataHandle.getBigInt();
			}
			else if(type.equalsIgnoreCase("float"))
			{
				byteBufferDataHandle.getBigInt();
				return byteBufferDataHandle.getBigFloat();
			}
			else if(type.equalsIgnoreCase("double"))
			{
				byteBufferDataHandle.getBigInt();
				return byteBufferDataHandle.getBigDouble();
			}
			else if(type.equalsIgnoreCase("long"))
			{
				byteBufferDataHandle.getBigInt();
				return byteBufferDataHandle.getBigLong();
			}
			else if(type.indexOf("String")!=-1)
			{
				byteBufferDataHandle.getBigInt();
				return byteBufferDataHandle.getBigString(stringHandle);
				
			}
			else if(type.startsWith("class [B"))//byte数组对象
			{
				byte b [] = null;
				int i = byteBufferDataHandle.remain();
				if(i>0)
				{
					b = new byte[i];
					byteBufferDataHandle.readBigByte(b);
				}
				return b;
			}
			else
			{
				
				return null;
			}
		}
		else if(endian.equals("1"))
		{
			if(type.equalsIgnoreCase("byte"))
			{
				byteBufferDataHandle.getLittleInt();
				return byteBufferDataHandle.getLittleByte();
			}
			else if(type.equalsIgnoreCase("Short"))
			{
				return byteBufferDataHandle.getLittleShort();
			}
			else if(type.equalsIgnoreCase("int"))
			{
				byteBufferDataHandle.getLittleInt();
				return byteBufferDataHandle.getLittleInt();
			}
			else if(type.equalsIgnoreCase("float"))
			{
				byteBufferDataHandle.getLittleInt();
				return byteBufferDataHandle.getLittleFloat();
			}
			else if(type.equalsIgnoreCase("double"))
			{
				byteBufferDataHandle.getLittleInt();
				return byteBufferDataHandle.getLittleDouble();
			}
			else if(type.equalsIgnoreCase("long"))
			{
				byteBufferDataHandle.getLittleInt();
				return byteBufferDataHandle.getLittleLong();
			}
			else if(type.indexOf("String")!=-1)
			{
				byteBufferDataHandle.getLittleInt();
				return byteBufferDataHandle.getLittleString(stringHandle);
			}
			else if(type.startsWith("class [B"))//byte数组对象
			{
				byte b [] = null;
				int i = byteBufferDataHandle.remain();
				if(i>0)
				{
					b = new byte[i];
					byteBufferDataHandle.readLittleByte(b);
				}
				return b;
			}
			else
			{
				if(log.isWarnEnabled())
				{
					log.warn("The data type is not be identified :"+type);
				}
				return null;
			}
		}
		else
		{
			if(log.isWarnEnabled())
			{
				log.warn("The data type is not be identified :"+endian);
			}
			return null;
		}
	}
	private void WriteBuffer(String type,String endian,AbstractStringHandle stringHandle,Object obj,ByteBufferDataHandle byteBufferDataHandle)
	{
	
		if(endian.equals("0"))
		{
			if(type.equalsIgnoreCase("byte"))
			{
				byteBufferDataHandle.setBigData(0);
				byteBufferDataHandle.setBigData((Byte)obj);
			}
			else if(type.equalsIgnoreCase("Short"))
			{
				 byteBufferDataHandle.setBigData((Short)obj);
			}
			else if(type.equalsIgnoreCase("int"))
			{
				 byteBufferDataHandle.setBigData(2);
				 byteBufferDataHandle.setBigData((Integer)obj);
			}
			else if(type.equalsIgnoreCase("float"))
			{
				 byteBufferDataHandle.setBigData(3);
				 byteBufferDataHandle.setBigData((Float)obj);
			}
			else if(type.equalsIgnoreCase("double"))
			{
				 byteBufferDataHandle.setBigData(7);
				 byteBufferDataHandle.setBigData((Double)obj);
			}
			else if(type.equalsIgnoreCase("long"))
			{
				 byteBufferDataHandle.setBigData(9);
				 byteBufferDataHandle.setBigData((Long)obj);
			}
			else if(type.indexOf("String")!=-1)
			{
				 byteBufferDataHandle.setBigData(4);
				 byteBufferDataHandle.setBigData((String)obj,stringHandle);	
				
			}
			else if(type.startsWith("class [B"))//byte数组对象
			{
				if(obj!=null)
				{
					byteBufferDataHandle.writeBigByte((byte[])obj);
				}
			}
			else
			{

				if(log.isWarnEnabled())
				{
					log.warn("The data type is not be identified :"+type);
				}
				
			}
		}
		else if(endian.equals("1"))
		{
			if(type.equalsIgnoreCase("byte"))
			{
				 byteBufferDataHandle.setLittleData(0);
				 byteBufferDataHandle.setLittleData((Byte)obj);
			}
			else if(type.equalsIgnoreCase("Short"))
			{
				 byteBufferDataHandle.setLittleData((Short)obj);
			}
			else if(type.equalsIgnoreCase("int"))
			{
				 byteBufferDataHandle.setLittleData(2);
				 byteBufferDataHandle.setLittleData((Integer)obj);
			}
			else if(type.equalsIgnoreCase("float"))
			{
				 byteBufferDataHandle.setLittleData(3);
				 byteBufferDataHandle.setLittleData((Float)obj);
			}
			else if(type.equalsIgnoreCase("double"))
			{
				 byteBufferDataHandle.setLittleData(7);
				 byteBufferDataHandle.setLittleData((Double)obj);
			}
			else if(type.equalsIgnoreCase("long"))
			{
				 byteBufferDataHandle.setLittleData(9);
				 byteBufferDataHandle.setLittleData((Long)obj);
			}
			else if(type.indexOf("String")!=-1)
			{	
				 byteBufferDataHandle.setLittleData(4);
				byteBufferDataHandle.setLittleData((String)obj,stringHandle);
				
			}
			else if(type.startsWith("class [B"))//byte数组对象
			{
				if(obj!=null)
				{
					byteBufferDataHandle.writeLittleByte((byte[])obj);
				}
			}
			else
			{

				if(log.isWarnEnabled())
				{
					log.warn("The data type is not be identified :"+type);
				}
				
			}
		}
		else
		{

			if(log.isWarnEnabled())
			{
				log.warn("The data type is not be identified :"+endian);
			}
			
		}
	}
	
	
}
