package org.epilot.ccf.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.mina.common.ByteBuffer;
import org.epilot.ccf.core.protocol.Message;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ASObject;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

public class AmfDataConvert {
	
	public static byte[] objToBytes(ASObject message){
		SerializationContext serializationContext = new SerializationContext();
		serializationContext.legacyCollection = true;
		// 序列化amf3对象
		Amf3Output amfout = new Amf3Output(serializationContext);

		// 实现了一个输出流，其中的数据被写入一个 byte 数组。
		ByteArrayOutputStream byteoutStream = new ByteArrayOutputStream();

		// 将byteoutStream产生的数组流导入到DataOutputStream流中
		DataOutputStream dataoutstream = new DataOutputStream(byteoutStream);

		// 设置流的编码格式为amf3
		amfout.setOutputStream(dataoutstream);
		
		try {
			amfout.writeObject(message);// 实际上是将message对象写入到dataoutstream流中
			dataoutstream.flush();// 清空缓存
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 将ByteArrayOutputStream流中转化成字节数组
		byte[] messageBytes = byteoutStream.toByteArray();// amf3数据
		return messageBytes;
	}
	
	
	public static Object byteToObj(ByteBuffer in){
		SerializationContext serializationContext = new SerializationContext();
		Amf3Input amfInput = new Amf3Input(serializationContext);
		Object obj = null;
		try {
			int length = 64;
			int offset = 0;
			byte[] bytes = new byte[in.limit()];
			while(in.hasRemaining()){
				if(in.limit()-in.position() < length)
					length = in.limit()-in.position();
				in.get(bytes, offset, length);
				offset += length;
			}
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
			DataInputStream dataInputStream = new DataInputStream(byteInputStream);
			amfInput.setInputStream(dataInputStream);
			
		
			obj = amfInput.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
